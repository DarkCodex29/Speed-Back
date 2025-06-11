package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.AlfrescoConfig;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.repository.speed.AcumulacionRepository;
import com.hochschild.speed.back.repository.speed.ExpedienteRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.service.AlfrescoService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.AssertUtils;
import com.hochschild.speed.back.util.EncryptUtil;
import com.hochschild.speed.back.util.exception.ExcepcionAlfresco;
import org.alfresco.webservice.authoring.AuthoringServiceSoapBindingStub;
import org.alfresco.webservice.authoring.CheckoutResult;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.*;
import org.alfresco.webservice.util.*;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("AlfrescoService")
public class AlfrescoServiceImpl implements AlfrescoService {
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(AlfrescoServiceImpl.class.getName());
    protected final Store STORE = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
    private final AlfrescoConfig alfrescoConfig;
    private final AcumulacionRepository acumulacionRepository;
    private String urlAPIAlfresco;

    private final ExpedienteRepository expedienteRepository;
    private final UsuarioRepository usuarioRepository;
    @Autowired
    public AlfrescoServiceImpl(AcumulacionRepository acumulacionRepository, UsuarioRepository usuarioRepository, AlfrescoConfig alfrescoConfig, ExpedienteRepository expedienteRepository) {
        this.acumulacionRepository = acumulacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.alfrescoConfig = alfrescoConfig;
        this.urlAPIAlfresco = "http://" + alfrescoConfig.getServidor() + ":" + alfrescoConfig.getPuerto() + "/alfresco/api/-default-/cmis/versions/1.1/atom";
        this.expedienteRepository = expedienteRepository;
    }

    @Override
    public Map<String, Object> descargarArchivo(String space, String fileName) throws Exception {
        ContentStream content;
        Document nodeDocument;
        Folder nodeSpace, rootSpace;
        Map<String, Object> result;
        Session session;

        AssertUtils.isNotEmptyString(space, "space was not received");
        AssertUtils.isNotEmptyString(fileName, "fileName was not received");
        LOGGER.debug("space [{}] " + space +  " fileName [{}] " +   fileName);
        session = iniciarSesionAlfresco();

        if (session == null) {
            throw new ExcepcionAlfresco("No se pudo iniciar sesi\u00f3n en Alfresco");
        }

        rootSpace = session.getRootFolder();
        LOGGER.debug("rootSpace [{}]" + rootSpace.getPath());
        nodeSpace = (Folder) obtenerNodoRutaAbsoluta(session, rootSpace, space);
        nodeDocument = (Document) obtenerNodoRutaAbsoluta(session, nodeSpace, fileName);

        if (nodeDocument == null || nodeDocument.getContentStream() == null) {
            throw new ExcepcionAlfresco("El archivo " + fileName + " no tiene contenido");
        }

        content = nodeDocument.getContentStream();
        result = new HashMap<>();
        result.put("mime", content.getMimeType());
        result.put("stream", content.getStream());
        return result;
    }
    
    @Override
    public String obtenerUrlAlfresco(Archivo archivo){
        Session sesion=iniciarSesionAlfresco();
        if(sesion != null){
            Document documento=obtenerDocumento(sesion,archivo);
            if(documento != null){
                LOGGER.debug("url [{}] "+ documento.getContentUrl());
                return documento.getContentUrl();
            }
            LOGGER.error("No se encontr\u00f3 el archivo [" + archivo.getNombre() + "]");
        }
        return null;
    }
    
    @Override
    public String obtenerIdAlfresco(Archivo archivo){
        Session sesion=iniciarSesionAlfresco();
        if(sesion != null){
            Document documento=obtenerDocumento(sesion,archivo);
            if(documento != null){
                LOGGER.debug("url [{}] "+ documento.getContentUrl());
                return documento.getId();
            }
            LOGGER.error("No se encontr\u00f3 el archivo [" + archivo.getNombre() + "]");
        }
        return null;
    }

    @Override
    public boolean existeArchivo(Archivo archivo){
        Session sesion=iniciarSesionAlfresco();
        if(sesion != null){
            return obtenerDocumento(sesion,archivo) != null;
        }
        return false;
    }

    @Override
    public void obtenerVersiones(Archivo archivo){
        Session sesion=iniciarSesionAlfresco();
        if(sesion != null){
            Document documento=obtenerDocumento(sesion,archivo);
            if(documento != null){
                LOGGER.info("Obteniendo versiones del documento " + documento.toString());
                List<Document> versiones=documento.getAllVersions();
                if(versiones.size() > 0){
                    archivo.setVersion(versiones.get(0).getVersionLabel());
                }
                List<VersionArchivo> anteriores=new ArrayList<VersionArchivo>();
                for(int i=1;i < versiones.size();i++){
                    Document version=versiones.get(i);
                    LOGGER.info("Version: " + version.getVersionLabel());
                    VersionArchivo va=new VersionArchivo();
                    LOGGER.info("label "+ version.getVersionLabel() +" createdBy "+  version.getCreatedBy());
                    va.setNumeroVersion(version.getVersionLabel());
                    va.setAutor(usuarioRepository.obtenerPorUsuario(version.getCreatedBy()).getLabel());
                    anteriores.add(va);
                }
                archivo.setVersiones(anteriores);
            }
            else{
                LOGGER.error("No se encontr\u00f3 el archivo [" + archivo.getNombre() + "]");
            }
        }
    }
    @Override
    public String getTicket() {
        String ticket = null;
        HttpClient client = new HttpClient();
        String password;
        String apiurl = "http://" + alfrescoConfig.getServidor() + ":" + alfrescoConfig.getPuerto() + "/alfresco/service/api/login";
        PostMethod post = new PostMethod(apiurl);

        try {
            password = EncryptUtil.decrypt(EncryptUtil.DEFAULT_ENCRYPTION_KEY, alfrescoConfig.getPassword());
            JSONObject login = new JSONObject();
            login.put("username", alfrescoConfig.getUser());
            login.put("password", password);

            post.setDoAuthentication(true);
            post.setRequestHeader("Content-Type", "application/json");
            post.setRequestEntity(new StringRequestEntity(login.toString(), "application/json", "UTF-8"));

            int status = client.executeMethod(post);

            if (status != HttpStatus.SC_OK) {
                throw new RuntimeException("Method failed: " + post.getStatusLine());
            }

            String responseData = post.getResponseBodyAsString();

            LOGGER.debug("responseData " + responseData);
            JSONObject response = new JSONObject(responseData);
            ticket = response.getJSONObject("data").getString("ticket");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }
        return ticket;
    }

    @Override
    public String getDownloadUrl(Archivo archivo) {
        String ticket;

        ticket=getTicket();
        return obtenerUrlAlfresco(archivo) + "&alf_ticket=" + ticket;
    }
    @Override
    public List<String> buscarExpedientesPorContenido(String query){
        Session sesion=iniciarSesionAlfresco();
        if(sesion != null){
            List<String> expedientes=new ArrayList<String>();
            QueryStatement stmt=sesion.createQueryStatement("CONTAINS(?)");
            stmt.setString(1,query);
            ItemIterable<CmisObject> objetos=sesion.queryObjects("cmis:document",stmt.toQueryString(),false,sesion.getDefaultContext());
            for(CmisObject objeto : objetos){
                if(objeto instanceof Document){
                    Document documento=(Document) objeto;
                    List<Folder> padres=documento.getParents();
                    for(Folder padre : padres){
                        expedientes.add(padre.getName());
                    }
                }
            }
            return expedientes;
        }
        return null;
    }
    @Override
    public String subirArchivo(String space, String fileLocalPathName) throws ExcepcionAlfresco {
        Document nodeDocument;
        File file;
        Folder nodeSpace, rootSpace;
        Session session;
        String fileName;

        AssertUtils.isNotEmptyString(space, "space was not received");
        AssertUtils.isNotEmptyString(fileLocalPathName, "fileLocalPathName was not received");
        session = iniciarSesionAlfresco();

        if (session == null) {
            throw new ExcepcionAlfresco("No se pudo iniciar sesi\u00f3n en Alfresco");
        }

        fileName = fileLocalPathName.substring(fileLocalPathName.lastIndexOf("]") + 1);

        //LOGGER.debug("space [{}] fileLocalPathName [{}] fileName [{}]", space, fileLocalPathName, fileName);
        rootSpace = session.getRootFolder();
        //log.debug("rootSpace [{}]", rootSpace.getPath());
        nodeSpace = (Folder) obtenerNodoRutaAbsoluta(session, rootSpace, space);
        nodeDocument = (Document) obtenerNodoRutaAbsoluta(session, nodeSpace, fileName);
        file = new File(fileLocalPathName);

        if (file.exists()) {
            if (nodeDocument == null) {
                crearDocumento(session, nodeSpace, fileName, file);
            } else {
                versionarDocumento(session, nodeSpace, fileName, file);
            }

            return nodeSpace.getPath() + "/" + fileName;
        }

        return null;
    }

    private boolean verificarExistenciaNodo(Session sesion, Folder padre, String nombre) {
        try {
            sesion.getObjectByPath(com.hochschild.speed.back.util.StringUtils.getFullPathWithoutSeparatorAtTheEnd(padre.getPath(), nombre));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String subirArchivoPlantilla(String space, String fileLocalPathName, String name) throws ExcepcionAlfresco {

        Boolean nodeDocument;
        File file;
        Folder nodeSpace, rootSpace;
        Session session;

        AssertUtils.isNotEmptyString(space, "space was not received");
        AssertUtils.isNotEmptyString(fileLocalPathName, "fileLocalPathName was not received");
        session = iniciarSesionAlfresco();

        if (session == null) {
            throw new ExcepcionAlfresco("No se pudo iniciar sesi\u00f3n en Alfresco");
        }

        rootSpace = session.getRootFolder();
        nodeSpace = (Folder) obtenerNodoRutaAbsoluta(session, rootSpace, space);
        nodeDocument = verificarExistenciaNodo(session, nodeSpace, name);
        file = new File(fileLocalPathName);

        if (file.exists()) {
            if (!nodeDocument) {
                crearDocumento(session, nodeSpace, name, file);
            } else {
                versionarDocumento(session, nodeSpace, name, file);
            }

            return nodeSpace.getPath() + "/" + name;
        }
        return null;
    }

    @Override
    public Map<String, Object> obtenerArchivo(Archivo archivo, String version) {
        Session sesion=iniciarSesionAlfresco();
        if(sesion != null){
            Document documento=obtenerDocumento(sesion,archivo);
            if(documento != null){
                if(!AppUtil.checkNullOrEmpty(version)){
                    List<Document> versiones=documento.getAllVersions();
                    for(Document v : versiones){
                        if(v.getVersionLabel().equals(version)){
                            documento=v;
                        }
                    }
                }
                ContentStream content=documento.getContentStream();
                if(content != null){
                    Map<String,Object> salida=new HashMap<String,Object>();
                    salida.put("mime",content.getMimeType());
                    salida.put("stream",content.getStream());
                    return salida;
                }
            }
        }
        return null;
    }

    @Override
    public void subirArchivos(List<Documento> documentos) throws ExcepcionAlfresco {
        Session session = iniciarSesionAlfresco();
        if (session != null) {
            LOGGER.info("Numero de documentos :" + documentos.size());
            for (Documento documento : documentos) {
                Expediente expediente = obtenerExpedienteOriginal(documento);
                expediente = expedienteRepository.findExpedienteById(expediente.getId());
                Calendar fecha = Calendar.getInstance();
                fecha.setTime(expediente.getFechaCreacion());
                String[] datos = new String[]{alfrescoConfig.getSpace(), "" + fecha.get(Calendar.YEAR), "" + (fecha.get(Calendar.MONTH) + 1), "" + fecha.get(Calendar.DAY_OF_MONTH), expediente.getNumero()};
                Folder padre = session.getRootFolder();
                String ruta = "";
                for (String dato : datos) {
                    ruta += dato + "/";
                    LOGGER.info("Ruta archivo en Alfresco :" + ruta);
                    Folder nodo = (Folder) obtenerNodo(session, padre, dato);
                    if (nodo == null) {
                        padre = crearNodo(padre, dato);
                    } else {
                        padre = nodo;
                    }
                }
                for (Archivo archivo : documento.getArchivos()) {
                    File file = new File(archivo.getRutaLocal());
                    if(archivo.isNuevo()){
                        crearDocumento(session,padre,archivo.getNombre(),file);
                    }
                    else{
                        versionarDocumento(session,padre,archivo.getNombre(),file);
                    }
                }
            }
        }
    }
    private void crearDocumento(Session sesion,Folder padre,String nombre,File archivo) throws ExcepcionAlfresco{
        String mimeType;
        try{
            mimeType=Files.probeContentType(archivo.toPath());
            if(StringUtils.isBlank(mimeType)){
                mimeType=Constants.MIMETYPE_TEXT_PLAIN;
            }
            ContentStream contentStream=sesion.getObjectFactory().createContentStream(nombre,archivo.length(),mimeType,new FileInputStream(archivo));
            Map<String,Object> newDocumentProps=new HashMap<String,Object>();
            String typeId="cmis:document";
            newDocumentProps.put(PropertyIds.OBJECT_TYPE_ID,typeId);
            newDocumentProps.put(PropertyIds.NAME,nombre);
            VersioningState versioningState=VersioningState.NONE;
            DocumentType docType=(DocumentType) sesion.getTypeDefinition(typeId);
            if(Boolean.TRUE.equals(docType.isVersionable())){
                versioningState=VersioningState.MAJOR;
            }
            padre.createDocument(newDocumentProps,contentStream,versioningState);
            LOGGER.info("Se creo el archivo" + nombre +"dentro de "+padre.getPath());
        }
        catch(IOException e){
            e.printStackTrace();
            throw new ExcepcionAlfresco("Error leyendo archivo [" + nombre + "]");
        }catch(CmisContentAlreadyExistsException ex){
            throw new ExcepcionAlfresco("Error, el archivo ya esta cargado [" + nombre + "]");
        }
    }


    private void versionarDocumento(Session sesion,Folder padre,String nombre,File archivo) throws ExcepcionAlfresco{
        Document documento=(Document) obtenerNodo(sesion,padre,nombre);
        if(documento != null){
            try{
                String mimeType=Files.probeContentType(archivo.toPath());
                ContentStream contentStream=sesion.getObjectFactory().createContentStream(nombre,archivo.length(),mimeType,new FileInputStream(archivo));
                ObjectId id=documento.checkOut();
                Document pwc=(Document) sesion.getObject(id);
                pwc.checkIn(false,null,contentStream,"Versionamiento de archivo");
                LOGGER.info("Se versiono el archivo "+ nombre+  " dentro de " +padre.getPath());
            }
            catch(IOException e){
                throw new ExcepcionAlfresco("Error leyendo archivo [" + nombre + "]");
            }
        }
    }
    private boolean iniciarSesion() {
        String password;
        WebServiceFactory.setEndpointAddress(urlAPIAlfresco);
        try {
            password = EncryptUtil.decrypt(EncryptUtil.DEFAULT_ENCRYPTION_KEY, alfrescoConfig.getPassword());
            AuthenticationUtils.startSession(alfrescoConfig.getUser(), password);
            AuthenticationDetails detail = AuthenticationUtils.getAuthenticationDetails();
            if (!AppUtil.checkNullOrEmpty(detail.getSessionId())) {
                LOGGER.info("Se inicio sesion en Alfresco");
                return true;
            }
        } catch (Exception ex) {
            LOGGER.info("No se pudo iniciar sesion en Alfresco " + ex);
        }
        return false;
    }
    private Session iniciarSesionAlfresco(){
        String password;
        try{
            password= EncryptUtil.decrypt(EncryptUtil.DEFAULT_ENCRYPTION_KEY,alfrescoConfig.getPassword());
            SessionFactory sessionFactory= SessionFactoryImpl.newInstance();
            Map<String,String> parameters=new HashMap<>();
            parameters.put(SessionParameter.USER,alfrescoConfig.getUser());
            parameters.put(SessionParameter.PASSWORD,password);
            parameters.put(SessionParameter.ATOMPUB_URL,urlAPIAlfresco);
            parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
            parameters.put(SessionParameter.COMPRESSION,"true");
            parameters.put(SessionParameter.CACHE_TTL_OBJECTS,"0");
            LOGGER.info(parameters);
            List<Repository> repositories=sessionFactory.getRepositories(parameters);
            if(repositories != null && !repositories.isEmpty()){
                return repositories.get(0).createSession();
            }
        }
        catch(Exception e){
            LOGGER.error("Error al momento de iniciar sesion en Alfresco",e);
        }
        return null;
    }
    private void terminarSession() {
        AuthenticationUtils.endSession();
    }

    private Expediente obtenerExpedienteOriginal(Documento documento) {
        AcumulacionDocumento acumulacion = acumulacionRepository.obtenerPrimeraPorDocumento(documento.getId());
        if (acumulacion == null) {
            return documento.getExpediente();
        }
        return acumulacion.getExpedienteOrigen();
    }

    private CmisObject obtenerNodo(Session sesion,Folder padre,String nombre) {
        try{
            String path=padre.getPath();
            if(!path.endsWith("/")){
                path+="/";
            }
            path+=nombre;
            return sesion.getObjectByPath(path);
        }
        catch(CmisObjectNotFoundException e){
            return null;
        }
    }

    private CmisObject obtenerNodoRutaAbsoluta(Session sesion, Folder padre, String nombre) {
        try {
            return sesion.getObjectByPath(com.hochschild.speed.back.util.StringUtils.getFullPathWithoutSeparatorAtTheEnd(padre.getPath(), nombre));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private String traducirXPath(String ruta) {
        LOGGER.info("ruta:" + ruta);
        if (ruta != null) {
            String sruta[] = ruta.split("/");
            StringBuffer sb = new StringBuffer("/app:company_home");
            for (String dir : sruta) {
                sb.append("/*[@cm:name=\"").append(dir.trim()).append("\"]");
            }
            String rutaAlfresco = sb.toString();
            LOGGER.info("rutaAlfresco:" + rutaAlfresco);
            return rutaAlfresco;
        }
        return null;
    }

    private Folder crearNodo(Folder padre, String nombre) {
        Map<String,Object> newFolderProps=new HashMap<String,Object>();
        newFolderProps.put(PropertyIds.OBJECT_TYPE_ID,"cmis:folder");
        newFolderProps.put(PropertyIds.NAME,nombre);
        LOGGER.debug("Creando nodo [" + nombre + "] dentro de " + padre.getPath());
        return padre.createFolder(newFolderProps);

    }

    private String normilizarNodeName(String name) {
        return name.replace(" ", "_");
    }

    private boolean subirInFiletoAlfresco(InputStream inputStream, String nombreArchivo, String mimeType, Reference parentref, boolean update) {
        if (update) {
            return arctualizarArchivo(inputStream, nombreArchivo, mimeType, parentref);
        }
        return anexarArchivo(inputStream, nombreArchivo, mimeType, parentref);
    }

    private boolean arctualizarArchivo(InputStream inputStream, String nombreDocumento, String mimetype, Reference parentref) {

        LOGGER.info("Se va a actualizar el archivo en Alfresco");

        try {
            BufferedInputStream bufferedInput = new BufferedInputStream(inputStream);
            // Se abre el fichero donde se har\u00e1 la copia
            ByteArrayOutputStream bufferedOutput = new ByteArrayOutputStream();
            // Bucle para leer de un fichero y escribir en el otro.
            byte[] array = new byte[1024];
            int leidos = bufferedInput.read(array);
            while (leidos > 0) {
                bufferedOutput.write(array, 0, leidos);
                leidos = bufferedInput.read(array);
            }
            // Cierre de los ficheros
            byte[] data = bufferedOutput.toByteArray();
            bufferedInput.close();
            bufferedOutput.close();

            CML cml = new CML();
            Predicate predicate1 = new Predicate(new Reference[]{parentref}, null, null);
            CMLAddAspect addAspect = new CMLAddAspect(Constants.ASPECT_VERSIONABLE, null, predicate1, null);
            cml.setAddAspect(new CMLAddAspect[]{addAspect});

            LOGGER.info("Creando el documento " + nombreDocumento);
            UpdateResult[] results = WebServiceFactory.getRepositoryService().update(cml);
            results[0].getDestination();

            Reference updateRef = new Reference(STORE, null, parentref.getPath() + "/cm:" + codificarUnicode(nombreDocumento));
            AuthoringServiceSoapBindingStub authoringService = WebServiceFactory.getAuthoringService();
            Predicate itemsToCheckOut = new Predicate(new Reference[]{updateRef}, null, null);
            CheckoutResult checkOutResult = authoringService.checkout(itemsToCheckOut, null);

            Reference workingCopyReference = checkOutResult.getWorkingCopies()[0];
            // ContentServiceSoapBindingStub
            // contentService=WebServiceFactory.getContentService();

            // ContentFormat format=new
            // ContentFormat(Constants.MIMETYPE_TEXT_PLAIN,"UTF-8");
            if (StringUtils.isBlank(mimetype)) {
                mimetype = Constants.MIMETYPE_TEXT_PLAIN;
            }
            ContentFormat format = new ContentFormat(mimetype, "UTF-8");

            WebServiceFactory.getContentService().write(workingCopyReference, Constants.PROP_CONTENT, data, format);

            Predicate predicate = new Predicate(new Reference[]{workingCopyReference}, null, null);
            NamedValue[] comments = new NamedValue[]{Utils.createNamedValue("description", "El contenido ha sido actualizado.")};
            authoringService.checkin(predicate, comments, false);
            return true;

        } catch (IOException e) {
            LOGGER.info(e.getMessage(), e);
            return false;
        }
    }

    private String codificarUnicode(String entrada) {
        return ISO9075.encode(entrada);
    }

    private boolean anexarArchivo(InputStream inputStream, String nombreDocumento, String mimeType, Reference parentref) {

        LOGGER.info("Se va a crear el archivo en Alfresco");
        ParentReference parent;
        Reference document = null;
        String subEspacio = null;

        try {
            parent = ReferenceToParent(STORE, parentref, subEspacio);

            BufferedInputStream bufferedInput = new BufferedInputStream(inputStream);
            // Se abre el fichero donde se har\u00e1 la copia
            ByteArrayOutputStream bufferedOutput = new ByteArrayOutputStream();
            // Bucle para leer de un fichero y escribir en el otro.
            byte[] array = new byte[1024];
            int leidos = bufferedInput.read(array);
            while (leidos > 0) {
                bufferedOutput.write(array, 0, leidos);
                leidos = bufferedInput.read(array);
            }
            // Cierre de los ficheros
            byte[] data = bufferedOutput.toByteArray();
            bufferedInput.close();
            bufferedOutput.close();
            String child = Constants.createQNameString(Constants.NAMESPACE_CONTENT_MODEL, nombreDocumento);
            parent.setChildName(child);

            NamedValue prop = Utils.createNamedValue(Constants.PROP_NAME, nombreDocumento);
            NamedValue[] properties = new NamedValue[]{prop};
            CMLCreate create = new CMLCreate("1", parent, null, null, null, Constants.TYPE_CONTENT, properties);
            CML cml = new CML();
            Predicate predicate1 = new Predicate(new Reference[]{parentref}, null, null);
            CMLAddAspect addAspect = new CMLAddAspect(Constants.ASPECT_VERSIONABLE, null, predicate1, null);
            cml.setAddAspect(new CMLAddAspect[]{addAspect});
            cml.setCreate(new CMLCreate[]{create});
            // Execute the CML create statement

            UpdateResult[] results = WebServiceFactory.getRepositoryService().update(cml);
            document = results[0].getDestination();

            // ContentFormat format=new
            // ContentFormat(Constants.MIMETYPE_TEXT_PLAIN,"UTF-8");
            if (StringUtils.isBlank(mimeType)) {
                mimeType = Constants.MIMETYPE_TEXT_PLAIN;
            }
            ContentFormat format = new ContentFormat(mimeType, "UTF-8");

            LOGGER.info("Poniendo el contenido al documento");
            WebServiceFactory.getContentService().write(document, Constants.PROP_CONTENT, data, format);
            return true;

        } catch (IOException e) {
            LOGGER.info("Ocurrio un error manipulando el archivo", e);
            return false;
        }
    }

    private ParentReference ReferenceToParent(Store STORE, Reference referenciaEspacio, String hijo) {
        return new ParentReference(STORE, referenciaEspacio.getUuid(), referenciaEspacio.getPath(), Constants.ASSOC_CONTAINS, Constants.createQNameString(Constants.NAMESPACE_CONTENT_MODEL, hijo));
    }

/*
    @Override
    public List<String> buscarExpedientesPorContenido(String busquedaAlfresco) {

        if (busquedaAlfresco != "") {

            String EndpointAddress = urlAPIAlfresco;
            LOGGER.info("EndpointAddress :" + EndpointAddress);
            WebServiceFactory.setEndpointAddress(EndpointAddress);

            if (iniciarSesion()) {
                List<String> numeroExpedientes = new ArrayList<String>();
                RepositoryServiceSoapBindingStub repositoryService = null;
                Store store = null;
                QueryResult queryResult = null;
                ResultSetRow[] rows = null;
                repositoryService = WebServiceFactory.getRepositoryService();
                store = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
                Query queryG;
                String tmp = "TEXT:\'" + busquedaAlfresco + "\'";
                LOGGER.info("tmp :" + tmp);
                queryG = new Query(Constants.QUERY_LANG_LUCENE, tmp);

                try {
                    queryResult = repositoryService.query(store, queryG, false);
                    rows = queryResult.getResultSet().getRows();
                    if (rows != null) {
                        LOGGER.info("Cada row es un Expediente :" + rows.length);

                        for (ResultSetRow row : rows) {
                            LOGGER.info("UID: " + row.getNode().getId());
                            LOGGER.info("Type: " + row.getNode().getType());
                            // NamedValue[] values = row.getColumns();
                            LOGGER.info("Properties: ");

                            NamedValue namedValue = row.getColumns(0);
                            String contenido = namedValue.getValue();
                            String numExpediente = null;
                            LOGGER.info("El contenido es :" + contenido);
                            numExpediente = expresionRegularCadena(contenido);
                            if (numeroExpedientes != null) {
                                numeroExpedientes.add(numExpediente);
                            }
                        }
                        terminarSession();
                        LOGGER.info("Numero de expedientes encontrados en la búsqueda :" + numeroExpedientes.size());
                        return numeroExpedientes;

                    }
                } catch (RemoteException e) {
                    LOGGER.error("Error al acceder a Alfresco", e);
                }
            }
        }
        LOGGER.info("No hay coincidencias con la busqueda");
        return null;
    }
*/

    private String expresionRegularCadena(String cadena) {
        String numExpediente = null;
        Pattern p = Pattern.compile("20[0-9]{10}"); // expresion regular
        Matcher m = p.matcher(cadena);
        while (m.find()) {
            numExpediente = cadena.substring(m.start(), m.start() + 12);
        }
        return numExpediente;
    }

    private Document obtenerDocumento(Session sesion,Archivo archivo){
        Expediente expediente=obtenerExpedienteOriginal(archivo.getDocumento());
        expediente = expedienteRepository.findExpedienteById(expediente.getId());
        Calendar fecha=Calendar.getInstance();
        fecha.setTime(expediente.getFechaCreacion());
        String[] datos=new String[]{alfrescoConfig.getSpace(),"" + fecha.get(Calendar.YEAR),"" + (fecha.get(Calendar.MONTH) + 1),"" + fecha.get(Calendar.DAY_OF_MONTH),expediente.getNumero()};
        Folder padre=sesion.getRootFolder();
        String ruta="";
        for(String dato : datos){
            ruta+="/" + dato;
            padre=(Folder) obtenerNodo(sesion,padre,dato);
            if(padre == null){
                LOGGER.error("No existe el Folder [" + ruta + "]");
                return null;
            }
        }
        return (Document) obtenerNodo(sesion,padre,archivo.getNombre());
    }




}
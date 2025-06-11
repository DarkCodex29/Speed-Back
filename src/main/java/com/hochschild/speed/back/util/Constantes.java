package com.hochschild.speed.back.util;

public class Constantes {

    public static final String SESION_USUARIO = "usuarioSesion";
    public static final String SESION_GRID = "gridSesion";
    public static final String SESION_MENU = "menuSesion";
    public static final String SESION_IP = "ipSesion";
    public static final String SESION_TITULO = "tituloSesion";
    public static final String SESION_BUSQUEDAS_GUARDADAS = "busquedas";
    public static final String SESION_MAX_FILE_SIZE = "maxFileSize";
    public static final String SESION_DOCUMENTOS_GUARDADOS = "documentos";
    public static final String SESION_ARCHIVOS = "archivosSesion";
    public static final String SESION_VALORES_ORG = "valoresOrgSesion";

    /**
     * Estados *
     */
    public static final Character ESTADO_TODOS = '0';
    public static final Character ESTADO_ACTIVO = 'A';
    public static final Character ESTADO_INACTIVO = 'I';
    public static final Character ESTADO_GUARDADO = 'G';
    public static final Character ESTADO_REGISTRADO = 'R';
    public static final Character ESTADO_ENVIADO = 'E';
    public static final Character ESTADO_PROCESO = 'P';
    public static final Character ESTADO_TERMINADO = 'T';
    public static final Character ESTADO_BUSQUEDA = 'B';
    public static final Character ESTADO_ARCHIVADO = 'X';
    public static final Character ESTADO_ELIMINADO = 'N';
    public static final Character ESTADO_ABIERTO = 'A';
    public static final Character ESTADO_CERRADO = 'C';
    public static final Character ESTADO_DEVUELTO = 'D';
    public static final Character ESTADO_MENSAJERIA_RECIBIDO = 'R';
    public static final Character ESTADO_MENSAJERIA_ENVIADO = 'E';
    public static final Character ESTADO_MENSAJERIA_CARGO = 'C';
    public static final Character ESTADO_PAUSADO = 'U';
    public static final Character ESTADO_PENDIENTE = 'P';
    public static final Character ESTADO_ATENDIDO = 'O';
    public static final Character ESTADO_RECIBIDO = 'H';
    public static final Character ESTADO_POR_ENVIAR = 'Q';
    public static final Character ESTADO_APROBADO = 'L';
    public static final Character ESTADO_ACEPTADO = 'A';
    public static final Character ESTADO_OBSERVADO = 'O';
    public static final String ESTADO_REPCOMP_INACTIVO = "N";

    public static final String GRID_EXPEDIENTE = "Expediente";
    public static final String GRID_EXPEDIENTE_COPIA = "ExpedienteCopia";
    public static final String GRID_DOCUMENTO = "Documento";
    public static final String GRID_NUEVO_DOCUMENTO = "NuevoDocumento";
    public static final String GRID_MENU = "Menu";
    public static final String GRID_GRID = "Grid";
    public static final String GRID_BOTON = "Boton";
    public static final String GRID_BOTON_POR_GRID_POR_PERFIL = "BotonPorGridPorPerfil";
    public static final String GRID_USUARIO = "Usuario";
    public static final String GRID_REGISTRAR_EXPEDIENTE = "RegistrarExpediente";
    public static final String GRID_CAMPO = "Campo";
    public static final String GRID_TIPO_DOCUMENTO = "tipoDocumento";
    public static final String GRID_TIPO_ADJUNTAR_DOCUMENTO = "AdjuntarDocumento";
    public static final String GRID_NOTIFICACION = "Notificacion";
    public static final String GRID_PROCESO_USUARIO = "Proceso";
    public static final String GRID_NUEVO_DOCUMENTO_EXPEDIENTE = "DocumentoExpediente";
    public static final String GRID_WORKFLOW = "Workflow";
    public static final String GRID_TRAZA = "Traza";
    public static final String GRID_USUARIO_POR_TRAZA = "UsuarioPorTraza";
    public static final String GRID_AREA = "Area";
    public static final String GRID_REEMPLAZO = "Reemplazo";
    public static final String GRID_PROCESO = "Proceso";
    public static final String GRID_CLIENTE = "Cliente";
    public static final String GRID_MENSAJERIA = "Mensajeria";
    public static final String GRID_FERIADO = "Feriado";
    public static final String GRID_SEDE = "Sede";
    public static final String GRID_BUSQUEDA = "Busqueda";
    public static final String GRID_BUSQUEDA_SIMPLE = "buscarexpediente";
    public static final String GRID_CARGO = "Cargo";
    public static final String GRID_ENVIO_PENDIENTE = "EnvioPendiente";
    public static final String GRID_HCPLANTILLA = "HcPlantilla";
    public static final String GRID_HCPAIS = "HcPais";
    public static final String GRID_HCCOMPANIA = "HcCompania";
    public static final String GRID_HCAREA = "HcArea";
    public static final String GRID_HCUBICACION = "HcUbicacion";
    public static final String GRID_PARAMETRO = "Parametro";
    public static final String GRID_HCPENALIDAD = "HcPenalidad";
    public static final String GRID_HCGRUPO = "HcGrupo";
    public static final String GRID_REPRESENTANTE_COMP = "RepresentanteComp";

    public static final String URL_ADJUNTAR_ARCHIVO = "registrarExpediente/uploadFiles";

    /**
     * Acciones sobre el expediente *
     */
    public static final String ACCION_CREAR = "crear";
    public static final String ACCION_REGISTRAR = "Registrar";
    public static final String ACCION_COPIA = "Copia";
    public static final String ACCION_REGISTRAR_COPIA = "Copiar";
    public static final String ACCION_TERMINADO_PARALELO = "Expedientes paralelo terminado";
    public static final String ACCION_DERIVAR = "Derivar";
    public static final String ACCION_DERIVAR_MULTIPLE = "Derivaci\u00f3n m\u00faltiple";
    public static final String ACCION_RECHAZAR = "Rechazar";
    public static final String ACION_PARA_APROBAR = "Para Aprobar";
    public static final String ACCION_APROBAR = "Aprobado";
    public static final String ACCION_ARCHIVAR = "Archivar";
    public static final String ACCION_OBSERVAR = "Observar";
    public static final String ACCION_ACUMULAR = "Acumulaci\u00f3n";
    public static final String ACCION_ACEPTAR = "Aceptar";
    public static final String ACCION_ENVIAR_SOLICITUD = "Enviar Solicitud";
    public static final String ACCION_REABRIR = "Reabrir";
    public static final String ACCION_REGISTRO_MANUAL = "Registro manual";
    public static final String ACCION_ADJUNTAR_BORRADOR = "Adjuntar Borrador";
    public static final String ACCION_ENVIADO_VISADO = "Enviado a Visado";
    public static final String ACCION_VISADO = "Visado";
    public static final String ACCION_ENVIADO_FIRMA = "Enviado a Firma";
    public static final String ACCION_ENVIADO_FIRMA_ELECTRONICA = "Enviado a Firma Electrónica";
    public static final String ACCION_ENTREGA_DOCUMENTO = "Entrega de Documento";
    public static final String ACCION_ADJUNTAR_FIRMADO = "Adjuntar Firmado";
    public static final String ACCION_CAMBIO_ABOGADO_RESPONSABLE = "Cambio Abogado Resp";
    public static final String ACCION_ELIMINAR = "Eliminar";
    public static final String ACCION_CANCELAR_VISADO = "Cancelar visado";
    public static final String ACCION_ADENDA_VIGENTE = "Adenda vigente";
    public static final int ACCION_NUMEROS_DESTINO_PARALELO = 60008;

    /**
     * Errores *
     */
    public static final int ERROR_USUARIO_NO_AUTORIZADO = 60001;
    public static final int ERROR_EXPEDIENTE_INEXISTENTE = 60002;
    public static final int ERROR_REMITENTE_INEXISTENTE = 60005;
    public static final int ERROR_RESPONSABLE_INEXISTENTE = 60005;
    public static final int ERROR_DESTINO_INCORRECTO = 60003;
    public static final int ERROR_PLAZO_NO_INGRESADO_CORRECTAMENTE = 60004;
    public static final int ERROR_ROL_NO_PARTICIPANTE_PROCESO = 60006;
    public static final int ERROR_FATAL = 666;
    public static final int ERROR_DERIVAR_PARALELO = 60007;
    public static final int ERROR_DERIVAR_NO_WORKFLOW = 555555;
    public static final int ERROR_DERIVAR_NO_PASO_WORKFLOW = 555565;
    public static final int ERROR_USUARIO_NO_SESION = 777;
    public static final int ERROR_NOTIFICACION_INEXISTENTE = 60015;
    public static final int ERROR_APROBACION_ENVIO_PROCESO = 777;
    public static final int ERROR_LEER_ARCHIVO = 15001;
    public static final int ERROR_DOCUMENTO_INEXISTENTE = 15002;
    public static final int ERROR_USUARIO_INEXISTENTE = 15003;
    public static final int ERROR_ALFRESCO_SUBIR = 15004;
    public static final int ERROR_ALFRESCO_INACTIVO = 15005;
    public static final int ERROR_ARCHIVO_INEXISTENTE = 15006;
    public static final int ERROR_FIRMA_EXISTENTE = 15007;
    public static final int OK = 1;
    public static final String ERROR_GENERAL = "-100";

    /**
     * Perfiles *
     */
    public static final String PERFIL_ADMIN_CODIGO = "admin";
    public static final String PERFIL_MESA_DE_PARTES = "mesaPartes";
    public static final String PERFIL_USUARIO_FINAL = "usuarioFinal";
    public static final String PERFIL_DIGITALIZACION = "digitalizador";

    /**
     * Tipos de proceso *
     */
    public static final String TIPO_PROCESO_TRAMITE = "tramite";
    public static final String TIPO_PROCESO_INTALIO = "intalio";
    public static final String TIPO_PROCESO_WORKFLOW = "workflow";
    public static final String TIPO_PROCESO_INTERNO = "interno";

    public static final String CORRECTO = "OK";

    public static final String BOTON_ADJUNTAR_ARCHIVO = "AdjuntarArchivo";

    public static final String MENSAJE_OK = "OK";
    public static final String MENSAJE_ERROR_GENERICO = "ERROR";

    /**
     * Codigos *
     */
    public static final String CODIGO_COLUMNA_ALERTA = "alertGrid";
    public static final String CODIGO_COLUMNA_ID = "id";
    public static final String CODIGO_COLUMNA_ESTADO = "estado";
    public static final String CODIGO_FECHA_RECOMENDADA = "fechaRecomendada";
    public static final Object CODIGO_COMUNICACIONES_INTERNAS = "interno";

    public static final String EXPEDIENTE_RECHAZAR = "Rechazar";

    public static final String COLUMNA_TIPO_BOTON = "boton";
    public static final String COLUMNA_TIPO_IMAGEN = "imagen";
    public static final String COLUMNA_TIPO_ALERTA = "alerta";
    public static final String COLUMNA_TIPO_ESTADO_DL = "estadoDL";
    public static final String COLUMNA_TIPO_BOOLEAN = "boolean";

    public static final String COLUMNA_TIPO_ESTADO_GRUPO = "booleanChar";
    public static final String DATO_COLUMNA_ESTADO_ACTIVO = "A";
    public static final String DATO_COLUMNA_ESTADO_INACTIVO = "I";

    public static final String ARCHIVOS_EN_ALFRESCO = "S";

    public static final String TIPO_PARAMETRO_REITERANCIAS = "tipo_reiterancias";
    public static final String TIPO_PARAMETRO_TIPO_GRUPOS = "grupo";
    public static final String TIPO_UBICACION_DOCUMENTO = "Seguimiento";

    /**
     * Parametros *
     */
    public static final String PARAMETRO_TIPO_ENVIO = "tipo_envio";
    public static final String PARAMETRO_AMBITO_ENVIO = "ambito_envio";
    public static final String PARAMETRO_UNIDAD_PESO = "unidad_peso";
    public static final String PARAMETRO_EMPRESA_DESTINO = "empresa_destino";
    public static final String PARAMETRO_TIPO_COURIER = "tipo_courier";
    public static final String PARAMETRO_ESTADOS_CARGO = "estado";
    public static final String PARAMETRO_PRIORIDAD_EXPEDIENTE = "prioridadExpediente";
    public static final String PARAMETRO_TIPO_MES = "mes";
    public static final String PARAMETRO_TIPO_ANIO = "anio";
    public static final String PARAMETRO_CONFIDENCIALIDAD = "confidencialidad";
    public static final String PARAMETRO_ID_DOCUMENTOS_SOLICITUD = "id_td_docsolic";
    public static final String PARAMETRO_ID_PODERES = "id_td_poderes";
    public static final String PARAMETRO_MONEDA = "moneda";
    public static final String PARAMETRO_ID_PROCESO_CONTRATO = "id_proc_contrato";
    public static final String PARAMETRO_ID_PROCESO_ADENDA = "id_proc_adenda";
    public static final String PARAMETRO_ID_CONTRATO = "id_td_contrato";
    public static final String PARAMETRO_ID_ADENDA = "id_td_adenda";
    public static final String PARAMETRO_CONTRATO = "contrato";
    public static final String PARAMETRO_ADENDA = "adenda";
    public static final String PARAMETRO_ADENDA_AUTOMATICA = "adenda automatica";

    public static final String PARAMETRO_ID_PERFIL_USUARIOFINAL = "id_perfil_uf";
    public static final String PARAMETRO_NOMBRE_GRUPO_COMUNES = "nombre_comunes";
    public static final String PARAMETRO_HORA_ENVIO_ALARMA = "hora_alarma";
    public static final String PARAMETRO_ALARMA_DIAS_ACTIVACION = "alarma_dias";
    public static final String PARAMETRO_ALARMA_INTERVALO = "alarma_intervalo";
    public static final String PARAMETRO_ALARMA_TITULO = "alarma_titulo";
    public static final String PARAMETRO_ALARMA_MENSAJE = "alarma_mensaje";
    public static final String PARAMETRO_CODIGO_ROL_DEFAULT = "rol_sca_defecto";
    public static final String PARAMETRO_CARACTERES_ESPECIALES = "caracteres_especiales";
    public static final String VALOR_PRIORIDAD_NORMAL = "2";

    public static final String PARAMETRO_SEGUIMIENTO = "Seguimiento";
    public static final String PARAMETRO_SEGUIMIENTO_ADMINISTRACION_DE_CONTRATOS = "Administración de Contratos";
    public static final String PARAMETRO_SEGUIMIENTO_LEGAL = "Legal";
    public static final String PARAMETRO_SEGUIMIENTO_AREA_USUARIA = "Area Usuaria";
    public static final String PARAMETRO_SEGUIMIENTO_CONTRATISTA = "Contratista";

    /**
     * Notificaciones *
     */
    public static final String NOTIFICACION_DEVOLVER_MENSAJERIA = "devolverMensajeria";
    public static final String NOTIFICACION_TERMINAR_MENSAJERIA = "terminarMensajeria";
    public static final String NOTIFICACION_APROBACION_ENVIO = "aprobacionEnvio";
    public static final String NOTIFICACION_DEVOLUCION_ENVIO = "devolucionEnvio";

    public static final String NUMERACION_AUTOMATICA_NO_GENERADA = "ANG";
    public static final String NUMERACION_ADENDA_PREFIJO = "AD";
    public static final String MENSAJE_SIN_RESULTADOS = "No se encontr\u00f3 resultados";

    public static final String ROL_ADMINISTRADOR_CODIGO = "Administracion";

    public static final String ERROR_ID_PROCESO_NO_VALIDO = "ID del proceso no valido";
    public static final String ERROR_TIPO_DOCUMENTO_NO_VALIDO = "ID tipo documento no valido";
    public static final String ERROR_FORMATO_FECHA = "Formato de fecha no valido";
    public static final String ERROR_ID_CAMPO_NO_ASOCIADO_DOCUMENTO = "El campo no esta asociado al tipo de documento";
    public static final String ERROR_TIPO_CLIENTE_INCOMPLETO = "Debe especificarse un tipo de cliente";
    public static final String ERROR_TIPO_CLIENTE_ERRONEO = "El campo Tipo Cliente debe ser un numero";
    public static final String ERROR_TIPO_CLIENTE_NO_EXISTE = "El Tipo Cliente especificado no existe";
    public static final String ERROR_EXPEDIENTE_NO_CREADO = "No ha podido crearse el expediente en la base de datos";
    public static final String ERROR_PROCESO_NO_PERMITIDO = "No se puede iniciar un proceso de tipo intalio desde el servicio web";
    public static final String ERROR_USUARIO_RESPONSABLE_NO_ENCONTRADO = "Debe especificarse el ID del usuario responsable para procesar el expediente";
    public static final String ERROR_PROCESO_SIN_RESPONSABLE = "El proceso indicado no tiene un usuario responsable";
    public static final String ERROR_PROCESO_SIN_PARTICIPANTES = "El proceso indicado no tiene una lista de usuarios participantes";
    public static final String ERROR_USUARIO_NO_PERTENECE = "El usuario responsable no pertenece al proceso";
    public static final String ERROR_USUARIO_CREADOR_NO_PERTENECE = "El usuario creador del expediente no pertenece al proceso";
    public static final String ERROR_USUARIO_CREADOR_NO_EXISTE = "El usuario creador indicado no existe";
    public static final String ERROR_USUARIO_CREADOR_PROHIBIDO = "El usuario creador no cuenta con un rol que le permita iniciar el proceso";
    public static final String ERROR_PROCESO_SIN_WORKFLOW = "El proceso indicado no tiene un workflow asociado";
    public static final String ERROR_SIN_DOCUMENTOS = "El expediente debe contener al menos un documento";
    public static final String ERROR_SIN_ROL_ADMINISTRADOR = "Debe crearse el rol \"Administrador\" (Codigo: Administrador 22) y asignárselo a un usuario activo para poder crear el documento mediante el servicio web";
    public static final String ERROR_NUMERO_EXPEDIENTE_VACIO = "Debe especificarse un numero de expediente";
    public static final String ERROR_EXPEDIENTE_NO_EXISTE = "El numero de expediente escrito no se encuentra en el sistema";
    public static final String ERROR_DATOS_DOCUMENTO_INCOMPLETOS = "Los datos del documento se encuentran incompletos";
    public static final String ERROR_USUARIO_AUTOR_NO_EXISTE = "El usuario indicado como autor del documento no existe";

    public static final String OPERADOR_AND = "AND";
    public static final String OPERADOR_OR = "OR";

    /**
     * Alertas *
     */
    public static final int ALERTA_ROJA_PORCENTAJE = 80;
    public static final int ALERTA_AMARILLA_PORCENTAJE_MIN = 50;
    public static final int ALERTA_AMARILLA_PORCENTAJE_MAX = 79;
    public static final int ALERTA_VERDE_PORCENTAJE_MIN = 0;
    public static final int ALERTA_VERDE_PORCENTAJE_MAX = 49;

    public static final Character TIPO_PROCESO_ALERTA_PROCESO = 'P';
    public static final Character TIPO_PROCESO_ALERTA_ACTIVIDAD = 'A';

    public static final String BASE_DATOS_POSTGRE = "POSTGRE";
    public static final String BASE_DATOS_ORACLE = "ORACLE";

    public static final Character CONFIDENCIALIDAD_PROCESO = 'P';
    public static final Character CONFIDENCIALIDAD_EXPEDIENTE = 'E';

    public static final String DOCUMENTO_SIN_NUMERACION = "S/N";

    /**
     * Traza Copia *
     */
    public static final String TIPO_REFERENCIA_TRAZA = "T";

    /**
     * URLs para Filtro *
     */
    public static final String URL_LOGIN = "login";
    public static final String URL_LOGIN_EXTERNAL = "loginExternal";
    public static final String URL_OUT = "out";
    public static final String URL_RECURSOS_WEB = "resources";
    public static final String URL_WEB_SERVICE = "ServiciosSTD";

    public static final Character TIPO_DESTINATARIO_RESPONSABLE = 'R';
    public static final Character TIPO_DESTINATARIO_DESTINATARIO = 'D';
    public static final Character TIPO_DESTINATARIO_COPIA = 'C';

    public static final String TIPO_NUMERACION_MANUAL = "M";
    public static final String TIPO_NUMERACION_AUTOMATICA = "A";
    public static final String TIPO_NUMERACION_SIN_NUMERACION = "S";

    public static final Character TIPO_XOR_PREGUNTA = 'P';

    public static final Character TIPO_SIGUIENTE_PASO_EXISTENTE = 'E';
    public static final Character TIPO_SIGUIENTE_PASO_NUEVO = 'N';

    public static final String CODIGO_ROL_ABOGADO_RESPONSABLE = "abogadoResponsable";
    public static final String CODIGO_ROL_ABOGADO = "abogado";
    public static final String CODIGO_ROL_SOLICITANTE = "solicitante";
    public static final String CODIGO_ROL_VISADOR = "visador";
    public static final String TIPO_PROCESO_HC = "procesoHC";

    public static final String TIPO_UBICACION_OPERACION = "operacion";
    public static final String TIPO_UBICACION_PROYECTO = "proyecto";
    public static final String TIPO_UBICACION_OFICINA = "oficina";
    public static final String TIPO_UBICACION_EXPLORACION = "exploracion";

    public static final Character ESTADO_HC_EN_SOLICITUD = 'N';
    public static final Character ESTADO_HC_SOLICITUD_ENVIADA = 'R';
    public static final Character ESTADO_HC_EN_ELABORACION = 'E';
    public static final Character ESTADO_HC_ELABORADO = 'D';
    public static final Character ESTADO_HC_ENVIADO_VISADO = 'V';
    public static final Character ESTADO_HC_VISADO = 'S';
    public static final Character ESTADO_HC_ENVIADO_FIRMA = 'F';
    public static final Character ESTADO_HC_COMUNICACION = 'C';
    public static final Character ESTADO_HC_VIGENTE = 'T';
    public static final Character ESTADO_HC_VENCIDO = 'O';

    public static final String OBS_HC_ENVIO_SOLICITUD = "Solicitud enviada";
    public static final String OBS_HC_ENVIO_SOLICITUD_OBSERVADA = "Solicitud enviada luego de subsanar observaciones";
    public static final String OBS_HC_ARCHIVO_SOLICITUD_MANUAL = "Solicitud archivada - Registro manual";
    public static final String OBS_HC_APROBACION_SOLICITUD = "Solicitud aceptada";
    public static final String OBS_HC_DOCUMENTO_ELABORADO = "Documento elaborado";
    public static final String OBS_HC_ENVIADO_VISADO = "Enviado para visado";
    public static final String OBS_HC_VISADO = "Documento visado";
    public static final String OBS_HC_OBSERVAR_VISADO = "Documento observado";
    public static final String OBS_HC_ENVIADO_FIRMA = "Enviado a firma";
    public static final String OBS_HC_ENVIADO_FIRMA_ELECTRONICA = "Enviado a firma electrónica";
    public static final String OBS_HC_ANULACION_FIRMA_ELECTRONICA = "Anulación de Firma Electrónica";
    public static final String OBS_HC_ENTREGA_DOCUMENTO = "Entrega de documento para firma";
    public static final String OBS_HC_DOCUMENTO_FIRMADO = "Documento firmado";
    public static final String OBS_HC_CAMBIO_ABOGADO_RESPONSABLE = "Cambio de Abogado responsable";
    public static final String OBS_HC_CANCELAR_VISADO = "Visado Cancelado";
    public static final String OBS_HC_REAPERTURA = "Documento reabierto";

    public static final String TIPO_CLIENTE_JURIDICA = "juridica";
    public static final String TIPO_CLIENTE_NATURAL = "natural";
    public static final String TIPO_CLIENTE_EXTRANJERO = "extranjero";

    public static final String TIPO_CLIENTE_ACTIVO = "S";
    public static final String TIPO_CLIENTE_INACTIVO = "N";

    public static final String TIPO_CONTRAPARTE = "contraparte";
    public static final String TIPO_REPRESENTANTE = "representante";

    public static final String SUB_CODIGO_MIS_PENDIENTES = "MP";
    public static final String SUB_CODIGO_MIS_SOLICITUDES = "MS";

    public static final String TIPO_DOCUMENTO_ADENDA = "Adenda";
    public static final Character TIPO_DOCUMENTO_CONTRATO_ADENDA = 'A';
    public static final String TIPO_DOCUMENTO_CONTRATO = "Contrato";
    public static final Character TIPO_DOCUMENTO_CONTRATO_CODIGO = 'C';
    public static final String TIPO_DOCUMENTO_LEGAL = "DocumentoLegal";
    public static final String TIPO_DOCUMENTO_BORRADORES = "Borradores";
    public static final String TIPO_DOCUMENTO_VERSION_FINAL = "Version Final";

    public static final String MAP_KEY_DESTINATARIOS = "destinatarios";
    public static final String MAP_KEY_TRAZA = "traza";

    public static final String URLPARTS_SCHEME = "URL_SCHEME";
    public static final String URLPARTS_SERVERNAME = "URL_SERVERNAME";
    public static final String URLPARTS_SERVERPORT = "URL_SERVERPORT";
    public static final String URLPARTS_CONTEXTPATH = "URL_CONTEXTPATH";

    public static final Integer CAT_SUNAT_TD_RUC = 6;
    public static final Integer CAT_SUNAT_TD_DNI = 1;

    public static final String KEY_VO_PAISES = "voPaises";
    public static final String KEY_VO_COMPANIAS = "voCompanias";
    public static final String KEY_VO_AREAS = "voAreas";
    public static final String KEY_VO_UBICACIONES = "voUbicaciones";

    public static final String MONEDA_SOLES = "soles";
    public static final String MONEDA_DOLARES = "dolares";
    public static final String MONEDA_PORCENTAJE = "porcentaje";

    public static final String SESION_TIMEOUT = "SESION_TIMEOUT";
    public static final String URL_ENVIAR_VB = "enviarVisado";
    public static final String TIPO_PLAZO_GENERAL = "PLAZO_VALOR-";

    /**
     * Adenda Automática *
     */
    public static final Character CODIGO_TIPO_CONTRATO = 'A';
    public static final Character ESTADO_PLANTILLA_HABILITADA = 'S';
    public static final String ESTADO_CONFIGURACION_HABILITADO = "S";

    public static final int SECUENCIA_ADENDA_UNO = 1;
    public static final int SECUENCIA_ADENDA_DOS = 2;
    public static final int SECUENCIA_ADENDA_TRES = 3;

    /**
     * Proceso *
     */
    public static final int PROCESO_CONTRATO = 3;
    public static final int PROCESO_ADENDA = 4;
    public static final int PROCESO_ADENDA_AUTOMATICA = 5;

    /**
     * Tipos de documentos *
     */
    public static final String TIPO_DOCUMENTO_ADENDA_CONTRACTUAL = "Adenda Cesión de Posición Contractual";
    public static final String TIPO_DOCUMENTO_ADENDA_AMPLICACION_PLAZO = "Adenda Ampliación de Plazo";
    public static final String TIPO_DOCUMENTO_ADENDA_MODIFICACION_TARIFAS = "Adenda Modificación de Tarifas";

    public static final int TIPO_DOCUMENTO_COD_ADENDA_RESOLUCION_UNILATERAL = 56;

    /**
     * Recurso por perfil*
     */
    public static final Character RECURSO_PARA_ESTADO = 'A';

    /*
        Proceso
     */
    public static final String NOMBRE_PROCESO_ADENDA_AUTOMATICA = "Adenda Automática";

    /**
     * Firma Electrónica *
     */
    public static final String FIRMA_ELECTRONICA_BTN_URL = "firmaElectronica";
    public static final String FIRMA_ELECTRONICA_LANG = "es";
    public static final String FLAG_EXPEDIENTE_FIRMA_ELECTRONICA = "S";
    public static final String FLAG_EXPEDIENTE_SIN_FIRMA_ELECTRONICA = "N";
    public static final String SIGNERS_USER_GROUP = "signers";
    public static final String DNI_SIGNERS_USER_GROUP = "dnisigners";
    public static final String CE_SIGNERS_USER_GROUP = "cesigners";
    public static final String NOTIFIERS_USER_GROUP = "notifiers";
    public static final String ESTADO_REPRESENTANTE_COMPANHIA_HABILITADO = "S";
    public static final String ESTADO_ACCESO_REPRESENTANTE_HABILITADO = "S";
    public static final String PARAMETRO_TIPO_FIRMA_ELECTRONICA = "firma_electronica";
    public static final Integer ID_PARAMETRO_FIRMA_ELECTRONICA_FREQUENCY = 101;
    public static final Integer ID_PARAMETRO_FIRMA_ELECTRONICA_MAX_ATTEMPTS = 102;


    public static final String PATH_IMAGES= "/resources/images";
    public static final String PATH_ISOTIPO_HOC= "/hoc_isotipo.png";
    public static final String CID_ISOTIPO_HOC= "hoc_isotipo";

    /**
     * Parametro Tipo de grupo *
     */
    public static final String TIPO_GRUPO_AVISO_SOLICITUD = "Aviso Solicitud";

    /**
     * Parametro Tipo de documento *
     */
    public static final String ID_TIPO_DOCUMENTO_CONTRATO = "7";
    public static final String ID_TIPO_DOCUMENTO_ADENDA = "8";
    public static final String ID_TIPO_DOCUMENTO_SOLICITUD = "4";
    public static final String ID_TIPO_DOCUMENTO_BORRADORES = "12";


    /**
     * Parametro Tipo de notificacion *
     */
    public static final String TIPO_NOTIFICACION_SOLICITUD_ACEPTADA = "HC_Solicitud_Aceptada";
    public static final String TIPO_NOTIFICACION_ADJUNTAR_BORRADOR = "HC_Adjuntar_Borrador";
    public static final String TIPO_NOTIFICACION_VISADO_APROBADO = "HC_Visado_Aprobado";
    public static final String TIPO_NOTIFICACION_ENVIO_FIRMO = "HC_Envio_Firma";

    /**
     * Parametros tipo contrato cfg
     */
    public static final String ESTADO_ACTIVO_UNIR_DOC = "S";

    public static final String TIPO_PARAMETRO_FIRMA_ELECTRONICA = "tipo_firmaElectronica";
    public static final String TIPO_PARAMETRO_IDIOMA_FIRMA_ELECTRONICA = "idioma_firmaElectronica";

    public static final String TIPO_FIRMA_ELECTRONICA_VIDEO_FIRMA = "VF";
    public static final String TIPO_FIRMA_ELECTRONICA_FIRMA_DIBUJADA = "FD";

    public static final String TIPO_CLIENTE_FIRMA_ELECTRONICA_DNI = "DNI";
    public static final String TIPO_CLIENTE_FIRMA_ELECTRONICA_CE = "CE";
    public static final String TIPO_CLIENTE_FIRMA_ELECTRONICA_DNI_CE = "DNI_CE";

    /**
     * Seguridad
     */
    public static final String ESTADO_SEGURIDAD_ELIMINADO = "S";
    public static final String ESTADO_SEGURIDAD_ACTIVO = "N";
    public static final String TIPO_SEGURIDAD = "tipo_seguridad";
    public static final String TIPO_SEGURIDAD_GRUPO = "GRU";
    public static final String TIPO_SEGURIDAD_USUARIO = "USU";
    public static final String EXPEDIENTE_ES_CONFIDENCIAL = "S";
    public static final String EXPEDIENTE_NO_ES_CONFIDENCIAL = "N";
    public static final String TIPO_PARAMETRO_GRUPO = "grupo";
    public static final String TIPO_PARAMETRO_ROL_SEGURIDAD = "rol_seguridad";
    public static final String VALOR_PARAMETRO_GRUPO_SEGURIDAD = "SEG";
    public static final String SEGURIDAD_NOMBRE_PARAMETRO = "Seguridad";
    public static final String PATRON_FECHA_APLICACION = "dd/MM/yyyy";
    public static final String NOMBRE_PUESTO_EXTERNO = "EXTERNO";

    public static final Integer ID_RECURSO_USUARIO_POR_TRAZA = 230;


    public static final Integer ID_TIPO_MOVIMIENTO_TX_INVENTARIO_INICIAL = 138;

    public static final Integer ID_TIPO_MOVIMIENTO_TX_INVENTARIO_COMPRA = 139;

    public static final Integer ID_TIPO_MOVIMIENTO_TX_INVENTARIO_SALIDA = 140;

    public static final Integer ID_TIPO_MOVIMIENTO_TX_INVENTARIO_ANULACION = 141;

    public static final Integer ID_TIPO_REGISTRO_TX_INGRESO = 142;

    public static final Integer ID_TIPO_REGISTRO_TX_SALIDA = 143;
    public static final Integer ID_PARAMETRO_URL_LEGAL_AL_DIA = 144;
    public enum Destinatario {
        DEVOLVER,
        NO_ENVIAR
    }

    public static final Integer MOSTRAR_CARGO_EXP = 1;
    public static final Integer NUEVA_SOLICITUD = 2;
    public static final Integer DETALLE_EXPEDIENTE = 3;
    public static final Integer EXPEDIENTE_CONFIDENCIAL_INVALIDO = 0;

    public static final String NOMBRE_COMPANY = "Compañia Minera Ares S.A.C.";
    public static final String PARAMETRO_ID_BORRADOR = "12";
    public static final String PARAMETRO_ID_VERSIONFINAL = "13";

}

package com.hochschild.speed.back.util;

import com.hochschild.speed.back.model.response.ResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.*;

public class AppUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppUtil.class);

    public static ResponseEntity<ResponseModel> convertToResponseEntity(Map<String, Object> result) {
        ResponseModel response=new ResponseModel();
        if (result.get("resultado").equals("error")) {
            response.setHttpSatus(HttpStatus.CONFLICT);
            response.setMessage(result.get("mensaje").toString());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } else {
            response.setHttpSatus(HttpStatus.OK);
            response.setMessage(result.get("mensaje").toString());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    public static Float arreglarMontoValor(String valor) {
        try{
            String montoString = valor;
            montoString = montoString.replace(",", "");
            montoString = montoString.replace("'", "");
            int v = montoString.lastIndexOf('.');
            StringBuilder xx;
            if (v != -1) {//decimal no encontrado
                xx = new StringBuilder(montoString);
                xx.setCharAt(v, '$');
                montoString = xx.toString().replace(".", "");
                montoString = montoString.replace("$", ".");
            }
            return Float.parseFloat(montoString);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return null;
        }
    }

    public static boolean checkNullOrEmpty(String cadena) {
        return cadena == null || cadena.trim().isEmpty();
    }

    public static String quitarAcentos(String cadena) {
        cadena = cadena.replace("\u00f1", "n");
        cadena = cadena.replace(",", "");
        return Normalizer.normalize(cadena, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    
    public static String quitarCaracteres(String cadena, ArrayList<String> listaCaracteres) {
        for (String caracter : listaCaracteres) {
            cadena = cadena.replaceAll("\\" + caracter, "");
        }
        return cadena;
    }

    public static String convertirAcentosUTF(String cadena) {
        if (cadena != null) {
            cadena = cadena.replace("&Aacute;", "\u00C1");
            cadena = cadena.replace("&Eacute;", "\u00C9");
            cadena = cadena.replace("&Iacute;", "\u00CD");
            cadena = cadena.replace("&Oacute;", "\u00D3");
            cadena = cadena.replace("&Oacute;", "\u00DA");
            cadena = cadena.replace("&Ntilde;", "\u00D1");
            cadena = cadena.replace("&ordm;", "\u00BA");

            cadena = cadena.replace("&aacute;", "\u00E1");
            cadena = cadena.replace("&eacute;", "\u00E9");
            cadena = cadena.replace("&iacute;", "\u00ED");
            cadena = cadena.replace("&oacute;", "\u00F3");
            cadena = cadena.replace("&uacute;", "\u00FA");
            cadena = cadena.replace("&ntilde;", "\u00F1");
        }
        return cadena;
    }


    public static List<Map<String, Object>> getEstados() {

        List<Map<String, Object>> estados = new ArrayList<>();

        Map<String, Object> e0 = new HashMap<>();
        e0.put("nombre", "Todos");
        e0.put("valor", Constantes.ESTADO_TODOS);
        estados.add(e0);

        Map<String, Object> e1 = new HashMap<>();
        e1.put("nombre", "Activo");
        e1.put("valor", Constantes.ESTADO_ACTIVO);
        estados.add(e1);

        Map<String, Object> e2 = new HashMap<>();
        e2.put("nombre", "Archivado");
        e2.put("valor", Constantes.ESTADO_ARCHIVADO);
        estados.add(e2);

        Map<String, Object> e8 = new HashMap<>();
        e8.put("nombre", "Busqueda");
        e8.put("valor", Constantes.ESTADO_BUSQUEDA);
        estados.add(e8);

        Map<String, Object> e6 = new HashMap<>();
        e6.put("nombre", "Enviado");
        e6.put("valor", Constantes.ESTADO_ENVIADO);
        estados.add(e6);

        Map<String, Object> e4 = new HashMap<>();
        e4.put("nombre", "Guardado");
        e4.put("valor", Constantes.ESTADO_GUARDADO);
        estados.add(e4);

        Map<String, Object> e3 = new HashMap<>();
        e3.put("nombre", "Inactivo");
        e3.put("valor", Constantes.ESTADO_INACTIVO);
        estados.add(e3);

        Map<String, Object> e7 = new HashMap<>();
        e7.put("nombre", "Proceso");
        e7.put("valor", Constantes.ESTADO_PROCESO);
        estados.add(e7);

        Map<String, Object> e5 = new HashMap<>();
        e5.put("nombre", "Registrado");
        e5.put("valor", Constantes.ESTADO_REGISTRADO);
        estados.add(e5);

        Map<String, Object> e9 = new HashMap<>();
        e9.put("nombre", "Pausado");
        e9.put("valor", Constantes.ESTADO_PAUSADO);
        estados.add(e9);

        Map<String, Object> e10 = new HashMap<>();
        e10.put("nombre", "Pendiente");
        e10.put("valor", Constantes.ESTADO_PENDIENTE);
        estados.add(e10);

        Map<String, Object> e11 = new HashMap<>();
        e11.put("nombre", "Atendido");
        e11.put("valor", Constantes.ESTADO_ATENDIDO);
        estados.add(e11);

        Map<String, Object> e12 = new HashMap<>();
        e12.put("nombre", "Por Enviar");
        e12.put("valor", Constantes.ESTADO_POR_ENVIAR);
        estados.add(e12);

        Map<String, Object> e13 = new HashMap<>();
        e13.put("nombre", "Recibido");
        e13.put("valor", Constantes.ESTADO_RECIBIDO);
        estados.add(e13);

        Map<String, Object> e14 = new HashMap<>();
        e14.put("nombre", "HC: Comunicacion a Interesados");
        e14.put("valor", Constantes.ESTADO_HC_COMUNICACION);
        estados.add(e14);

        Map<String, Object> e15 = new HashMap<>();
        e15.put("nombre", "HC: Doc. Elaborado");
        e15.put("valor", Constantes.ESTADO_HC_ELABORADO);
        estados.add(e15);

        Map<String, Object> e16 = new HashMap<>();
        e16.put("nombre", "HC: En Elaboracion");
        e16.put("valor", Constantes.ESTADO_HC_EN_ELABORACION);
        estados.add(e16);

        Map<String, Object> e17 = new HashMap<>();
        e17.put("nombre", "HC: En Solicitud");
        e17.put("valor", Constantes.ESTADO_HC_EN_SOLICITUD);
        estados.add(e17);

        Map<String, Object> e18 = new HashMap<>();
        e18.put("nombre", "HC: Enviado a Firma");
        e18.put("valor", Constantes.ESTADO_HC_ENVIADO_FIRMA);
        estados.add(e18);

        Map<String, Object> e19 = new HashMap<>();
        e19.put("nombre", "HC: Enviado a Visado");
        e19.put("valor", Constantes.ESTADO_HC_ENVIADO_VISADO);
        estados.add(e19);

        Map<String, Object> e20 = new HashMap<>();
        e20.put("nombre", "HC: Solicitud Enviada");
        e20.put("valor", Constantes.ESTADO_HC_SOLICITUD_ENVIADA);
        estados.add(e20);

        Map<String, Object> e21 = new HashMap<>();
        e21.put("nombre", "HC: Vencido");
        e21.put("valor", Constantes.ESTADO_HC_VENCIDO);
        estados.add(e21);

        Map<String, Object> e22 = new HashMap<>();
        e22.put("nombre", "HC: Vigente");
        e22.put("valor", Constantes.ESTADO_HC_VIGENTE);
        estados.add(e22);

        Map<String, Object> e23 = new HashMap<>();
        e23.put("nombre", "HC: Visado");
        e23.put("valor", Constantes.ESTADO_HC_VISADO);
        estados.add(e23);

        return estados;
    }

    public static List<Map<String, Object>> getEstadosDL() {
        List<Map<String, Object>> estados = new ArrayList<>();

        Map<String, Object> e1 = new HashMap<>();
        e1.put("nombre", "Comunicacion a Interesados");
        e1.put("valor", Constantes.ESTADO_HC_COMUNICACION);
        estados.add(e1);

        Map<String, Object> e2 = new HashMap<>();
        e2.put("nombre", "Doc. Elaborado");
        e2.put("valor", Constantes.ESTADO_HC_ELABORADO);
        estados.add(e2);

        Map<String, Object> e3 = new HashMap<>();
        e3.put("nombre", "En Elaboracion");
        e3.put("valor", Constantes.ESTADO_HC_EN_ELABORACION);
        estados.add(e3);

        Map<String, Object> e4 = new HashMap<>();
        e4.put("nombre", "En Solicitud");
        e4.put("valor", Constantes.ESTADO_HC_EN_SOLICITUD);
        estados.add(e4);

        Map<String, Object> e5 = new HashMap<>();
        e5.put("nombre", "Enviado a Firma");
        e5.put("valor", Constantes.ESTADO_HC_ENVIADO_FIRMA);
        estados.add(e5);

        Map<String, Object> e6 = new HashMap<>();
        e6.put("nombre", "Enviado a Visado");
        e6.put("valor", Constantes.ESTADO_HC_ENVIADO_VISADO);
        estados.add(e6);

        Map<String, Object> e7 = new HashMap<>();
        e7.put("nombre", "Solicitud Enviada");
        e7.put("valor", Constantes.ESTADO_HC_SOLICITUD_ENVIADA);
        estados.add(e7);

        Map<String, Object> e8 = new HashMap<>();
        e8.put("nombre", "Vencido");
        e8.put("valor", Constantes.ESTADO_HC_VENCIDO);
        estados.add(e8);

        Map<String, Object> e9 = new HashMap<>();
        e9.put("nombre", "Vigente");
        e9.put("valor", Constantes.ESTADO_HC_VIGENTE);
        estados.add(e9);

        Map<String, Object> e10 = new HashMap<>();
        e10.put("nombre", "Visado");
        e10.put("valor", Constantes.ESTADO_HC_VISADO);
        estados.add(e10);

        return estados;
    }

    public static String getNombreEstadoDL(Character codigoEstado) {
        List<Map<String, Object>> estados = getEstadosDL();

        for (Map<String, Object> mapEstado : estados) {
            if (mapEstado.get("valor").equals(codigoEstado)) {
                return (String) mapEstado.get("nombre");
            }
        }
        return "";
    }

    public static String getNombreEstado(Character codigoEstado) {
        List<Map<String, Object>> estados = getEstados();

        for (Map<String, Object> mapEstado : estados) {
            if (mapEstado.get("valor").equals(codigoEstado)) {
                return (String) mapEstado.get("nombre");
            }
        }
        return "";
    }
    public static String getNombreEstadoVisadores(Character codigoEstado) {
        String value = "";
        switch (codigoEstado) {
            case 'A': value = "Aprobado";
            break;
            case 'O': value = "Observado";
            break;
            case 'P': value = "Pendiente";
            break;
            case 'E': value = "Enviado";
        }
        return value;
    }
    public static String procesarMensajeError(int error) {
        // FIXME retornar de algun archivo de configuracion
        switch (error) {
            case Constantes.ERROR_DOCUMENTO_INEXISTENTE:
                return "El identificador de documento ingresado es incorrecto";
            case Constantes.ERROR_USUARIO_INEXISTENTE:
                return "El nombre de usuario ingresado es incorrecto";
            case Constantes.ERROR_LEER_ARCHIVO:
                return "No se pudo leer el archivo";
            case Constantes.ERROR_ALFRESCO_SUBIR:
                return "Se produjo un error al subir el archivo a Alfresco";
            case Constantes.ERROR_ALFRESCO_INACTIVO:
                return "El Alfresco se encuentra desactivado, no se pueden adjuntar archivos";
        }
        return "";
    }

    public static Map<String, String> getUrlParts() {
        Map<String, String> urlParts = new HashMap<>();
        ResourceBundle bundle = ResourceBundle.getBundle("mail", new EncodingControl("UTF-8"));
        urlParts.put(Constantes.URLPARTS_SCHEME, bundle.getString("mail.url.scheme"));
        urlParts.put(Constantes.URLPARTS_SERVERNAME, bundle.getString("mail.url.servername"));
        urlParts.put(Constantes.URLPARTS_SERVERPORT, bundle.getString("mail.url.port"));
        urlParts.put(Constantes.URLPARTS_CONTEXTPATH, bundle.getString("mail.url.contextpath"));
        return urlParts;
    }

    public static Integer getIDTipoDocumentoClienteSunat(String tipoCliente) {
        if (tipoCliente != null) {
            if (tipoCliente.equals(Constantes.TIPO_CLIENTE_JURIDICA)) {
                return Constantes.CAT_SUNAT_TD_RUC;
            } else if (tipoCliente.equals(Constantes.TIPO_CLIENTE_NATURAL)) {
                return Constantes.CAT_SUNAT_TD_DNI;
            }
        }
        return 0;
    }
}
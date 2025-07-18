package com.hochschild.speed.back.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class FilesUtil {

    private static final Map<String, String> FILE_EXTENSION_MAP;

    static {
        FILE_EXTENSION_MAP = new HashMap<>();
        // MS Office
        FILE_EXTENSION_MAP.put("doc", "application/msword");
        FILE_EXTENSION_MAP.put("dot", "application/msword");
        FILE_EXTENSION_MAP.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        FILE_EXTENSION_MAP.put("dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
        FILE_EXTENSION_MAP.put("docm", "application/vnd.ms-word.document.macroEnabled.12");
        FILE_EXTENSION_MAP.put("dotm", "application/vnd.ms-word.template.macroEnabled.12");
        FILE_EXTENSION_MAP.put("xls", "application/vnd.ms-excel");
        FILE_EXTENSION_MAP.put("xlt", "application/vnd.ms-excel");
        FILE_EXTENSION_MAP.put("xla", "application/vnd.ms-excel");
        FILE_EXTENSION_MAP.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        FILE_EXTENSION_MAP.put("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
        FILE_EXTENSION_MAP.put("xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12");
        FILE_EXTENSION_MAP.put("xltm", "application/vnd.ms-excel.template.macroEnabled.12");
        FILE_EXTENSION_MAP.put("xlam", "application/vnd.ms-excel.addin.macroEnabled.12");
        FILE_EXTENSION_MAP.put("xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
        FILE_EXTENSION_MAP.put("ppt", "application/vnd.ms-powerpoint");
        FILE_EXTENSION_MAP.put("pot", "application/vnd.ms-powerpoint");
        FILE_EXTENSION_MAP.put("pps", "application/vnd.ms-powerpoint");
        FILE_EXTENSION_MAP.put("ppa", "application/vnd.ms-powerpoint");
        FILE_EXTENSION_MAP.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        FILE_EXTENSION_MAP.put("potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
        FILE_EXTENSION_MAP.put("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
        FILE_EXTENSION_MAP.put("ppam", "application/vnd.ms-powerpoint.addin.macroEnabled.12");
        FILE_EXTENSION_MAP.put("pptm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
        FILE_EXTENSION_MAP.put("potm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
        FILE_EXTENSION_MAP.put("ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
        // Open Office
        FILE_EXTENSION_MAP.put("odt", "application/vnd.oasis.opendocument.text");
        FILE_EXTENSION_MAP.put("ott", "application/vnd.oasis.opendocument.text-template");
        FILE_EXTENSION_MAP.put("oth", "application/vnd.oasis.opendocument.text-web");
        FILE_EXTENSION_MAP.put("odm", "application/vnd.oasis.opendocument.text-master");
        FILE_EXTENSION_MAP.put("odg", "application/vnd.oasis.opendocument.graphics");
        FILE_EXTENSION_MAP.put("otg", "application/vnd.oasis.opendocument.graphics-template");
        FILE_EXTENSION_MAP.put("odp", "application/vnd.oasis.opendocument.presentation");
        FILE_EXTENSION_MAP.put("otp", "application/vnd.oasis.opendocument.presentation-template");
        FILE_EXTENSION_MAP.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        FILE_EXTENSION_MAP.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template");
        FILE_EXTENSION_MAP.put("odc", "application/vnd.oasis.opendocument.chart");
        FILE_EXTENSION_MAP.put("odf", "application/vnd.oasis.opendocument.formula");
        FILE_EXTENSION_MAP.put("odb", "application/vnd.oasis.opendocument.database");
        FILE_EXTENSION_MAP.put("odi", "application/vnd.oasis.opendocument.image");
        FILE_EXTENSION_MAP.put("oxt", "application/vnd.openofficeorg.extension");
        // Other
        FILE_EXTENSION_MAP.put("txt", "text/plain");
        FILE_EXTENSION_MAP.put("rtf", "application/rtf");
        FILE_EXTENSION_MAP.put("pdf", "application/pdf");
    }

    public static String getMimeType(String fileName) {
        // 1. first use java's built-in utils
        FileNameMap mimeTypes = URLConnection.getFileNameMap();
        String contentType = mimeTypes.getContentTypeFor(fileName);

        // 2. nothing found -> lookup our in extension map to find types like ".doc" or ".docx"
        if (contentType == null) {
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());;
            contentType = FILE_EXTENSION_MAP.get(extension);
        }
        return contentType;
    }

    public static byte[] convertArrayByte(InputStream is) throws IOException {
        //int size = 1024; 1Mb
        int size = 30720; //30Mb
        int len = 0;
        byte[] data = new byte[size];

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = is.read(data, 0, size)) != -1) {
            bos.write(data, 0, len);
        }

        return bos.toByteArray();
    }

}

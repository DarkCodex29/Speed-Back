package com.hochschild.speed.back.ws.remote.doc_api.client;

import java.io.File;

public interface DocClient {

    byte[] descargarDocumento(File file);
    
    byte[] descargarDocumentoAndMerge(File fileA, File fileB);
}

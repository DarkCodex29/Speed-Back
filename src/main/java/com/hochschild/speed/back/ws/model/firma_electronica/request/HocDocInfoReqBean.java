package com.hochschild.speed.back.ws.model.firma_electronica.request;

import lombok.Data;
import java.io.Serializable;

@Data
public class HocDocInfoReqBean implements Serializable {

    private Integer idDocumentoLegal;

    private Integer idResponsable;

    private String docNumber;

    private String docAbstract;

    private String docType;

    private String chiefLawyer;
}

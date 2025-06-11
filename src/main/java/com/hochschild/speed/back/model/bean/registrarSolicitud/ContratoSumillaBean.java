package com.hochschild.speed.back.model.bean.registrarSolicitud;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.xpath.operations.Bool;

@AllArgsConstructor
@NoArgsConstructor
public @Data  class ContratoSumillaBean {
    private Integer idDocumentoLegal;
    private String sumilla;

    /*public ContratoSumillaBean (Integer idDocumentoLegal,String sumilla){
        this.idDocumentoLegal = idDocumentoLegal;
        this.sumilla =  sumilla + '-';

    }*/
}

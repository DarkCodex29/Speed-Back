package com.hochschild.speed.back.model.reporte;

import lombok.Data;

public @Data class DatosR4 {
    private Character status;
    private String strStatus;
    private String enero;
    private String febrero;
    private String marzo;
    private String abril;
    private String mayo;
    private String junio;
    private String julio;

    private String agosto;
    private String setiembre;


    private String octubre;
    private String noviembre;
    private String diciembre;


    public DatosR4(Character status, String enero, String febrero, String marzo, String abril, String mayo, String junio, String julio, String agosto, String setiembre, String octubre, String noviembre, String diciembre) {
        this.status = status;
        this.enero = enero;
        this.febrero = febrero;
        this.marzo = marzo;
        this.abril = abril;
        this.mayo = mayo;
        this.junio = junio;
        this.julio = julio;
        this.agosto = agosto;
        this.setiembre = setiembre;
        this.octubre = octubre;
        this.noviembre = noviembre;
        this.diciembre = diciembre;
    }

}

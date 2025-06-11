package com.hochschild.speed.back.model.domain.speed.utils;

import java.util.List;

/**
 *
 * @author Fernando Salaz
 * 
 */
public class SolicitudesJsonList {

    private List<SolicitudesJson> listadoSolicitudes;

    public SolicitudesJsonList(List<SolicitudesJson> listadoSolicitudes) {
        this.listadoSolicitudes = listadoSolicitudes;
    }

    /**
     * @return the listadoSolicitudes
     */
    public List<SolicitudesJson> getListadoSolicitudes() {
        return listadoSolicitudes;
    }

    /**
     * @param listadoSolicitudes the listadoSolicitudes to set
     */
    public void setListadoSolicitudes(List<SolicitudesJson> listadoSolicitudes) {
        this.listadoSolicitudes = listadoSolicitudes;
    }
}
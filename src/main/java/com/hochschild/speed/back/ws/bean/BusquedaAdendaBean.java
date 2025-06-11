package com.hochschild.speed.back.ws.bean;

import com.hochschild.speed.back.model.domain.speed.HcContrato;
import com.hochschild.speed.back.model.domain.speed.HcDocumentoLegal;
import com.hochschild.speed.back.model.domain.speed.HcTipoContrato;
import com.hochschild.speed.back.util.DateUtil;

import java.util.Date;

public class BusquedaAdendaBean {

    private Integer id;
    private HcContrato contrato;
    private HcTipoContrato hcTipoContrato;
    private Date inicioVigencia;
    private Boolean modifica_fin;
    private Date nuevaFechaFin;
    private Boolean indefinido;
    private String descripcion;
    private Integer secuencia;
    private HcDocumentoLegal documentoLegal;

    public BusquedaAdendaBean() {
    }

    public BusquedaAdendaBean(Integer id, HcContrato contrato, HcTipoContrato hcTipoContrato, Date inicioVigencia, Boolean modifica_fin, Date nuevaFechaFin, Boolean indefinido, String descripcion, Integer secuencia, HcDocumentoLegal documentoLegal) {
        this.id = id;
        this.contrato = contrato;
        this.hcTipoContrato = hcTipoContrato;
        this.inicioVigencia = inicioVigencia;
        this.modifica_fin = modifica_fin;
        this.nuevaFechaFin = nuevaFechaFin;
        this.indefinido = indefinido;
        this.descripcion = descripcion;
        this.secuencia = secuencia;
        this.documentoLegal = documentoLegal;
    }

    public BusquedaAdendaBean(Integer id, Date inicioVigencia, Boolean modifica_fin, Date nuevaFechaFin, Boolean indefinido, String descripcion, Integer secuencia) {
        this.id = id;
        this.inicioVigencia = inicioVigencia;
        this.modifica_fin = modifica_fin;
        this.nuevaFechaFin = nuevaFechaFin;
        this.indefinido = indefinido;
        this.descripcion = descripcion;
        this.secuencia = secuencia;
    }

    /*
    public final String getStrNuevaFecha() {
        return convertDateToString(this.inicioVigencia, "dd/MM/yyyy");
    }

    public final String getStrNuevaFechaFin() {
        return convertDateToString(this.nuevaFechaFin, "dd/MM/yyyy");
    }

    public static String convertDateToString(Date date, String format) {
        String strDate = "";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            strDate = formatter.format(date);
        } catch (Exception e) {
            strDate = "";
        }
        return strDate;
    }*/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HcContrato getContrato() {
        return contrato;
    }

    public void setContrato(HcContrato contrato) {
        this.contrato = contrato;
    }

    public HcTipoContrato getHcTipoContrato() {
        return hcTipoContrato;
    }

    public void setHcTipoContrato(HcTipoContrato hcTipoContrato) {
        this.hcTipoContrato = hcTipoContrato;
    }

    public Date getInicioVigencia() {
        inicioVigencia = DateUtil.minimizarFecha(inicioVigencia);
        return inicioVigencia;
    }

    public void setInicioVigencia(Date inicioVigencia) {
        this.inicioVigencia = DateUtil.minimizarFecha(inicioVigencia);
    }

    public Boolean getModifica_fin() {
        return modifica_fin;
    }

    public void setModifica_fin(Boolean modifica_fin) {
        this.modifica_fin = modifica_fin;
    }

    public Date getNuevaFechaFin() {
        nuevaFechaFin = DateUtil.maximizarFecha(nuevaFechaFin);
        return nuevaFechaFin;
    }

    public void setNuevaFechaFin(Date nuevaFechaFin) {
        this.nuevaFechaFin = DateUtil.maximizarFecha(nuevaFechaFin);;
    }

    public Boolean getIndefinido() {
        return indefinido;
    }

    public void setIndefinido(Boolean indefinido) {
        this.indefinido = indefinido;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    public HcDocumentoLegal getDocumentoLegal() {
        return documentoLegal;
    }

    public void setDocumentoLegal(HcDocumentoLegal documentoLegal) {
        this.documentoLegal = documentoLegal;
    }

}

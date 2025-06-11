package com.hochschild.speed.back.model.bean.bandejaEntrada;

import com.hochschild.speed.back.model.domain.speed.*;
import lombok.Data;
import java.util.List;

public @Data class DatosContratoBean {

    private Integer idTraza;
    private Usuario usuario;
    private HcDocumentoLegal documentoLegal;
    private List<Documento> documentos;
    private Integer idPais;
    private Integer idCompania;
    private List<HcPais> paises;
    private List<HcCompania> companias;
    private List<HcArea> areas;
    private List<HcUbicacion> ubicaciones;
    private List<Cliente> representantes;
    private List<HcUbicacion> ubicacionOperacion;
    private List<HcUbicacion> ubicacionProyecto;
    private List<HcUbicacion> ubicacionOficina;
    private List<HcTipoContrato> listaTipoContrato;
    private List<Parametro> listaMonedas;
    private List<HcPenalidadPorDocumentoLegal> penalidades;
    private Boolean botoneraActiva;
}

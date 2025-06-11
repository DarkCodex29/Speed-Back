package com.hochschild.speed.back.model.bean;

import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.domain.speed.Expediente;
import com.hochschild.speed.back.model.domain.speed.HcDocumentoLegal;
import com.hochschild.speed.back.model.domain.speed.HcUbicacion;
import com.hochschild.speed.back.model.domain.speed.HcTipoContratoConfiguracion;
import com.hochschild.speed.back.model.domain.speed.HcAdenda;
import com.hochschild.speed.back.model.domain.speed.Cliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public @Data class RevisarAdendaBean {

    private Integer idTraza;
    private Usuario usuario;
    private Expediente expediente;
    private HcDocumentoLegal hcDocumentoLegal;
    private Integer idCompania;
    private List<Cliente> representates;
    private List<HcUbicacion> ubicacionOperacion;
    private List<HcUbicacion> ubicacionProyecto;
    private List<HcUbicacion> ubicacionOficina;
    private List<HcUbicacion> ubicacionesDocumento;
    private List<HcTipoContratoConfiguracion> listaProcesos;
    private HcAdenda tipoContratoAdenda;
}
package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.*;


@Data
@Entity
@AllArgsConstructor
@Table(name = "HC_TIPO_CONTRATO_CONFIGURACION")
public class HcTipoContratoConfiguracion implements Entidad {

    @Id
    @Column(name = "id_tipo_configuracion", nullable = false)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name="id_tipo_contrato", nullable=false)
    private HcTipoContrato hcTipoContrato;

    @Column(name = "habilitar_contraparte", nullable = false)
    private String esContraparte;
    
    @Column(name = "habilitar_representante", nullable = false)
    private String esRepresentante;

    @Column(name = "habilitar_doc_firmado", nullable = false)
    private String esDocFirmado;
   
    @Column(name = "indicador_plantilla", nullable = false)
    private String esPlantilla;
    
    @Column(name = "estado_solicitud", nullable = false)
    private String estadoSolicitud;

    @Column(name = "estado", nullable = false)
    private String estado;
    
    @Column(name = "sumilla", nullable = false)
    private String sumilla;
    
    @Column(name = "id_recurso_boton", nullable = true)
    private Integer idRecurso;
    
    @Column(name = "unir_doc", nullable = false)
    private String esDocumentoUnido;

    public HcTipoContratoConfiguracion() {

    }

    @Override
    public String getLabel() {
        return null;
    }
}
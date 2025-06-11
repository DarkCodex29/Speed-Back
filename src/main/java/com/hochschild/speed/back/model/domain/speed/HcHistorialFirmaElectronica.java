package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Data
@Entity
@AllArgsConstructor
@Table(name = "HC_HISTORIAL_POR_FIRMALECTRONICA")
public class HcHistorialFirmaElectronica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historia_firma_electronica", nullable = false)
    private Integer id;

    @Column(name = "id_felectronica_por_documentolegal")
    private Integer idHcFirmaElectronicaDocLegal;

    @Column(name = "tipo_evento", length = 20)
    private String tipoEvento;

    @Column(name = "representante")
    private Integer representante;

    @Column(name = "descripcion_evento", length = 20)
    private String descripcionEvento;

    @Column(name = "estado_evento", length = 20)
    private String estadoEvento;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_evento")
    private Date fechaEvento;

    @Column(name = "observacion", length = 500)
    private String observacion;

    public HcHistorialFirmaElectronica() {

    }
}
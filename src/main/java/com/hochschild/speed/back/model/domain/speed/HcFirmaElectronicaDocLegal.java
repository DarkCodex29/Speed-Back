package com.hochschild.speed.back.model.domain.speed;

import com.hochschild.speed.back.model.domain.speed.Entidad;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

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

/**
 *
 * @author Luciano Carhuaricra - lcarhuaricra@consultorahdc.com
 * @date 01/03/2021
 */

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "HC_FIRMAELECTRONICA_POR_DOCUMENTO_LEGAL")
public class HcFirmaElectronicaDocLegal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_felectronica_por_documentolegal", nullable = false)
    private Integer id;

    @Column(name = "id_documento_legal")
    private Integer idDocumentoLegal;

    @Column(name = "referencia_id_Keynua", length = 500)
    private String idReferenciaIdKeynua;

    @Column(name = "responsable")
    private Integer responsable;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha_creacion")
    private Date fechaCreacion;
}


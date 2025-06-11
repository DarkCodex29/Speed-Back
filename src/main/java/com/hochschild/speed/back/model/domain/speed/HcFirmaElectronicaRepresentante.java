package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


@Data
@Entity
@AllArgsConstructor
@Table(name = "HC_FIRMAELECTRONICA_POR_REPRESENTANTE")
public class HcFirmaElectronicaRepresentante implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_felectronica_representante", nullable = false)
    private Integer id;

    @Column(name = "id_felectronica_por_documentolegal")
    private Integer idHcFirmaElectronicaDocLegal;

    @Column(name = "representante")
    private Integer representante;

    @Lob
    @Column(name = "enlace")
    private String enlace;

    @Column(name = "id_tipo_representante")
    private Integer idTipoRepresentante;

    @Column(name = "apellido_paterno")
    private String apellidoPaterno;

    @Column(name = "apellido_materno")
    private String apellidoMaterno;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "numero_identificacion")
    private String numeroIdentificacion;

    @Column(name = "correo")
    private String correo;

    public HcFirmaElectronicaRepresentante() {

    }
}
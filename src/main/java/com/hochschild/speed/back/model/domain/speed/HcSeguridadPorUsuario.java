package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Data
@Entity
@AllArgsConstructor
@Table(name = "HC_SEGURIDAD_POR_USUARIO")
public class HcSeguridadPorUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seguridad_usuario", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_seguridad_por_documento_legal")
    private HcSeguridadPorDocumentoLegal hcSeguridadPorDocumentoLegal;

    @ManyToOne
    @JoinColumn(name = "tipo_usuario")
    private Parametro tipoUsuario;

    @Column(name = "codigo_usuario")
    private int codigoUsuario;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    private String eliminado;

    public HcSeguridadPorUsuario() {

    }
}
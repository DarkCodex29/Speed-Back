package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ASISTENTEVIRTUAL_CORREO")
public @Data class AsistenteVCorreo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCorreoAsistente", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(length = 1)
    private String vigente;

    @Column(name = "usuario_creacion")
    private String usuarioCreacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

}

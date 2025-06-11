package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ASISTENTEVIRTUAL_USABILIDAD")
@Data
public class AsistenteVirtualUsabilidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAsistenteUsabilidad", nullable = false)
    private Integer id;

    private Integer idOpcionAsistente;

    @Column(length = 1)
    private String esOpcionAsistente;

    @Column(name = "usuario_creacion")
    private String usuarioCreacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
}

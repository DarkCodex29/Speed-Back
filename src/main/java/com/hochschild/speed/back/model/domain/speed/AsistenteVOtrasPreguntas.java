package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ASISTENTEVIRTUAL_OTRASPREGUNTAS")
@Data
public class AsistenteVOtrasPreguntas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOtrasPreguntas", nullable = false)
    private Integer id;

    private String pregunta;
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
}

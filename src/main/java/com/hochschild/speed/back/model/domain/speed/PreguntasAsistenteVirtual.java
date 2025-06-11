package com.hochschild.speed.back.model.domain.speed;


import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "PreguntasAsistenteVirtual")
public class PreguntasAsistenteVirtual {

    @EmbeddedId
    private PreguntaAsistenteVirtualPK id;

    @ManyToOne
    @JoinColumn(name = "idGrupoAsistenteVirtual", nullable = false)
    private GrupoAsistenteVirtual grupo;

    @Column(columnDefinition = "char(6)")
    private String codigoPregunta;

    private String descripcionPregunta;

    @Column(columnDefinition = "char(6)")
    private String codigoRespuesta;

    @Column(length = 65535, columnDefinition = "Text")
    private String descripcionRespuesta;

    @Column(columnDefinition = "char(1)")
    private String vigente;

    public PreguntasAsistenteVirtual() {

    }

    public PreguntasAsistenteVirtual(PreguntaAsistenteVirtualPK id, GrupoAsistenteVirtual grupo, String codigoPregunta, String descripcionPregunta, String codigoRespuesta, String descripcionRespuesta, String vigente) {
        this.id = id;
        this.grupo = grupo;
        this.codigoPregunta = codigoPregunta;
        this.descripcionPregunta = descripcionPregunta;
        this.codigoRespuesta = codigoRespuesta;
        this.descripcionRespuesta = descripcionRespuesta;
        this.vigente = vigente;
    }
}
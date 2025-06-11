package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "GrupoAsistenteVirtual")
@Data
public class GrupoAsistenteVirtual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idGrupoAsistenteVirtual")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idArea")
    private Parametro area;

    @ManyToOne
    @JoinColumn(name = "idTema")
    private Parametro tema;

    private Character vigente;


}

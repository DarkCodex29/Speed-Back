package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Data
@Entity
@AllArgsConstructor
@Table(name="SISTEMA_OPCIONES")
public class SistemaOpciones implements Serializable{

    private static final long serialVersionUID = -4011805751889998514L;

    @Id
    private Long idOpcion;

    private Long idPadre;

    @Size(min = 1, max = 100)
    private String opcion;

    @Size(min = 1, max = 200)
    private String linkOpcion;

    @Size(min = 1, max = 50)
    private String orden;

    private Long idAplicacion;

    @Size(min = 1, max = 1)
    private String esMenu;

    @Size(min = 1, max = 1)
    private String vigente;

    public SistemaOpciones() {

    }
}
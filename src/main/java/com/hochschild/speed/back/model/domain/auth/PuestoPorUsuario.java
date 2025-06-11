package com.hochschild.speed.back.model.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PuestoPorUsuario")
public class PuestoPorUsuario {

    @Id
    private String idPuesto;

    private String idUsuario;

    private String idPersonal;

    private String nombreUsuario;

    private String apellidoUsuario;

    private String emailPer;

    private String areaTrabajo;

    private String areaUsuario;

    private String puestoUsuario;

    private String correoUsuario;

    private String activo;

}
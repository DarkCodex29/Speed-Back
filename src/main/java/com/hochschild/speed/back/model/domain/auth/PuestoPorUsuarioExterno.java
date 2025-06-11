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
@Table(name = "PuestoPorUsuarioExterno")
public class PuestoPorUsuarioExterno {

    @Id
    private String idPuesto;
    private String idUsuario;
    private String nombre;
    private String mail;

}


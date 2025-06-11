package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.*;


@Data
@Entity
@AllArgsConstructor
@Table(name="HC_TIPO_CONTRATO")
public class HcTipoContrato implements Entidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_contrato", nullable = false)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column
    private Character codigo;

    public HcTipoContrato() {

    }

    @Override
    public String getLabel() {
        return null;
    }
}
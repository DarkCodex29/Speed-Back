package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import javax.persistence.*;

@Builder
@Data
@Entity
@AllArgsConstructor
@Table(name = "HC_PAIS")
public class HcPais implements Entidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pais", nullable = false)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(length = 20)
    private String codigo;

    private Integer orden;

    private String estado;

    public HcPais() {

    }

    @Override
    public String getLabel() {
        return null;
    }
}
package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Data
@Entity
@AllArgsConstructor
@Table(name = "TIPO_CLIENTE")
public class TipoCliente implements Entidad {

    @Id
    @Column(name = "id_tipo_cliente", nullable = false)
    private Integer id;

    @Column(nullable = false, length = 20)
    private String codigo;

    @Column(nullable = false, length = 30)
    private String nombre;

    @Column(length = 20)
    private String documento;

    @Column(nullable=false,length = 1)
    private String verContraparte;

    @Column(nullable=false,length = 1)
    private String verRepresentante;

    @Column(nullable=false,length = 1)
    private String estado;

    public TipoCliente() {

    }

    @Override
    public String getLabel() {
        return null;
    }
}
package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@Table(name = "HC_PENALIDAD")
public class HcPenalidad implements Entidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_penalidad", nullable = false)
    private Integer id;

    @Column(length = 5000)
    private String descripcion;

    private Integer reiterancia;

    @Column(name = "aplica_penalidad")
    private boolean aplicaPenalidad;

    private boolean estado;

    @Column(length = 500)
    private String etiqueta;

    @Column(name = "aplica_valorDefecto")
    private boolean aplicaPorDefecto;

    @Column(name = "numero_reiterancia")
    private Integer numeroReiterancia;

    @Column(length = 20, name = "tipo_valor")
    private String tipoValor;

    @Column(length = 10)
    private String valor;

    public HcPenalidad() {

    }

    @Override
    public String getLabel() {
        return null;
    }

    public String getEstadoDesc(){
        return estado ? "Activo" : "Inactivo";
    }
}
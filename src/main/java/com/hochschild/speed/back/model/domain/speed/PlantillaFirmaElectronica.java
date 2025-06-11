package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Data
@Entity
@AllArgsConstructor
@Table(name = "PLANTILLAFIRMAELECTRONICA")
public class PlantillaFirmaElectronica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPlantillaFirmaElectronica", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tipoFirma")
    private Parametro tipoFirma;
    
    private String tipoCliente;
    
    private String plantilla;
    
    private String estado;

    public PlantillaFirmaElectronica() {

    }
}
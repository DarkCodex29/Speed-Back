package com.hochschild.speed.back.model.domain.speed;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
@AllArgsConstructor
@Table(name="USUARIO_REPRESENTANTE_COMPANHIA")
public class RepresentanteComp implements Entidad{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id_representante_companhia", nullable = false)
    private Integer id;    
    
    @ManyToOne
    @JoinColumn(name = "representante")
    private Usuario representante;
    
    private String correo;

    private String estado;

    private String nroDocumento;

    public RepresentanteComp() {

    }

    @Override
    public String getLabel() {
        return representante.getNombres() + " " + representante.getApellidos();
    }
}
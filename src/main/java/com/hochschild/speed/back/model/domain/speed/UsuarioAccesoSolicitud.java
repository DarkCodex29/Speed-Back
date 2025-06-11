package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Data
@Entity
@AllArgsConstructor
@Table(name="USUARIO_ACCESO_SOLICITUD")
public class UsuarioAccesoSolicitud implements Entidad{

    @Id
    @Column(name = "id_acceso_solicitud", nullable = false)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "abogado_responsable")
    private Usuario responsable;

    private String estado;

    public UsuarioAccesoSolicitud() {

    }

    @Override
    public String getLabel() {
        return null;
    }
}
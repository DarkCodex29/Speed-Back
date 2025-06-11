package com.hochschild.speed.back.model.domain.speed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;

import lombok.Data;
import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Data
@Entity
@AllArgsConstructor
@Table(name = "HC_GRUPO")
public class HcGrupo implements Entidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo", nullable = false)
    private Integer id;

    @Column(length = 100)
    private String nombre;

    private Character estado;

    @ManyToOne
    @JoinColumn(name = "tipo_grupo")
    private Parametro tipoGrupo;

    private transient List<Boolean> flagUsuarioGrupoExiste;

    private transient List<String> idUsuario;

    private transient List<String> usuario;

    private transient List<String> clave;

    private transient List<String> nombres;

    private transient List<String> apellidos;

    private transient List<String> correo;

    public HcGrupo() {

    }

    @Override
    public String getLabel() {
        return null;
    }

    public String getEstadoDesc(){
        return estado == 'A' ? "Activo" : "Inactivo";
    }
}
package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@Table(name = "VERSION")
public class Version implements Entidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_version", nullable = false)
    private Integer id;

    @Column(length = 150)
    private String nombre;

    @Column(name = "ruta_alfresco", length = 450)
    private String rutaAlfresco;

    @Column(length = 150)
    private String autor;

    @Column(name = "numero_version", length = 50)
    private String numeroVersion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "archivo")
    private Archivo archivo;

    public Version() {

    }

    @Override
    public String getLabel() {
        return null;
    }
}
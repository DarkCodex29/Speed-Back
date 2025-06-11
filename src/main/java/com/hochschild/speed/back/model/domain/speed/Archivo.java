package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@Table(name = "ARCHIVO")
public class Archivo implements Entidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_archivo", nullable = false)
    private Integer id;

    @Column(length = 150, nullable = false)
    private String nombre;

    @Column(name = "ruta_local", length = 450)
    private String rutaLocal;

    private Character estado;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion", nullable = false)
    private Date fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "documento", nullable = false)
    private Documento documento;

    private Integer fila;

    @OneToMany(mappedBy = "archivo")
    private List<Version> versions;
    private transient List<VersionArchivo> versiones;

    private transient String nombreArchivoDisco;

    private transient String rutaAlfresco;

    private transient boolean nuevo;
    private transient String version;

    public Archivo() {

    }

    public Archivo(Integer id) {
        this.id = id;
    }

    public Archivo(Integer id, String nombre, Character estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }

    public Archivo(Integer id, String nombre, String rutaLocal, String rutaAlfresco, Character estado, Date fechaCreacion, Documento documento) {
        this.id = id;
        this.nombre = nombre;
        this.rutaLocal = rutaLocal;
        this.rutaAlfresco = rutaAlfresco;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.documento = documento;
    }

    public Archivo(Integer id, String nombre, Character estado, Date fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "Archivo{"
                + "id=" + id
                + ", nombre='" + nombre + '\''
                + ", rutaLocal='" + rutaLocal + '\''
                + ", estado=" + estado
                + ", fechaCreacion=" + fechaCreacion
                + ", documento=" + documento.getId()
                + ", fila=" + fila
                + ", versions=" + versions
                + ", rutaAlfresco='" + rutaAlfresco + '\''
                + ", nuevo=" + nuevo
                + '}';
    }

    public Archivo(Integer id, String nombre, String rutaLocal, Character estado, Date fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.rutaLocal = rutaLocal;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String getLabel() {
        return null;
    }
}

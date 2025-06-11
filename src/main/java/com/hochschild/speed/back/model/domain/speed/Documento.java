package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@Table(name = "DOCUMENTO")
public class Documento implements Entidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento", nullable = false)
    private Integer id;

    @Column(length = 200)
    private String numero;

    private Integer folios;

    @Column(name = "titulo", length = 255)
    private String titulo;

    @Column(columnDefinition = "TEXT", length = 4000)
    private String observacion;

    @Column(length = 200)
    private String descripcion;

    private Character estado;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_documento")
    private Date fechaDocumento;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion", nullable = false)
    private Date fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "id_tipo_documento", nullable = false)
    private TipoDocumento tipoDocumento;

    @ManyToOne
    @JoinColumn(name = "expediente", nullable = false)
    private Expediente expediente;

    @OneToMany(mappedBy = "documento")
    private List<Archivo> archivos;

    @ManyToOne
    @JoinColumn(name = "autor")
    private Usuario autor;

    public Documento() {

    }

    public String getLabel() {
        String label = "";
        label += numero != null ? numero + " - " : "";
        label += titulo;
        return label;
    }

    @Override
    public String toString() {
        return "Documento{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", folios=" + folios +
                ", titulo='" + titulo + '\'' +
                ", observacion='" + observacion + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estado=" + estado +
                ", fechaDocumento=" + fechaDocumento +
                ", fechaCreacion=" + fechaCreacion +
                ", tipoDocumento=" + tipoDocumento.getId() +
                ", expediente=" + expediente.getId() +
                ", autor=" + autor.getId() +
                '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Documento other = (Documento) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public int getArchivosActivos() {
        int cont = 0;
        for (Archivo archivo:getArchivos()
             ) {
            if (archivo.getEstado()!='I'){
                cont++;
            }
        }
        return cont;
    }
}
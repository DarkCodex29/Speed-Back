package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

import lombok.EqualsAndHashCode;
import org.codehaus.jackson.annotate.JsonIgnore;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name="EXPEDIENTE")
public class Expediente implements Entidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_expediente", nullable = false)
    private Integer id;

    @Column(length = 20, nullable = false)
    private String numero;

    private Character estado;

    @Column(length = 255)
    private String titulo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion", nullable = false)
    private Date fechaCreacion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario creador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_proceso", nullable = false)
    private Proceso proceso;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "expediente")
    private List<Documento> documentos;

    @ManyToOne
    @JoinColumn(name = "padre")
    private Expediente padre;

    @Column(name = "observacionmp", length = 4000)
    private String observacionMp;

    private boolean copiado;

    private transient Integer existente;

    @Column(name = "titulo_notificacion", length = 500)
    private String titulo_notificacion;

    private Boolean bloqueado;

    @JsonIgnore
    @OneToOne(mappedBy = "expediente", cascade = CascadeType.ALL)
    private HcDocumentoLegal documentoLegal;

    @Column(name = "aplica_penalidad")
    private boolean aplicaPenalidad;

    @Column(name = "aplica_arrendamiento")
    private String aplicaArrendamiento;
    
    @Column(name = "firma_electronica")
    private String firmaElectronica;

    public Expediente() {

    }

    public Expediente(Integer id) {
        this.id = id;
    }

    public Expediente(Integer id, String numero, String titulo, Date fechaCreacion, Proceso proceso, Cliente cliente) {
        super();
        this.id = id;
        this.numero = numero;
        this.titulo = titulo;
        this.fechaCreacion = fechaCreacion;
        this.proceso = proceso;
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Expediente{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", estado=" + estado +
                ", titulo='" + titulo + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }

    @Override
    public String getLabel() {
        return null;
    }
}
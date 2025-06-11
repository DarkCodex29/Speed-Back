package com.hochschild.speed.back.model.domain.speed;

import com.hochschild.speed.back.model.domain.speed.utils.Constantes;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Data
@Entity
@AllArgsConstructor
@Table(name = "CLIENTE", uniqueConstraints = @UniqueConstraint(columnNames = {"id_tipo_cliente", "numero_identificacion"}))
public class Cliente implements Entidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente", nullable = false)
    private Integer id;

    @Column(length = 100)
    private String nombre;

    @Column(name = "apellido_paterno", length = 50)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", length = 50)
    private String apellidoMaterno;

    @Column(name = "razon_social", length = 200)
    private String razonSocial;

    @ManyToOne
    @JoinColumn(name = "id_tipo_cliente", nullable = false)
    private TipoCliente tipo;

    @Column(name = "numero_identificacion", nullable = false, length = 20)
    private String numeroIdentificacion;

    @Column(length = 100)
    private String telefono;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    private Character estado;

    private transient List<Direccion> direcciones;

    private Integer distancia;

    @Column(name = "correo", length = 50)
    private String correo;

    @Column(name = "es_contraparte")
    private Boolean esContraparte;

    @Column(name = "es_representante")
    private Boolean esRepresentante;

    @Column(name = "situacion_sunat", length = 20)
    private String situacionSunat;
    @Column(name = "direccion", length = 4000)
    private String direccion;
    @Column(name = "usuario_creacion")
    private Integer usuarioCreacion;

    @Column(name = "fecha_modificacion")
    private Date fechaModificacion;

    @Column(name = "usuario_modificacion")

    private Integer usuarioModificacion;

    @Column(name = "contacto", length = 200)
    private String contacto;

    private transient String direccionSunat;

    private transient String mensajeSR;

    private transient Boolean labelCompleto;

    public Cliente() {

    }

    public Cliente(Integer id, String nombre, String apellidoPaterno, String apellidoMaterno,String correo, String razonSocial, TipoCliente tipo, String numeroIdentificacion, String labelCompleto) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.correo=correo;
        this.razonSocial = razonSocial;
        this.tipo = tipo;
        this.numeroIdentificacion = numeroIdentificacion;
        if (labelCompleto != null) {
            this.labelCompleto = Boolean.parseBoolean(labelCompleto);
        }
    }

    public String getNombres() {
        String texto = "";
        if (tipo.getCodigo().equals(Constantes.TIPO_CLIENTE_JURIDICA)) {
            if (razonSocial != null) {
                texto = razonSocial;
            }
        } else {
            if (apellidoPaterno != null) {
                texto += apellidoPaterno;
            }
            if (apellidoMaterno != null) {
                texto += " " + apellidoMaterno;
            }
            if (nombre != null) {
                texto += ", " + nombre;
            }
        }
        texto += " (" + tipo.getDocumento() + ": " + numeroIdentificacion + ")";
        return texto;
    }

    public String getIdentificacion() {
        return tipo.getDocumento() + ": " + numeroIdentificacion;
    }

    public String getLabelCompleto() {
        return getNombres() + " (" + getIdentificacion() + ")";
    }

    @Override
    public String getLabel() {
        if (labelCompleto != null && labelCompleto) {
            return getLabelCompleto();
        }
        return getNombres();
    }
}
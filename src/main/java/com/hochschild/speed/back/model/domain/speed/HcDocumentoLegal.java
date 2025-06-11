package com.hochschild.speed.back.model.domain.speed;

import com.hochschild.speed.back.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
@Table(name = "HC_DOCUMENTO_LEGAL")
public class HcDocumentoLegal implements Entidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento_legal", nullable = false)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "expediente", nullable = false)
    private Expediente expediente;

    @ManyToOne
    @JoinColumn(name = "ubicacion_legal")
    private Parametro ubicacionDocumento;

    @ManyToOne
    @JoinColumn(name = "hc_area")
    private HcArea area;

    @ManyToOne
    @JoinColumn(name = "contraparte")
    private Cliente contraparte;

    @ManyToOne
    @JoinColumn(name = "responsable", nullable = false)
    private Usuario responsable;

    @ManyToOne
    @JoinColumn(name = "solicitante", nullable = false)
    private Usuario solicitante;

    @Column(nullable = false)
    private Character estado;

    @Column(length = 250)
    private String sumilla;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_borrador")
    private Date fechaBorrador;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_solicitud")
    private Date fechaSolicitud;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_movimiento", nullable = true)
    private Date fechaMovimiento;

    @Column(length = 400)
    private String cnt_domicilio;

    @Column(length = 200)
    private String cnt_nombre_contacto;

    @Column(length = 20)
    private String cnt_telefono_contacto;

    @Column(length = 50)
    private String cnt_correo_contacto;

    @Column(length = 50)
    private String numero;

    @OneToOne(mappedBy = "documentoLegal", cascade = CascadeType.ALL)
    private HcContrato contrato;

    @OneToOne(mappedBy = "documentoLegal", cascade = CascadeType.ALL)
    private HcAdenda adenda;

    public HcDocumentoLegal() {

    }

    public String getFechaBorradorFormat(){
        return DateUtil.convertDateToString(this.fechaBorrador,DateUtil.FORMAT_DATE);
    }

    public String getFechaSolicitudFormat(){
        return DateUtil.convertDateToString(this.fechaSolicitud,DateUtil.FORMAT_DATE);
    }

    public String getFechaMovimientoFormat(){
        return DateUtil.convertDateToString(this.fechaMovimiento,DateUtil.FORMAT_DATE);
    }

    public HcDocumentoLegal(Integer id, String numero, String sumilla) {
        this.id = id;
        this.numero = numero;
        this.sumilla = sumilla;
    }

     @Override
    public String toString() {
        return "HcDocumentoLegal{" + "id=" + id + ", estado=" + estado + "}";
    }

    @Override
    public String getLabel() {
        return null;
    }
}

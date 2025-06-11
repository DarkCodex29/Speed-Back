package com.hochschild.speed.back.model.domain.speed;

import com.hochschild.speed.back.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@Table(name="HC_CONTRATO")
public class HcContrato implements Entidad {

    @Id
    @Column(name = "id_documento_legal", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tipo_contrato")
    private HcTipoContrato tipo_contrato;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_fin")
    private Date fechaFin;

    @Column(nullable = false)
    private Boolean indefinido;

    @ManyToOne
    @JoinColumn(name = "moneda")
    private Parametro moneda;

    @Column
    private String modalidad_pago;

    @Column(name = "precio_Unitario")
    private Float precioUnitario;

    @Column
    private Float monto;

    @Column(nullable = false)
    private Boolean adelanto;

    @Column
    private Float monto_adelanto;

    @Column
    private transient String montoTransient;

    @Column
    private transient String monto_adelantoTransient;

    @Column(length = 50)
    private String periodicidad;

    @Column(nullable = false)
    private Boolean renovacion_auto;

    private Integer periodo_renovar;

    @Column(columnDefinition = "TEXT", length = 4000)
    private String descripcion;

    @OneToOne
    @JoinColumn(name = "id_documento_legal")
    @MapsId
    private HcDocumentoLegal documentoLegal;

    @Column(nullable = false)
    private Boolean arrendamiento;

    @Column(name = "aplica_periodicidad")
    private Integer aplicaPeriodicidad;

    @Column(name = "aplica_modalidad_pago")
    private Integer aplicaModalidadPago;

    public HcContrato() {

    }

    public String getFechaInicioFormat(){
        return DateUtil.convertDateToString(this.fechaInicio,DateUtil.FORMAT_DATE);
    }

    public String getFechaFinFormat(){
        return DateUtil.convertDateToString(this.fechaFin,DateUtil.FORMAT_DATE);
    }

    @Override
    public String getLabel() {
        return null;
    }
}
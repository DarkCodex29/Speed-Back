package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@Table(name = "HC_MOVIMIENTO_TRANSACCION")
public class HcMovimientoTransaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento_transaccion", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="tipo_movimiento")
    private Parametro tipoMovimiento;

    @ManyToOne
    @JoinColumn(name="tipo_registro")
    private Parametro tipoRegistro;

    @Column(name="codigo_registro")
    private Integer codigoRegistro;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha_movimiento")
    private Date fechaMovimiento;

    private Integer cantidad;

    @Column(name = "usuario_creacion")
    private Integer usuarioCreacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;


    public HcMovimientoTransaccion() {

    }
}

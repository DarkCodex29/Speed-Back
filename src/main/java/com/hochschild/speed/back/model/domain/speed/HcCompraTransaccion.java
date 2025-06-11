package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@Table(name = "HC_COMPRA_TRANSACCION")
public class HcCompraTransaccion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra_transaccion", nullable = false)
    private Integer id;

    private String descripcion;

    @Column(name = "cantidad_transaccion")
    private Integer cantidadTransaccion;

    @Column(name = "costo_transaccion")
    private BigDecimal costoTransaccion;

    private String estado;

    @Column(name = "usuario_creacion")
    private String usuarioCreacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_modificacion")
    private Date fechaModificacion;

    public HcCompraTransaccion() {}
}

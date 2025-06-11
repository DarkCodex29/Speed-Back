package com.hochschild.speed.back.model.domain.speed;

import com.hochschild.speed.back.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@AllArgsConstructor
@Table(name = "HC_ADENDA")
public class HcAdenda implements Entidad {

    @Id
    @Column(name = "id_documento_legal", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "contrato", nullable = false)
    private HcContrato contrato;

    @ManyToOne
    @JoinColumn(name = "id_tipo_contrato", nullable = true)
    private HcTipoContrato hcTipoContrato;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "inicio_vigencia")
    private Date inicioVigencia;

    @Column
    private Boolean modifica_fin;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "nueva_fecha_fin")
    private Date nuevaFechaFin;

    private Boolean indefinido;

    @Column(columnDefinition = "TEXT", length = 4000)
    private String descripcion;

    @Column(name = "secuencia", nullable = false)
    private Integer secuencia;

    @Override
    public String toString() {
        return "HcAdenda{" +
                "id=" + id +
                ", contrato=" + contrato.getId() +
                '}';
    }

    @OneToOne
    @JoinColumn(name = "id_documento_legal")
    @MapsId
    private HcDocumentoLegal documentoLegal;

    public HcAdenda() {

    }

    public String getInicioVigenciaFormat(){
        return DateUtil.convertDateToString(this.inicioVigencia,DateUtil.FORMAT_DATE);
    }

    public String getNuevaFechaFinFormat(){
        return DateUtil.convertDateToString(this.nuevaFechaFin,DateUtil.FORMAT_DATE);
    }

    @Override
    public String getLabel() {
        return null;
    }
}
package com.hochschild.speed.back.model.domain.speed;

import com.hochschild.speed.back.model.domain.speed.utils.UsuarioNotificacionDTO;
import lombok.AllArgsConstructor;

import lombok.Data;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Data
@Entity
@AllArgsConstructor
@Table(name="HC_SEGURIDAD_POR_DOCUMENTO_LEGAL")
public class HcSeguridadPorDocumentoLegal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seguridad_por_documento_legal", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_documento_legal")
    private HcDocumentoLegal documentoLegal;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    private String eliminado;

    private transient List<UsuarioNotificacionDTO> usuarios;

    public HcSeguridadPorDocumentoLegal() {

    }
}
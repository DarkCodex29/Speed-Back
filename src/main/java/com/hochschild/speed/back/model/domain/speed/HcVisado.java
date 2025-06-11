package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
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
@Table(name = "HC_VISADO")
public class HcVisado implements Entidad{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_visado", nullable=false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="id_documento_legal", nullable=false)
	private HcDocumentoLegal documentoLegal;
	
	@Column(nullable=false)
	private Integer secuencia;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion", nullable=false)
	private Date fechaCreacion;
	
	@Column(columnDefinition="TEXT",length=4000)
	private String observacion;

	public HcVisado() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}
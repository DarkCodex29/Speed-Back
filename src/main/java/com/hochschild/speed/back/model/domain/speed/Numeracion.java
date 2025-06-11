package com.hochschild.speed.back.model.domain.speed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="NUMERACION")
public class Numeracion implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_numeracion",nullable=false)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="id_tipo_documento",nullable=false)
	private TipoDocumento tipoDocumento;

	@ManyToOne
	@JoinColumn(name="id_area",nullable=false)
	private Area area;

	@Column(name="pre_formato",length=50)
	private String preformato;

	@Column(name="post_formato",length=250)
	private String postFormato;

	private Integer valor;

	@Column(length=1)
	private String tipo;

	private Integer longitud;

	public Numeracion() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}
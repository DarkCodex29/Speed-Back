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
import javax.persistence.UniqueConstraint;


@Data
@Entity
@AllArgsConstructor
@Table(name="FIRMA_POR_ARCHIVO",uniqueConstraints={@UniqueConstraint(columnNames={"id_archivo","version"})})
public class FirmaPorArchivo implements Entidad{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_firma_por_archivo",nullable=false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="id_archivo",nullable=false)
	private Archivo archivo;
	
	@Column(length=10,nullable=false)
	private String version;
	
	@ManyToOne
	@JoinColumn(name="id_usuario",nullable=false)
	private Usuario usuario;
	
	@Column(name="fecha_firma",nullable=false)
	private Date fechaFirma;
	
	@Column(length=250,nullable=false)
	private String firmante;

	public FirmaPorArchivo() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}

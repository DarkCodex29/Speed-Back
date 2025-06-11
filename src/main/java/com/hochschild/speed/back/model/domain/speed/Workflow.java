package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@Table(name="WORKFLOW",uniqueConstraints=@UniqueConstraint(columnNames={"id_proceso","version"}))
public class Workflow implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_workflow",nullable=false)
	private Integer id;

	@OneToOne
	@JoinColumn(name="id_proceso",unique=true,nullable=false)
	private Proceso proceso;

	@Column(length=100, nullable=false)
	private String nombre;

	@Column(length=200)
	private String descripcion;
	
	@Column(nullable=false)
	private int version;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion",nullable=false)
	private Date fechaCreacion;

	@OneToMany(mappedBy="workflow",orphanRemoval=true)
	private List<PasoWorkflow> pasos;

	public Workflow() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}
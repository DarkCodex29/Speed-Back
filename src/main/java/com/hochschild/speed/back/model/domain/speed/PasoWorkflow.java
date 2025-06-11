package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.*;


@Data
@Entity
@AllArgsConstructor
@Table(name="PASO_WORKFLOW")
@Inheritance(strategy=InheritanceType.JOINED)
public class PasoWorkflow implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_paso",nullable=false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="id_workflow",nullable=false)
	private Workflow workflow;
	
	@Column(length=100)
	private String nombre;
	
	@Column(nullable=true)
	private boolean creador;

	@ManyToOne
	@JoinColumn(name="usuario_destinatario")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name="rol_destinatario")
	private Rol rol;
	
	@Column(nullable=false)
	private boolean primero;
	
	@ManyToOne
	@JoinColumn(name="siguiente_paso")
	private PasoWorkflow siguiente;
	
	private transient String siguientePaso;

	public PasoWorkflow() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}
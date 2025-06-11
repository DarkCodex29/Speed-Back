package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.persistence.*;

@Builder
@Data
@Entity
@AllArgsConstructor
@Table(name="HC_COMPANIA")
public class HcCompania implements Entidad{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_compania", nullable=false)
	private Integer id;
	
	@Column(length=200, nullable=false)
	private String nombre;
	
	@ManyToOne
	@JoinColumn(name="pais", nullable=false)
	private HcPais pais;
	
	@Column(nullable=false)
	private Character estado;
	
	@Column(length=10)
	private String codigo;
        
	@Column(length=30)
	private String abreviatura;

	public HcCompania() {

	}

	@Override
	public String getLabel() {
		return null;
	}
}
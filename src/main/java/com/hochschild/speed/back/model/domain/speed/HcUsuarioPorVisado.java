package com.hochschild.speed.back.model.domain.speed;

import com.hochschild.speed.back.util.AppUtil;
import lombok.Data;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Data
@Entity
@Table(name="HC_USUARIO_POR_VISADO")
public class HcUsuarioPorVisado {
	
	@EmbeddedId
	private HcUsuarioPorVisadoPK id;
	
	@Column(nullable=false)
	private Character estado;
	
	@Column(nullable=false)
	private Integer orden;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_visado")
	private Date fechaVisado;

	public HcUsuarioPorVisado() {

	}

	public String getEstadoFormat(){
		return AppUtil.getNombreEstadoVisadores(estado);
	}
}
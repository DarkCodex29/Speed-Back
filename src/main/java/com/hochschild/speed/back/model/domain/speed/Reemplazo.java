package com.hochschild.speed.back.model.domain.speed;

import com.hochschild.speed.back.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@Table(name="REEMPLAZO")
public class Reemplazo implements Entidad{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_reemplazo",nullable=false)
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date desde;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date hasta;
	
	@ManyToOne
	@JoinColumn(name="reemplazado")
	private Usuario reemplazado;
	
	@ManyToOne
	@JoinColumn(name="reemplazante")
	private Usuario reemplazante;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="PROCESO_REEMPLAZO",joinColumns={@JoinColumn(name="id_reemplazo")},inverseJoinColumns={@JoinColumn(name="id_proceso")})
	private List<Proceso> procesos;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_creacion")
	private Date fechaCreacion;

	public Reemplazo() {

	}

	@Override
	public int hashCode(){
		final int prime=31;
		int result=1;
		result=prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Reemplazo other=(Reemplazo) obj;
		if(id == null){
			if(other.id != null)
				return false;
		}
		else if(!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String getLabel() {
		return null;
	}

	public String getFechaDesde() {
		return DateUtil.convertDateToString(this.desde,DateUtil.FORMAT_DATE_HOUR_MINUTE);
	}

	public String getFechaHasta() {
		return DateUtil.convertDateToString(this.hasta,DateUtil.FORMAT_DATE_HOUR_MINUTE);
	}
}
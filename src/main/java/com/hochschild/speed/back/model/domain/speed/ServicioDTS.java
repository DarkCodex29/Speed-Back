package com.hochschild.speed.back.model.domain.speed;

import lombok.AllArgsConstructor;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

@NamedStoredProcedureQuery(
		name = "consultarServiciosBusqueda",
		procedureName = "stpr_ReporteServicioContraparte", 
		resultClasses = ServicioDTS.class, 
		parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "id_cliente"), 
			@StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "numero"),
			@StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "vencimientoIni"), 
			@StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "vencimientoFin"),
			@StoredProcedureParameter(mode = ParameterMode.IN, type = Double.class, name = "montoIni"), 
			@StoredProcedureParameter(mode = ParameterMode.IN, type = Double.class, name = "montoFin"),
			@StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "tipo_contrato"), 
			@StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "estado"),
			@StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "pais"), 
			@StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "compania"),
			@StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "area"),
			@StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "tipoUbicacion"), 
			@StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "ubicacion")
			
			
		}
		)

@Data
@Entity
@AllArgsConstructor
@Table(name="SERVICIODTS")
public class ServicioDTS implements Entidad {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer fila;
	private Integer expediente;
	private String numero;
	private String tipo_contrato;
	private String servicio;
	private String contraparte;
	private String ubicacion;
	private String tipo;
	private String vencimiento;
	private String modalidad_pago;
	private String monto;
	private String estado;

	public ServicioDTS() {

	}

	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public void setId(Integer intgr) {

	}

	@Override
	public String getLabel() {
		return null;
	}
}
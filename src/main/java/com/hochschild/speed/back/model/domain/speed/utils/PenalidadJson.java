package com.hochschild.speed.back.model.domain.speed.utils;

import java.util.List;

public class PenalidadJson {
	
	private Integer idPenalidad;
	
	private String descripcion;
	
	private boolean aplica;
	
	private List<ReiteranciasJson> reiterancias;
        
        //Nuevos valores en el json
	private boolean aplicaValorDefecto;
        
        private Integer numeroReiterancia;
        
        private String tipoValor;
        
        private String valor;
        
	public Integer getIdPenalidad() {
		return idPenalidad;
	}

	public void setIdPenalidad(Integer idPenalidad) {
		this.idPenalidad = idPenalidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isAplica() {
		return aplica;
	}

	public void setAplica(boolean aplica) {
		this.aplica = aplica;
	}

	public List<ReiteranciasJson> getReiterancias() {
		return reiterancias;
	}

	public void setReiterancias(List<ReiteranciasJson> reiterancias) {
		this.reiterancias = reiterancias;
	}

        /**
         * @return the aplicaValorDefecto
         */
        public boolean isAplicaValorDefecto() {
            return aplicaValorDefecto;
        }

        /**
         * @param aplicaValorDefecto the aplicaValorDefecto to set
         */
        public void setAplicaValorDefecto(boolean aplicaValorDefecto) {
            this.aplicaValorDefecto = aplicaValorDefecto;
        }

        /**
         * @return the tipoValor
         */
        public String getTipoValor() {
            return tipoValor;
        }

        /**
         * @param tipoValor the tipoValor to set
         */
        public void setTipoValor(String tipoValor) {
            this.tipoValor = tipoValor;
        }

        /**
         * @return the valor
         */
        public String getValor() {
            return valor;
        }

        /**
         * @param valor the valor to set
         */
        public void setValor(String valor) {
            this.valor = valor;
        }	

        /**
         * @return the numeroReiterancia
         */
        public Integer getNumeroReiterancia() {
            return numeroReiterancia;
        }

        /**
         * @param numeroReiterancia the numeroReiterancia to set
         */
        public void setNumeroReiterancia(Integer numeroReiterancia) {
            this.numeroReiterancia = numeroReiterancia;
        }
}
package com.hochschild.speed.back.util.parametro;

import com.hochschild.speed.back.model.domain.speed.PasoWorkflow;
import com.hochschild.speed.back.model.domain.speed.PasoXOR;
import com.hochschild.speed.back.model.domain.speed.Workflow;
import java.util.List;

public class ParametroWorkflow{
	
	private Workflow workflow;
	
	private List<PasoWorkflow> pasos;
	
	private List<PasoXOR> xors;

	public Workflow getWorkflow(){
		return workflow;
	}

	public void setWorkflow(Workflow workflow){
		this.workflow=workflow;
	}

	public List<PasoWorkflow> getPasos(){
		return pasos;
	}

	public void setPasos(List<PasoWorkflow> pasos){
		this.pasos=pasos;
	}

	public List<PasoXOR> getXors(){
		return xors;
	}

	public void setXors(List<PasoXOR> xors){
		this.xors=xors;
	}

}

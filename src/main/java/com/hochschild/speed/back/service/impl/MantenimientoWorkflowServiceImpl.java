package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.mantenimiento.ProcesoWorkBean;
import com.hochschild.speed.back.model.bean.mantenimiento.WorkflowBean;
import com.hochschild.speed.back.model.domain.speed.Proceso;
import com.hochschild.speed.back.model.domain.speed.Workflow;
import com.hochschild.speed.back.model.filter.mantenimiento.WorkflowFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.ProcesoRepository;
import com.hochschild.speed.back.repository.speed.WorkflowRepository;
import com.hochschild.speed.back.service.MantenimientoWorkflowService;
import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("MantenimientoWorkflowService")
public class MantenimientoWorkflowServiceImpl implements MantenimientoWorkflowService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoWorkflowServiceImpl.class.getName());
    private final WorkflowRepository workflowRepository;
    private final ProcesoRepository procesoRepository;

    public MantenimientoWorkflowServiceImpl(WorkflowRepository workflowRepository,
                                            ProcesoRepository procesoRepository) {
        this.workflowRepository = workflowRepository;
        this.procesoRepository = procesoRepository;
    }

    @Override
    public Workflow find(Integer id) {
        return workflowRepository.findById(id);
    }

    @Override
    public List<Workflow> list(WorkflowFilter filter) {

        List<Workflow> list = workflowRepository.list(filter.getDescripcion(), filter.getNombre());
        for (Workflow workflow : list) {
            workflow.setProceso(null);
            workflow.setPasos(null);
        }
        return list;
    }

    @Override
    public List<ProcesoWorkBean> listProcesos() {

        List<Proceso> procesos = procesoRepository.obtenerProcesosActivos();
        List<ProcesoWorkBean> workProcesos = new ArrayList<>();

        for (Proceso proceso : procesos) {
            ProcesoWorkBean procesoWorkBean = new ProcesoWorkBean();
            procesoWorkBean.setId(proceso.getId());
            procesoWorkBean.setNombre(proceso.getNombre());
            procesoWorkBean.setDescripcion(proceso.getDescripcion());
            workProcesos.add(procesoWorkBean);
        }
        return workProcesos;
    }

    @Override
    public ResponseModel save(WorkflowBean workflowBean) {

        ResponseModel responseModel = new ResponseModel();

        try {
            Proceso proceso = procesoRepository.findById(workflowBean.getIdProceso());
            if (workflowBean.getId() != null){
                Workflow workflowBD = workflowRepository.findById(workflowBean.getId());
                workflowBD.setDescripcion(workflowBean.getDescripcion());
                workflowBD.setFechaCreacion(new Date());
                workflowBD.setNombre(workflowBean.getNombre());
                workflowBD.setVersion(workflowBean.getVersion());
                workflowBD.setProceso(proceso);
                workflowRepository.save(workflowBD);
                responseModel.setMessage("Workflow actualizado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(workflowBD.getId());
            }else{
                Workflow workflow = new Workflow();
                workflow.setDescripcion(workflowBean.getDescripcion());
                workflow.setFechaCreacion(new Date());
                workflow.setNombre(workflowBean.getNombre());
                workflow.setVersion(workflowBean.getVersion());
                workflow.setProceso(proceso);
                workflowRepository.save(workflow);
                responseModel.setMessage("Workflow creado");
                responseModel.setHttpSatus(HttpStatus.OK);
                responseModel.setId(workflow.getId());
            }
        } catch (DataIntegrityViolationException e ){
            responseModel.setMessage("Ya existe workflow vinculado a ese proceso");
            responseModel.setHttpSatus(HttpStatus.OK);
            responseModel.setId(null);
        } catch (Exception ex) {
            System.out.println("history already exist");
            LOGGER.info(ex.getMessage());
        }
        return responseModel;
    }
}
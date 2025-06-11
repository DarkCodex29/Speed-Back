package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.bean.mantenimiento.ProcesoWorkBean;
import com.hochschild.speed.back.model.bean.mantenimiento.WorkflowBean;
import com.hochschild.speed.back.model.domain.speed.Workflow;
import com.hochschild.speed.back.model.filter.mantenimiento.WorkflowFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import java.util.List;

public interface MantenimientoWorkflowService {
    Workflow find(Integer id);
    List<Workflow> list(WorkflowFilter filter);
    List<ProcesoWorkBean> listProcesos();
    ResponseModel save(WorkflowBean campoBean);
}

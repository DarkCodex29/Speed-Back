package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.mantenimiento.ProcesoWorkBean;
import com.hochschild.speed.back.model.bean.mantenimiento.WorkflowBean;
import com.hochschild.speed.back.model.domain.speed.Workflow;
import com.hochschild.speed.back.model.filter.mantenimiento.WorkflowFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoWorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author FERNANDO SALVI
 * @since 12/06/2022
 */
@RestController
@RequestMapping("/workflow")
public class WorkflowController {
    private Logger LOGGER = LoggerFactory.getLogger(WorkflowController.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final MantenimientoWorkflowService mantenimientoWorkflowService;

    public WorkflowController(JwtTokenUtil jwtTokenUtil, MantenimientoWorkflowService mantenimientoWorkflowService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.mantenimientoWorkflowService = mantenimientoWorkflowService;
    }

    @GetMapping(value = "/find/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Workflow> find(@PathVariable("id") Integer id) {
        Workflow result = mantenimientoWorkflowService.find(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Workflow>> list(@RequestBody WorkflowFilter workflowFilter){
        List<Workflow> result = mantenimientoWorkflowService.list(workflowFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/procesos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProcesoWorkBean>> listSedes(){
        List<ProcesoWorkBean> result = mantenimientoWorkflowService.listProcesos();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> save(@RequestBody WorkflowBean workflowBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = mantenimientoWorkflowService.save(workflowBean);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
}
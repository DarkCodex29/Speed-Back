package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.bean.VerificacionClienteBean;
import com.hochschild.speed.back.model.bean.mantenimiento.ClienteBean;
import com.hochschild.speed.back.model.domain.speed.Cliente;
import com.hochschild.speed.back.model.domain.speed.TipoCliente;
import com.hochschild.speed.back.model.filter.BuscarClienteFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.security.JwtTokenUtil;
import com.hochschild.speed.back.service.MantenimientoClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/cliente")
public class ClienteController {
    private Logger LOGGER = LoggerFactory.getLogger(ClienteController.class);
    private final MantenimientoClienteService clienteService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public ClienteController(MantenimientoClienteService mantenimientoClienteService, JwtTokenUtil jwtTokenUtil) {
        this.clienteService = mantenimientoClienteService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @ResponseBody
    @PostMapping(value = "/listarClientes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cliente>> listarClientes(@RequestBody BuscarClienteFilter clienteFilter){
        LOGGER.info(clienteFilter.toString());
        return clienteService.listarCliente(clienteFilter);
    }
    @ResponseBody
    @RequestMapping(value = "/listar", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Cliente>> listarClientes(){

        return clienteService.listarCliente();
    }

    @ResponseBody
    @RequestMapping(value = "/tiposActivosCliente", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TipoCliente>> listarTiposActivos() {
        return clienteService.obtenerTiposActivos();
    }

    @ResponseBody
    @RequestMapping(value = "/guardarClienteModal", method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> guardarClienteModal(@RequestBody ClienteBean clienteBean, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Integer idUsuario = (Integer) jwtTokenUtil.getClaimFromToken(token, "idUsuario");
        ResponseModel responseModel = clienteService.guardarCliente(clienteBean, idUsuario);
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, params = {"idCliente"})
    public ResponseEntity<Cliente> obtenerCliente(Integer idCliente) {
        return clienteService.buscarCliente(idCliente);
    }
    @GetMapping(value = "/validar",consumes = MediaType.APPLICATION_JSON_VALUE, params = {"dni"})
    public ResponseEntity<VerificacionClienteBean> verificarCliente(String dni) {
        return clienteService.verificarExistenciaCliente(dni);
    }
    @GetMapping(value = "/validarContraparte",consumes = MediaType.APPLICATION_JSON_VALUE, params = {"dni"})
    public ResponseEntity<VerificacionClienteBean> verificarClienteContraparte(String dni) {
        return clienteService.verificarExistenciaClienteContraparte(dni);
    }
}
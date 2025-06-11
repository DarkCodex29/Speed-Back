package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.VerificacionClienteBean;
import com.hochschild.speed.back.model.bean.mantenimiento.ClienteBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.filter.BuscarClienteFilter;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.MantenimientoClienteService;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MantenimientoClienteServiceImpl implements MantenimientoClienteService {

    private static final Logger LOGGER = Logger.getLogger(MantenimientoClienteServiceImpl.class.getName());
    private final ClienteRepository clienteRepository;
    private final TipoClienteRepository tipoClienteRepository;
    private final DireccionRepository direccionRepository;
    private final HcRepresentantePorContraparteRepository hcRepresentantePorContraparteRepository;
    private final ClienteLogRepository clienteLogRepository;

    @Autowired
    public MantenimientoClienteServiceImpl(ClienteRepository clienteRepository,
                                           TipoClienteRepository tipoClienteRepository,
                                           DireccionRepository direccionRepository, HcRepresentantePorContraparteRepository hcRepresentantePorContraparteRepository, ClienteLogRepository clienteLogRepository) {
        this.clienteRepository = clienteRepository;
        this.tipoClienteRepository = tipoClienteRepository;
        this.direccionRepository = direccionRepository;
        this.hcRepresentantePorContraparteRepository = hcRepresentantePorContraparteRepository;
        this.clienteLogRepository = clienteLogRepository;
    }

    @Override
    public ResponseEntity<Cliente> buscarCliente(Integer idCliente) {
        try {
            Cliente cliente = clienteRepository.findById(idCliente);
            List<Direccion> direccionList = direccionRepository.obtenerDireccionesPorCliente(cliente.getId());
            cliente.setDirecciones(direccionList);
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<Cliente>> listarCliente() {
        try {
            List<Cliente> clientes = clienteRepository.listarClientes();
            return new ResponseEntity<>(clientes, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public ResponseEntity<List<Cliente>> listarCliente(BuscarClienteFilter filter) {
        try {
            LOGGER.info(filter);
            List<Cliente> clientes = clienteRepository.listarClientes(filter.getIdTipo(), filter.getFiltroRazonSocial(), filter.getFiltroCorreo(), filter.getFiltroNumDocumento());
            return new ResponseEntity<>(clientes, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public ResponseEntity<List<TipoCliente>> obtenerTiposActivos() {
        try {
            List<TipoCliente> tipoClienteList = tipoClienteRepository.obtenerTipoClientePorEstado(Constantes.TIPO_CLIENTE_ACTIVO);
            return new ResponseEntity<>(tipoClienteList, HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public ResponseEntity<VerificacionClienteBean> verificarExistenciaCliente(String dni){
        VerificacionClienteBean verificacionClienteBean = new VerificacionClienteBean();
        try{
            Cliente clientesExistence = clienteRepository.buscarClienteDocumentoIdentificacion(dni, Constantes.ESTADO_INACTIVO);
            if(clientesExistence != null){
                verificacionClienteBean.setExiste(true);
                verificacionClienteBean.setCliente(clientesExistence);
                verificacionClienteBean.setMessage("Existe este cliente con dni");

            } else{
                verificacionClienteBean.setExiste(false);
                verificacionClienteBean.setMessage("No existe dni");
            }
            return new ResponseEntity<>(verificacionClienteBean, HttpStatus.OK);
        } catch (Exception ex){
            verificacionClienteBean.setExiste(true);
            verificacionClienteBean.setMessage("Error en la petición HTTP");
            return new ResponseEntity<>(verificacionClienteBean, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<VerificacionClienteBean> verificarExistenciaClienteContraparte(String dni){
        VerificacionClienteBean verificacionClienteBean = new VerificacionClienteBean();
        try{
            Cliente clientesExistence = clienteRepository.buscarClienteContraparteDocumentoIdentificacion(dni, Constantes.ESTADO_INACTIVO);
            if(clientesExistence != null){
                verificacionClienteBean.setExiste(true);
                verificacionClienteBean.setCliente(clientesExistence);
                verificacionClienteBean.setMessage("Existe este cliente con dni");

            } else{
                verificacionClienteBean.setExiste(false);
                verificacionClienteBean.setMessage("No existe dni");
            }
            return new ResponseEntity<>(verificacionClienteBean, HttpStatus.OK);
        } catch (Exception ex){
            verificacionClienteBean.setExiste(true);
            verificacionClienteBean.setMessage("Error en la petición HTTP");
            return new ResponseEntity<>(verificacionClienteBean, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public ResponseModel guardarCliente(ClienteBean clienteBean, Integer idUsuario) {
        LOGGER.info(clienteBean);
        ResponseModel responseModel = new ResponseModel();
        Cliente cliente = new Cliente();

        if (clienteBean.getEstado() == null) {
            cliente.setEstado(Constantes.ESTADO_INACTIVO);
        }
        if (clienteBean.getIdCliente() == null) {
            TipoCliente tipoCliente = tipoClienteRepository.findById(clienteBean.getTipoCliente());

            cliente.setNombre(clienteBean.getNombre());
            cliente.setApellidoPaterno(clienteBean.getApellidoPaterno());
            cliente.setApellidoMaterno(clienteBean.getApellidoMaterno());
            cliente.setRazonSocial(clienteBean.getRazonSocial());
            cliente.setTipo(tipoCliente);
            cliente.setNumeroIdentificacion(clienteBean.getNumeroIdentificacion());
            cliente.setTelefono(clienteBean.getTelefono());
            cliente.setEstado(clienteBean.getEstado());
            cliente.setEsContraparte(clienteBean.getEsContraparte());
            cliente.setEsRepresentante(clienteBean.getEsRepresentante());
            cliente.setSituacionSunat(clienteBean.getSituacionSunat());
            cliente.setCorreo(clienteBean.getCorreo());
            cliente.setFechaCreacion(new Date());
            cliente.setContacto(clienteBean.getContacto());
            cliente.setUsuarioCreacion(idUsuario);

            //Cambios modificados por wilder, agregando direccion y correo
            cliente.setDireccion(clienteBean.getDireccion());
            cliente.setContacto(clienteBean.getContacto());

            cliente = clienteRepository.save(cliente);

            ClienteLog clienteLog = new ClienteLog();
            clienteLog.setIdCliente(cliente.getId());
            clienteLog.setDireccion(cliente.getDireccion());
            clienteLog.setContacto(cliente.getContacto());
            clienteLog.setCorreo(cliente.getCorreo());

            clienteLog.setTelefono(cliente.getTelefono());
            clienteLog.setUsuarioCreacion(idUsuario);
            clienteLog.setFechaCreacion(new Date());
            clienteLogRepository.save(clienteLog);
            if (cliente.getTipo().getId() == 6 || cliente.getTipo().getId() == 7) {
                if (cliente.getEsContraparte() && cliente.getEsRepresentante()) {

                    HcRepresentantePorContrapartePK rpcId = new HcRepresentantePorContrapartePK();

                    rpcId.setContraparte(cliente);
                    rpcId.setRepresentante(cliente);

                    HcRepresentantePorContraparte repPorCont = new HcRepresentantePorContraparte();
                    repPorCont.setId(rpcId);

                    hcRepresentantePorContraparteRepository.save(repPorCont);
                }
            }


            responseModel.setId(cliente.getId());
            responseModel.setMessage("Cliente registrado");

        } else {
            Cliente existente = clienteRepository.findById(clienteBean.getIdCliente());

            if (clienteBean.getTipoCliente() == 6 || clienteBean.getTipoCliente() == 7) {
                if (existente.getEsContraparte() && existente.getEsRepresentante()) {
                    if (!(existente.getEsContraparte().equals(clienteBean.getEsContraparte())) || !(existente.getEsRepresentante().equals(clienteBean.getEsRepresentante()))) {

                        HcRepresentantePorContrapartePK rpcId = new HcRepresentantePorContrapartePK();
                        rpcId.setContraparte(existente);
                        rpcId.setRepresentante(existente);
                        HcRepresentantePorContraparte repPorCont = new HcRepresentantePorContraparte();
                        repPorCont.setId(rpcId);

                        //hcRepresentantePorContraparteRepository.delete(existente.getId());
                        hcRepresentantePorContraparteRepository.delete(repPorCont);
                    }
                } else {
                    if (clienteBean.getEsContraparte() && clienteBean.getEsRepresentante()) {

                        HcRepresentantePorContrapartePK rpcId = new HcRepresentantePorContrapartePK();
                        rpcId.setContraparte(existente);
                        rpcId.setRepresentante(existente);
                        HcRepresentantePorContraparte repPorCont = new HcRepresentantePorContraparte();
                        repPorCont.setId(rpcId);

                        hcRepresentantePorContraparteRepository.save(repPorCont);
                    }
                }
            }

            TipoCliente tipoCliente = tipoClienteRepository.findById(clienteBean.getTipoCliente());

            existente.setNombre(clienteBean.getNombre());
            existente.setApellidoPaterno(clienteBean.getApellidoPaterno());
            existente.setApellidoMaterno(clienteBean.getApellidoMaterno());
            existente.setRazonSocial(clienteBean.getRazonSocial());
            existente.setTipo(tipoCliente);
            existente.setNumeroIdentificacion(clienteBean.getNumeroIdentificacion());
            existente.setTelefono(clienteBean.getTelefono());
            existente.setEstado(clienteBean.getEstado());
            existente.setEsContraparte(clienteBean.getEsContraparte());
            existente.setEsRepresentante(clienteBean.getEsRepresentante());
            existente.setSituacionSunat(clienteBean.getSituacionSunat());
            existente.setCorreo(clienteBean.getCorreo());
            existente.setDireccion(clienteBean.getDireccion());
            existente.setFechaModificacion(new Date());
            existente.setUsuarioModificacion(idUsuario);
            existente.setContacto(clienteBean.getContacto());

            clienteRepository.save(existente);
            ClienteLog clienteLog = new ClienteLog();
            clienteLog.setIdCliente(existente.getId());
            clienteLog.setDireccion(existente.getDireccion());
            clienteLog.setContacto(existente.getContacto());
            clienteLog.setCorreo(existente.getCorreo());
            clienteLog.setTelefono(existente.getTelefono());
            clienteLog.setUsuarioCreacion(idUsuario);
            clienteLog.setFechaCreacion(new Date());
            clienteLogRepository.save(clienteLog);


            responseModel.setId(existente.getId());
            responseModel.setMessage("Cliente actualizado");
        }
        responseModel.setHttpSatus(HttpStatus.OK);

        return responseModel;
    }

}
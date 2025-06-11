package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.CompraTransaccionBean;
import com.hochschild.speed.back.model.domain.speed.HcCompraTransaccion;
import com.hochschild.speed.back.model.domain.speed.HcMovimientoTransaccion;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.CompraTransaccionRepository;
import com.hochschild.speed.back.repository.speed.HcMovimientoTransaccionRepository;
import com.hochschild.speed.back.repository.speed.ParametroRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.service.CompraTransaccionService;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("compraTransaccionService")
public class CompraTransaccionServiceImpl implements CompraTransaccionService {
    private static final Logger LOGGER = Logger.getLogger(CompraTransaccionServiceImpl.class.getName());
    private final CompraTransaccionRepository compraTransaccionRepository;
    private final UsuarioRepository usuarioRepository;

    private final ParametroRepository parametroRepository;

    private final HcMovimientoTransaccionRepository hcMovimientoTransaccionRepository;

    public CompraTransaccionServiceImpl(CompraTransaccionRepository compraTransaccionRepository, UsuarioRepository usuarioRepository, ParametroRepository parametroRepository, HcMovimientoTransaccionRepository hcMovimientoTransaccionRepository) {
        this.compraTransaccionRepository = compraTransaccionRepository;
        this.usuarioRepository = usuarioRepository;
        this.parametroRepository=parametroRepository;
        this.hcMovimientoTransaccionRepository=hcMovimientoTransaccionRepository;
    }

    @Override
    public List<CompraTransaccionBean> listarByEstado(String descripcion) {
        List<HcCompraTransaccion> results = compraTransaccionRepository.findByDescripcion(descripcion);
        List<CompraTransaccionBean> listCompras = results.stream().map(model -> {
            CompraTransaccionBean bean = new CompraTransaccionBean();
            bean.setId(model.getId());
            bean.setDescripcion(model.getDescripcion());
            bean.setCantidad(model.getCantidadTransaccion());
            bean.setCosto(model.getCostoTransaccion());
            bean.setEstado(model.getEstado());
            return bean;
        }).collect(Collectors.toList());

        return listCompras;
    }

    @Override
    public ResponseModel guardar(CompraTransaccionBean bean, Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario);
        ResponseModel responseModel = new ResponseModel();
        try{
            HcCompraTransaccion model = null;
            HcMovimientoTransaccion movimiento = null;
            if(bean.getId() != null){
                model = compraTransaccionRepository.findOne(bean.getId());
                model.setId(bean.getId());
                model.setUsuarioModificacion((usuario.getUsuario()));
                model.setFechaModificacion(new Date());
                responseModel.setHttpSatus(HttpStatus.OK);

                movimiento = hcMovimientoTransaccionRepository.findByCodigoRegistro(bean.getId());
            }else{
                model = new HcCompraTransaccion();
                movimiento = new HcMovimientoTransaccion();
                model.setUsuarioCreacion(usuario.getUsuario());
                model.setFechaCreacion(new Date());
                responseModel.setHttpSatus(HttpStatus.CREATED);
            }
            model.setDescripcion(bean.getDescripcion());
            model.setCantidadTransaccion(bean.getCantidad());
            model.setCostoTransaccion(bean.getCosto());
            model.setEstado(String.valueOf(Constantes.ESTADO_ACTIVO));

            //Movimiento

            compraTransaccionRepository.save(model);
            responseModel.setMessage("Exito");

            //Movimiento
            if(movimiento!=null){
                movimiento.setCodigoRegistro(model.getId());
                movimiento.setTipoMovimiento(parametroRepository.findById(Constantes.ID_TIPO_MOVIMIENTO_TX_INVENTARIO_COMPRA));
                movimiento.setTipoRegistro(parametroRepository.findById(Constantes.ID_TIPO_REGISTRO_TX_INGRESO));
                movimiento.setCantidad(bean.getCantidad());
                movimiento.setFechaMovimiento(new Date());
                movimiento.setUsuarioCreacion(idUsuario);
                movimiento.setFechaCreacion(new Date());
            }

            hcMovimientoTransaccionRepository.save(movimiento);

        }catch(Exception ex){
            LOGGER.error(ex.getMessage(), ex);
            responseModel.setMessage("Error");
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseModel;
    }

    @Override
    public ResponseModel eliminar(Integer idModel, Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario);
        ResponseModel responseModel = new ResponseModel();
        try{
            HcCompraTransaccion model = compraTransaccionRepository.findOne(idModel);
            model.setUsuarioModificacion((usuario.getUsuario()));
            model.setFechaModificacion(new Date());
            model.setEstado(String.valueOf(Constantes.ESTADO_INACTIVO));
            compraTransaccionRepository.save(model);
            HcMovimientoTransaccion movimiento = hcMovimientoTransaccionRepository.findByCodigoRegistro(idModel);

            if(movimiento!=null){
                hcMovimientoTransaccionRepository.delete(movimiento);
            }

            responseModel.setMessage("Exito");
            responseModel.setHttpSatus(HttpStatus.CREATED);
        }catch(Exception ex){
            LOGGER.error(ex.getMessage(), ex);
            responseModel.setMessage("Error");
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseModel;
    }
}

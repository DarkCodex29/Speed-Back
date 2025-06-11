package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.bandejaEntrada.AlarmaBean;
import com.hochschild.speed.back.model.bean.bandejaEntrada.BandejaAlarmaBean;
import com.hochschild.speed.back.model.bean.bandejaEntrada.NombresIdBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.HcAlarmaRepository;
import com.hochschild.speed.back.repository.speed.HcDestinatarioAlarmaRepository;
import com.hochschild.speed.back.repository.speed.HcGrupoRepository;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.service.CommonBusinessLogicService;
import com.hochschild.speed.back.service.MantenimientoAlarmaService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class MantenimientoAlarmaServiceImpl implements MantenimientoAlarmaService {
    private static final Logger LOGGER = Logger.getLogger(MantenimientoAlarmaServiceImpl.class.getName());
    private final UsuarioRepository usuarioRepository;
    private final HcDestinatarioAlarmaRepository hcDestinatarioAlarmaRepository;
    private final HcGrupoRepository hcGrupoRepository;
    private final HcAlarmaRepository hcAlarmaRepository;
    private final CommonBusinessLogicService commonBusinessLogicService;

    public MantenimientoAlarmaServiceImpl(UsuarioRepository usuarioRepository,
                                          HcAlarmaRepository hcAlarmaRepository,
                                          HcDestinatarioAlarmaRepository hcDestinatarioAlarmaRepository,
                                          HcGrupoRepository hcGrupoRepository,
                                          CommonBusinessLogicService commonBusinessLogicService) {
        this.usuarioRepository = usuarioRepository;
        this.hcAlarmaRepository = hcAlarmaRepository;
        this.hcDestinatarioAlarmaRepository = hcDestinatarioAlarmaRepository;
        this.hcGrupoRepository = hcGrupoRepository;
        this.commonBusinessLogicService = commonBusinessLogicService;
    }

    @Override
    public BandejaAlarmaBean bandejaAlarma(Integer idUsuario, Integer idExpediente) {

        BandejaAlarmaBean bandejaAlarmaBean = new BandejaAlarmaBean();

        try {
            Usuario usuario = usuarioRepository.findById(idUsuario);

            if (usuario != null) {
                HcDocumentoLegal documentoLegal = commonBusinessLogicService.obtenerHcDocumentoLegalPorExpediente(idExpediente);
                LOGGER.info("En bandeja de alarmas con id " + documentoLegal.getId());
                List<AlarmaBean> alarmas = obtenerAlarmasBean(documentoLegal.getId());
                bandejaAlarmaBean.setAlarmas(alarmas);
                bandejaAlarmaBean.setUsuario(usuario);
                bandejaAlarmaBean.setIdDocumentoLegal(documentoLegal.getId());

                return bandejaAlarmaBean;
            }
        } catch (Exception ex) {
            return null;
        }

        return bandejaAlarmaBean;
    }

    @Override
    public ResponseModel guardarAlarma(AlarmaBean alarmaBean) {

        ResponseModel responseModel = new ResponseModel();

        try {

            HcAlarma alarma = alarmaBean.getId() != null ? hcAlarmaRepository.findOne(alarmaBean.getId()) : new HcAlarma();
            HcDocumentoLegal dl = new HcDocumentoLegal();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            dl.setId(alarmaBean.getIdDocumentoLegal());
            alarma.setDocumentoLegal(dl);
            alarma.setFechaAlarma(formatter.parse(alarmaBean.getFechaAlarma()));
            alarma.setDias_activacion(alarmaBean.getActivacion());
            alarma.setDias_intervalo(alarmaBean.getIntervalo());
            alarma.setTitulo(alarmaBean.getTitulo());
            alarma.setMensaje(alarmaBean.getMensaje());
            alarma.setEstado(alarmaBean.getEstado() != null ? alarmaBean.getEstado() : 'A');
            alarma.setAnual(alarmaBean.getAnual());
            guardarAlarmaVisados(alarma, alarmaBean.getIdVisadores(), alarmaBean.getEsGrupo());

            responseModel.setMessage("Alarma guardada exitosamente");
            responseModel.setHttpSatus(HttpStatus.OK);

        } catch (Exception ex) {
            responseModel.setMessage(ex.getMessage());
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseModel;
    }

    @Override
    public List<AlarmaBean> obtenerAlarmasBean(Integer idDocumentoLegal) {

        List<AlarmaBean> result = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        List<HcAlarma> alarmas = obtenerAlarmas(idDocumentoLegal);

        for (HcAlarma alarma : alarmas) {
            AlarmaBean alarmaBean = new AlarmaBean();
            alarmaBean.setId(alarma.getId());
            alarmaBean.setIdDocumentoLegal(idDocumentoLegal);
            alarmaBean.setFechaAlarma(formatter.format(alarma.getFechaAlarma()));
            alarmaBean.setActivacion(alarma.getDias_activacion());
            alarmaBean.setIntervalo(alarma.getDias_intervalo());
            alarmaBean.setTitulo(alarma.getTitulo());
            alarmaBean.setMensaje(alarma.getMensaje());
            alarmaBean.setAnual(alarma.getAnual());
            alarmaBean.setEstado(alarma.getEstado());

            List<HcDestinatarioAlarma> destinatarios = hcDestinatarioAlarmaRepository.obtenerDestinatariosAlarma(alarma.getId());
            List<Integer> idVisadores = new ArrayList<>();
            List<Integer> esGrupo = new ArrayList<>();
            List<NombresIdBean> namesIdVisadores = new ArrayList<>();
            List<NombresIdBean> namesIdGrupo = new ArrayList<>();
            for (HcDestinatarioAlarma destinatario : destinatarios) {
                if (destinatario.getGrupo() != null) {
                    esGrupo.add(destinatario.getGrupo().getId());
                    namesIdGrupo.add(new NombresIdBean(destinatario.getGrupo().getId(), destinatario.getGrupo().getNombre()));
                } else {
                    idVisadores.add(destinatario.getUsuario().getId());
                    namesIdVisadores.add(new NombresIdBean(destinatario.getUsuario().getId(), destinatario.getUsuario().getNombreCompleto()));
                }
            }

            alarmaBean.setEsGrupo(esGrupo.toArray(new Integer[esGrupo.size()]));
            alarmaBean.setIdVisadores(idVisadores.toArray(new Integer[idVisadores.size()]));
            alarmaBean.setNamesIdGrupo(namesIdGrupo.toArray(new NombresIdBean[namesIdGrupo.size()]));
            alarmaBean.setNamesIdVisadores(namesIdVisadores.toArray(new NombresIdBean[namesIdVisadores.size()]));


            result.add(alarmaBean);
        }

        return result;
    }


    @Override
    public List<HcAlarma> obtenerAlarmas(Integer idDocumentoLegal) {
        return hcAlarmaRepository.obtenerAlarmas(idDocumentoLegal);
    }

    @Override
    public void guardarAlarmaVisados(HcAlarma alarma, Integer[] idVisadores, Integer[] esGrupo) {

        hcAlarmaRepository.save(alarma);

        for (int i = 0; i < idVisadores.length; i++) {
            if (esGrupo[i] == 0) {//es un usuario
                Usuario visador = usuarioRepository.findById(idVisadores[i]);
                List<Integer> olds = hcDestinatarioAlarmaRepository.findByIdUsuarioaAndIdAlarma(visador.getId(), alarma.getId());
                if (olds.size() > 0) {
                    for (Integer old : olds) {
                        hcDestinatarioAlarmaRepository.delete(old);
                    }
                }

                HcDestinatarioAlarma destAlarma = new HcDestinatarioAlarma();
                destAlarma.setUsuario(visador);
                destAlarma.setAlarma(alarma);
                hcDestinatarioAlarmaRepository.save(destAlarma);
            } else {//es grupo
                HcGrupo grupo = hcGrupoRepository.findById(esGrupo[i]);
                List<Integer> olds = hcDestinatarioAlarmaRepository.findByIdUsuarioaAndIdAlarma(grupo.getId(), alarma.getId());
                if (olds.size() > 0) {
                    for (Integer old : olds) {
                        hcDestinatarioAlarmaRepository.delete(old);
                    }
                }

                HcDestinatarioAlarma destAlarma = new HcDestinatarioAlarma();
                destAlarma.setGrupo(grupo);
                destAlarma.setAlarma(alarma);

                hcDestinatarioAlarmaRepository.save(destAlarma);
            }
        }
    }

    @Override
    public ResponseModel eliminarAlarma(Integer idUsuario, Integer idAlarma) {

        ResponseModel responseModel = new ResponseModel();

        try {
            Usuario usuario = usuarioRepository.findById(idUsuario);
            if (usuario != null) {
                LOGGER.info("id alarma " + idAlarma);
                hcAlarmaRepository.delete(idAlarma);
                responseModel.setMessage("Alarma eliminada");
                responseModel.setHttpSatus(HttpStatus.OK);
            }
        } catch (Exception ex) {
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            responseModel.setMessage(ex.getMessage());
        }
        return responseModel;
    }
}

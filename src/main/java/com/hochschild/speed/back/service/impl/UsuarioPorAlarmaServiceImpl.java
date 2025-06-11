package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.usuariosPorAlarma.GrupoBean;
import com.hochschild.speed.back.model.bean.usuariosPorAlarma.UsuarioBean;
import com.hochschild.speed.back.model.bean.usuariosPorAlarma.UsuariosAlarmaBean;
import com.hochschild.speed.back.model.domain.speed.HcDestinatarioAlarma;
import com.hochschild.speed.back.repository.speed.HcDestinatarioAlarmaRepository;
import com.hochschild.speed.back.service.UsuarioPorAlarmaService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioPorAlarmaServiceImpl implements UsuarioPorAlarmaService {
    private static final Logger LOGGER = Logger.getLogger(UsuarioPorAlarmaServiceImpl.class.getName());
    private final HcDestinatarioAlarmaRepository hcDestinatarioAlarmaRepository;

    public UsuarioPorAlarmaServiceImpl(HcDestinatarioAlarmaRepository hcDestinatarioAlarmaRepository) {
        this.hcDestinatarioAlarmaRepository = hcDestinatarioAlarmaRepository;
    }

    @Override
    public List<UsuariosAlarmaBean> obtenerUsuariosPorAlarma(Integer idAlarma) {

        List<UsuariosAlarmaBean> listado = new ArrayList<>();
        List<HcDestinatarioAlarma> usuariosPorAlarma = new ArrayList<>();

        try {
            usuariosPorAlarma = hcDestinatarioAlarmaRepository.findByIdAlarma(idAlarma);

            for (HcDestinatarioAlarma hcDestinatarioAlarma :usuariosPorAlarma) {
                UsuariosAlarmaBean usuariosAlarmaBean = new UsuariosAlarmaBean();
                UsuarioBean usuarioBean = null;
                GrupoBean grupoBean =  null;

                if (hcDestinatarioAlarma.getUsuario() != null){
                    usuarioBean = new UsuarioBean();
                    usuarioBean.setIdUsuario(hcDestinatarioAlarma.getUsuario().getId());
                    usuarioBean.setNombreCompleto(hcDestinatarioAlarma.getUsuario().getNombreCompleto());
                }

                if (hcDestinatarioAlarma.getGrupo() != null){
                    grupoBean = new GrupoBean();
                    grupoBean.setIdGrupo(hcDestinatarioAlarma.getGrupo().getId());
                    grupoBean.setNombre(hcDestinatarioAlarma.getGrupo().getNombre());
                }
                usuariosAlarmaBean.setUsuario(usuarioBean);
                usuariosAlarmaBean.setGrupo(grupoBean);
                listado.add(usuariosAlarmaBean);
            }

        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
        }
        return listado;
    }
}

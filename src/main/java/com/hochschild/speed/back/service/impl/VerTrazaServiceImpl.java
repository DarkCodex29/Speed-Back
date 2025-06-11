package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.bean.verTraza.HistorialTrazaBean;
import com.hochschild.speed.back.model.bean.verTraza.TrazaBean;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.repository.speed.ExpedienteRepository;
import com.hochschild.speed.back.repository.speed.FeriadoRepository;
import com.hochschild.speed.back.repository.speed.RolRepository;
import com.hochschild.speed.back.repository.speed.TrazaRepository;
import com.hochschild.speed.back.service.VerTrazaService;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("verTrazaService")
public class VerTrazaServiceImpl implements VerTrazaService {
    private static final Logger LOGGER = Logger.getLogger(VerTrazaServiceImpl.class.getName());

    private final ExpedienteRepository expedienteRepository;
    private final FeriadoRepository feriadoRepository;
    private final TrazaRepository trazaRepository;
    private final RolRepository rolRepository;

    @Autowired
    public VerTrazaServiceImpl(
            ExpedienteRepository expedienteRepository,
            FeriadoRepository feriadoRepository,
            TrazaRepository trazaRepository,
            RolRepository rolRepository
    ) {
        this.expedienteRepository = expedienteRepository;
        this.feriadoRepository = feriadoRepository;
        this.trazaRepository = trazaRepository;
        this.rolRepository = rolRepository;
    }

    @Override
    public Date obtenerFechaLimite(Expediente expediente) {
        try {
            if (expediente.getProceso().getPlazo() != null && expediente.getProceso().getPlazo() > 0) {
                return DateUtil.sumarDiasUtiles(expediente.getFechaCreacion(), expediente.getProceso().getPlazo(), expediente.getCreador().getArea().getSede(), feriadoRepository);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public HistorialTrazaBean obtenerHistoriaTrazasPorExpediente(Integer idExpediente) {
        HistorialTrazaBean historialTrazaBean = new HistorialTrazaBean();

        try {
            Expediente expediente = expedienteRepository.findById(idExpediente);
            Date fechaLimite = obtenerFechaLimite(expediente);
            historialTrazaBean.setNumero(expediente.getDocumentoLegal().getNumero());
            historialTrazaBean.setFechaLimite(DateUtil.convertDateToString(fechaLimite, DateUtil.FORMAT_DATE));
            List<Traza> trazas = trazaRepository.obtenerTrazaPorExpediente(idExpediente);
            List<TrazaBean> trazasBean = new ArrayList<>();
            for (Traza traza : trazas) {
                TrazaBean trazaBean = new TrazaBean();
                // Recorro las trazas, si es copia obtengo los hijos
                if (traza.getAccion().equals(Constantes.ACCION_REGISTRAR_COPIA)) {
                    trazaBean.setId(traza.getId());
                    trazaBean.setFechaRecepcion(DateUtil.convertDateToString(traza.getFechaCreacion(), DateUtil.FORMAT_DATE));
                    trazaBean.setAccionSeleccionada(traza.getAccion());
                    trazaBean.setDestinatarios("Derivación Paralela");
                    trazaBean.setMultiple(traza.getPadre() != null);
                } else {
                    // Obtengo el proceso del expediente
                    List<Object[]> listTmp = trazaRepository.obtenerDestinatarios(traza.getId(), true);
                    if (listTmp != null && !listTmp.isEmpty()) {
                        UsuarioPorTraza upt = (UsuarioPorTraza) listTmp.get(0)[1];
                        List<Usuario> listUsu = new ArrayList<Usuario>();
                        listUsu.add((Usuario) listTmp.get(0)[0]);
                        traza.setDestinatarios(listUsu);
                        if (upt.getRol() != null) {
                            trazaBean.setDestinatarios("Rol: " + obtenerRol(upt.getRol().getId()).getNombre());
                        } else {
                            String mensaje = traza.getDestinatarios().get(0).getNombres() + " " + traza.getDestinatarios().get(0).getApellidos();
                            mensaje += traza.getDestinatarios().get(0).getArea() != null ? "<br/> <strong>Area:</strong> " + traza.getDestinatarios().get(0).getArea().getNombre() : "";
                            trazaBean.setDestinatarios(mensaje);
                        }
                    }
                    trazaBean.setId(traza.getId());
                    trazaBean.setFechaRecepcion(DateUtil.convertDateToString(traza.getFechaCreacion(), DateUtil.FORMAT_DATE));
                    trazaBean.setAccionSeleccionada(traza.getAccion());
                    trazaBean.setAccion(traza.getAccion());
                    if (traza.getFechaLimite() != null) {
                        trazaBean.setFechaLimite(DateUtil.convertDateToString(traza.getFechaLimite(), DateUtil.FORMAT_DATE));
                    }
                    trazaBean.setProceso(traza.getProceso() != null ? traza.getProceso().getNombre() : "--");
                    trazaBean.setActividad(traza.getActividad());
                    trazaBean.setRemitente(procesarRemitente(traza));
                    trazaBean.setObservaciones(traza.getObservacion());
                    trazaBean.setMultiple(traza.getPadre() != null);
                }
                trazasBean.add(trazaBean);
            }
            historialTrazaBean.setHistorial(trazasBean);
            historialTrazaBean.setHttpSatus(HttpStatus.OK);
            historialTrazaBean.setMessage("exito");
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            historialTrazaBean.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
            historialTrazaBean.setMessage("error");
        }

        return historialTrazaBean;
    }

    @Override
    public List<Traza> obtenerPorExpediente(int idExpediente) {
        List<Traza> trazas = trazaRepository.obtenerTrazaPorExpediente(idExpediente);
        return trazas;
    }

    @Override
    public Rol obtenerRol(Integer id) {
        return rolRepository.findById(id);
    }

    private String procesarRemitente(Traza traza) {
        String salida = traza.getRemitente().getNombres() + " " + traza.getRemitente().getApellidos();
        if (traza.getRemitente().getArea() != null) {
            salida += "<br/><strong>&Aacute;rea:</strong> " + traza.getRemitente().getArea().getNombre();
        }
        if (traza.getReemplazado() != null) {
            salida += "<hr/><strong>En reemplazo de:</strong><br/>" + traza.getReemplazado().getLabel();
        }
        return salida;
    }
}

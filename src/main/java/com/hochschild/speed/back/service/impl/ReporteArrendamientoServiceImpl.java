package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.CydocConfig;
import com.hochschild.speed.back.dao.HcDocumentoLegalDao;
import com.hochschild.speed.back.dao.HcUbicacionPorDocumentoDao;
import com.hochschild.speed.back.dao.OpcionSeguridadDao;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.reporte.ArrendamientoReporteBean;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.service.ReporteArrendamientoService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReporteArrendamientoServiceImpl implements ReporteArrendamientoService {
    private final CydocConfig config;
    private final UsuarioRepository usuarioRepository;
    private final HcDocumentoLegalDao hcDocumentoLegalDao;

    private final HcUbicacionPorDocumentoDao hcUbicacionPorDocumentoDao;

    private final OpcionSeguridadDao opcionSeguridadDao;

    @Autowired
    public ReporteArrendamientoServiceImpl(CydocConfig config, HcDocumentoLegalDao hcDocumentoLegalDao, UsuarioRepository usuarioRepository, OpcionSeguridadDao opcionSeguridadDao, HcUbicacionPorDocumentoDao HcUbicacionPorDocumentoDao) {
        this.config = config;
        this.hcDocumentoLegalDao = hcDocumentoLegalDao;
        this.usuarioRepository = usuarioRepository;
        this.opcionSeguridadDao = opcionSeguridadDao;
        this.hcUbicacionPorDocumentoDao = HcUbicacionPorDocumentoDao;
    }

    @Override
    public List<ArrendamientoReporteBean> buscarDocumentosLegales(String numero, Date fechaFirmaInicio, Date fechaFirmaFin, Date fechaVencimientoInicio, Date fechaVencimientoFin, Integer idPais, Integer idCompania, Integer idArea, String tipoUbicacion, Integer idUbicacion, Integer idContraparte, Double montoDesde, Double montoHasta, Integer idTipoContrato, Character estado, List<String> valoresORG, boolean datosAdicionales, Integer idUser) {
        List<String> numeroExpedientes = null;
        Usuario usuario = usuarioRepository.findById(idUser);
        valoresORG = opcionSeguridadDao.getValoresOrganizacionales(1, usuario.getUsuario());
        /**
         * Normalizamos campos
         * --------------------------------------------------------------------------------
         */
        numero = hcDocumentoLegalDao.normalizarTerminoBusqueda(numero);

        fechaFirmaInicio = DateUtil.minimizarFecha(fechaFirmaInicio);
        fechaFirmaFin = DateUtil.maximizarFecha(fechaFirmaFin);
        fechaVencimientoInicio = DateUtil.minimizarFecha(fechaVencimientoInicio);
        fechaVencimientoFin = DateUtil.maximizarFecha(fechaVencimientoFin);

        idPais = hcDocumentoLegalDao.normalizarIdBusqueda(idPais);
        idCompania = hcDocumentoLegalDao.normalizarIdBusqueda(idCompania);
        idArea = hcDocumentoLegalDao.normalizarIdBusqueda(idArea);
        idUbicacion = hcDocumentoLegalDao.normalizarIdBusqueda(idUbicacion);

        tipoUbicacion = !tipoUbicacion.equals("0") ? tipoUbicacion : null;

        //No se modifica el texto a buscar en Alfresco
        idContraparte = hcDocumentoLegalDao.normalizarIdBusqueda(idContraparte);

        montoDesde = hcDocumentoLegalDao.normalizarMonto(montoDesde);
        montoHasta = hcDocumentoLegalDao.normalizarMonto(montoHasta);

        idTipoContrato = hcDocumentoLegalDao.normalizarIdBusqueda(idTipoContrato);

        estado = !estado.equals('0') ? estado : null;

        /**
         * Buscamos en Alfresco (Si es el
         * caso)-----------------------------------------------------------------
         */
        List<ArrendamientoReporteBean> resultado = hcDocumentoLegalDao.buscarDocumentosLegalesArrendamiento(numero,
                fechaFirmaInicio,
                fechaFirmaFin,
                fechaVencimientoInicio,
                fechaVencimientoFin,
                idPais,
                idCompania,
                idArea,
                tipoUbicacion,
                idUbicacion,
                idContraparte,
                montoDesde,
                montoHasta,
                idTipoContrato,
                estado,
                numeroExpedientes,
                datosAdicionales,
                true);
        System.out.println("LENGHT QUERY : " + resultado.size());

        boolean integracionSCA = config.getScaActivo();
        if (!resultado.isEmpty()) {
            if (valoresORG != null && !valoresORG.isEmpty()) {
                resultado = filtrarPorVO(resultado, valoresORG);
                resultado = completarOrdenar(resultado, integracionSCA);
            } else {

                if (!integracionSCA) {
                    resultado = completarOrdenar(resultado, integracionSCA);
                } else {
                    resultado = null;
                }
            }
        }
        return resultado;
    }


    private List<ArrendamientoReporteBean> filtrarPorVO(List<ArrendamientoReporteBean> lista, List<String> valoresORG) {
        if (lista != null && !lista.isEmpty()) {
            for (ArrendamientoReporteBean fila : lista) {
                fila.setPuedeVisualizar(validarPorVO(fila, valoresORG));
            }
        }

        return lista;
    }

    private boolean validarPorVO(ArrendamientoReporteBean datos, List<String> valoresORG) {
        if (valoresORG != null) {
            for (int q = 0; q < valoresORG.size(); q++) {
                String filaVO = valoresORG.get(q);
                String[] vo = filaVO.split(",");

                String Pais = new String(vo[0]);
                String Compania = new String(vo[1]);
                String UMOficina = new String(vo[2]);
                String Area = new String(vo[3]);

                if (Pais.equals("*")) {
                    return true;
                } else {
                    if (Compania.equals("*")) {
                        if (Pais.equals(datos.getVoPais())) {
                            return true;
                        } else {
                            if (valoresORG.size() == q) {
                                return false;
                            }
                        }
                    } else {
                        if (Area.equals("*")) {
                            if (Pais.equals(datos.getVoPais()) && Compania.equals(datos.getVoCompania())) {
                                return true;
                            } else {
                                if (valoresORG.size() == q) {
                                    return false;
                                }
                            }
                        } else {
                            if (UMOficina.equals("*")) {
                                if (Pais.equals(datos.getVoPais()) && Compania.equals(datos.getVoCompania()) && Area.equals(datos.getVoArea())) {
                                    return true;
                                } else {
                                    if (valoresORG.size() == q) {
                                        return false;
                                    }
                                }
                            } else {
                                if (Pais.equals(datos.getVoPais()) && Compania.equals(datos.getVoCompania()) && Area.equals(datos.getVoArea()) && hcUbicacionPorDocumentoDao.documentoContieneUbicacion(datos.getIdDocumentoLegal(), UMOficina)) {
                                    return true;
                                } else {
                                    if (valoresORG.size() == q) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private List<ArrendamientoReporteBean> completarOrdenar(List<ArrendamientoReporteBean> lista, boolean integracionSCA) {
        List<ArrendamientoReporteBean> salida = new ArrayList<>();
        List<ArrendamientoReporteBean> sinNumero = new ArrayList<ArrendamientoReporteBean>();

        if (lista != null && !lista.isEmpty()) {
            for (ArrendamientoReporteBean fila : lista) {
                if (!integracionSCA) {
                    fila.setPuedeVisualizar(true);
                }

                fila.setUbicacion(hcUbicacionPorDocumentoDao.obtenerUbicacionesReporte(fila.getIdDocumentoLegal()));
                fila.setEstado(AppUtil.getNombreEstadoDL(fila.getEst_codigo()));

                fila.setFechaInicio(hcDocumentoLegalDao.obtenerFechaInicio(fila.getIdDocumentoLegal()));
                fila.setFechaVencimiento(hcDocumentoLegalDao.obtenerFechaVencimiento(fila.getIdDocumentoLegal()));

                if (fila.getFlMonto() != null) {
                    DecimalFormat df = new DecimalFormat("##.##");
                    df.setRoundingMode(RoundingMode.DOWN);

                    fila.setMonto(df.format(fila.getFlMonto()));
                }

                if (fila.getCnt_tipo() != null) {
                    if (fila.getCnt_tipo().equals(Constantes.TIPO_CLIENTE_JURIDICA)) {
                        fila.setContraparte(fila.getCnt_razon());
                    } else {
                        fila.setContraparte(fila.getCnt_nombre());
                    }
                }

                if (StringUtils.isBlank(fila.getNumero())) {
                    sinNumero.add(fila);
                } else {
                    salida.add(fila);
                }
            }
            salida.addAll(sinNumero);
        }
        return salida;
    }


}

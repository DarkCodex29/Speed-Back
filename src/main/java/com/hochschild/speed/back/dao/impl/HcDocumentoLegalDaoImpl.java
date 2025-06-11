package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.dao.HcDocumentoLegalDao;
import com.hochschild.speed.back.dao.HcUbicacionPorDocumentoDao;
import com.hochschild.speed.back.model.bean.buscarDocumento.FilaBusquedaDocDashboardBean;
import com.hochschild.speed.back.model.domain.speed.HcDocumentoLegal;
import com.hochschild.speed.back.model.domain.speed.HcUbicacion;
import com.hochschild.speed.back.model.domain.speed.Traza;
import com.hochschild.speed.back.model.domain.speed.utils.FilaBusquedaDTS;
import com.hochschild.speed.back.model.domain.speed.utils.FilaSeguimientoSolicitudDTS;
import com.hochschild.speed.back.model.filter.BuscarDocumentoLegalFilter;
import com.hochschild.speed.back.model.reporte.ArrendamientoReporteBean;
import com.hochschild.speed.back.model.reporte.SeguimientoProcesosReporteBean;
import com.hochschild.speed.back.repository.speed.HcDocumentoLegalRepository;
import com.hochschild.speed.back.repository.speed.HcUbicacionPorDocumentoRepository;
import com.hochschild.speed.back.repository.speed.TrazaRepository;
import com.hochschild.speed.back.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.AppUtil;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class HcDocumentoLegalDaoImpl implements HcDocumentoLegalDao {
    private static final Logger LOGGER = Logger.getLogger(HcDocumentoLegalDaoImpl.class.getName());
    private final EntityManager entityManager;
    private final TrazaRepository trazaRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;

    private final HcUbicacionPorDocumentoDao hcUbicacionPorDocumentoDao;

    @Autowired
    public HcDocumentoLegalDaoImpl(@Qualifier("speedEntityManagerFactory") EntityManager entityManager, TrazaRepository trazaRepository, HcDocumentoLegalRepository hcDocumentoLegalRepository, HcUbicacionPorDocumentoRepository hcUbicacionPorDocumentoRepository, HcUbicacionPorDocumentoDao hcUbicacionPorDocumentoDao) {
        this.entityManager = entityManager;
        this.trazaRepository = trazaRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.hcUbicacionPorDocumentoDao = hcUbicacionPorDocumentoDao;
    }

    @Override
    public String normalizarTerminoBusqueda(String cadena) {
        if (!StringUtils.isBlank(cadena)) {
            cadena = AppUtil.quitarAcentos(cadena);
            cadena = cadena.toLowerCase();
            cadena = cadena.trim();
            cadena = cadena.replace(' ', '%');
            cadena = '%' + cadena + '%';
            return cadena;
        } else {
            return null;
        }
    }

    @Override
    public Integer obtenerNumeroAutomatico(Integer idCompania, Integer anio) {

        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery("GENERAR_NUMERACION_CONTRATO");

        // Register inputs
        storedProcedureQuery.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter(3, Integer.class, ParameterMode.OUT);

        // Set parameters
        storedProcedureQuery.setParameter(1, anio);
        storedProcedureQuery.setParameter(2, idCompania);

        // Realizamos la llamada al procedimiento
        storedProcedureQuery.execute();

        Object obj = storedProcedureQuery.getOutputParameterValue(3);

        if (obj != null) {
            return Integer.valueOf(obj.toString());
        }
        return null;
    }

    @Override
    public Integer obtenerNumeroManual(Integer idCompania, Integer anio) {

        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery("GENERAR_NUMERACION_CONTRATO_MANUAL");

        // Register inputs
        storedProcedureQuery.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
        storedProcedureQuery.registerStoredProcedureParameter(3, Integer.class, ParameterMode.OUT);

        // Set parameters
        storedProcedureQuery.setParameter(1, anio);
        storedProcedureQuery.setParameter(2, idCompania);

        // Realizamos la llamada al procedimiento
        storedProcedureQuery.execute();

        Object obj = storedProcedureQuery.getOutputParameterValue(3);

        if (obj != null) {
            return Integer.valueOf(obj.toString());
        }
        return null;
    }

    @Override
    public Boolean solicitudActualizarVigencia(Integer idDocumento) {

        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery("stpr_Solicitud_Actualizar_Vigencia");

        // Register inputs
        storedProcedureQuery.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);

        // Set parameters
        storedProcedureQuery.setParameter(1, idDocumento);

        try {
            // Realizamos la llamada al procedimiento
            storedProcedureQuery.execute();
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<FilaBusquedaDTS> buscarDocumentosLegales(
            BuscarDocumentoLegalFilter buscarDocumentoLegalFilter, Boolean datosAdicionales, List<String> numeroExpedientes) {
        String sql = "SELECT new com.hochschild.speed.back.model.domain.speed.utils.FilaBusquedaDTS(hc.expediente.id, "
                + "hc.id, "
                + "hc.expediente.proceso.id, "
                + "ad.contrato.id,"
                + "hc.numero,"
                + "hc.expediente.proceso.nombre, "
                + "(SELECT cn.tipo_contrato.nombre FROM HcContrato cn WHERE cn.id = hc.id), "
                + "hc.contraparte.tipo.codigo, "
                + "(hc.contraparte.apellidoPaterno ||' '|| hc.contraparte.apellidoMaterno ||' '|| hc.contraparte.nombre), "
                + "hc.contraparte.razonSocial, "
                + "hc.estado, "
                + "hc.area.compania.pais.codigo, "
                + "hc.area.compania.codigo, "
                + "hc.area.codigo,"
                + "hc.sumilla";

        if (datosAdicionales) {
            sql += ", "
                    + "hc.area.compania.nombre, "
                    + "(hc.solicitante.apellidos ||' '|| hc.solicitante.nombres), "
                    + "(hc.responsable.apellidos ||' '|| hc.responsable.nombres), "
                    + "(SELECT cn.moneda.descripcion FROM HcContrato cn WHERE cn.id = hc.id), "
                    + "(SELECT cn.monto FROM HcContrato cn WHERE cn.id = hc.id), "
                    + "hc.area.compania.pais.nombre, "
                    + "hc.area.nombre";
        }

        sql += ") FROM HcDocumentoLegal hc LEFT JOIN hc.adenda ad WHERE hc.expediente.estado in (:estadosPermitidos) ";

        if (buscarDocumentoLegalFilter.getNumero() != null) {
            sql += "AND dbo.unaccent(LOWER(hc.numero)) LIKE :numero ";
        }
        if (buscarDocumentoLegalFilter.getSumilla() != null) {
            sql += "AND dbo.unaccent(LOWER(hc.sumilla)) LIKE :sumilla ";
        }
        if (buscarDocumentoLegalFilter.getFechaFirmaInicio() != null) {
            sql += "AND dbo.obtener_fecha_inicio(hc.id) >= :fechaFirmaInicio ";
        }
        if (buscarDocumentoLegalFilter.getFechaFirmaFin() != null) {
            sql += "AND dbo.obtener_fecha_inicio(hc.id) <= :fechaFirmaFin ";
        }
        if (buscarDocumentoLegalFilter.getFechaVencimientoInicio() != null) {
            sql += "AND dbo.obtener_fecha_vencimiento(hc.id) >= :fechaVencimientoInicio ";
        }
        if (buscarDocumentoLegalFilter.getFechaVencimientoFin() != null) {
            sql += "AND dbo.obtener_fecha_vencimiento(hc.id) <= :fechaVencimientoFin ";
        }
        if (buscarDocumentoLegalFilter.getIdPais() != null) {
            sql += "AND hc.area.compania.pais.id = :idPais  ";
        }
        if (buscarDocumentoLegalFilter.getIdCompania() != null) {
            sql += "AND hc.area.compania.id = :idCompania  ";
        }
        if (buscarDocumentoLegalFilter.getIdArea() != null) {
            sql += "AND hc.area.id = :idArea  ";
        }
        if (buscarDocumentoLegalFilter.getIdUbicacion() != null) {
            sql += "AND :idUbicacion in (SELECT upd.id.ubicacion.id FROM HcUbicacionPorDocumento upd WHERE upd.id.documentoLegal.id = hc.id)  ";
        }
        if (buscarDocumentoLegalFilter.getIdSolicitante() != null) {
            sql += "AND hc.solicitante.id = :idSolicitante ";
        }
        if (buscarDocumentoLegalFilter.getIdResponsable() != null) {
            sql += "AND hc.responsable.id = :idResponsable ";
        }
        if (buscarDocumentoLegalFilter.getIdContraparte() != null) {
            sql += "AND hc.contraparte.id = :idContraparte ";
        }
        if (buscarDocumentoLegalFilter.getIdRepresentante() != null) {
            sql += "AND :idRepresentante in (SELECT rpd.id.cliente.id FROM HcRepresentatePorDocumento rpd WHERE rpd.id.documentoLegal.id = hc.id)  ";
        }
        if (buscarDocumentoLegalFilter.getMontoDesde() != null) {
            sql += "AND hc.contrato.monto >= :montoDesde  ";
        }
        if (buscarDocumentoLegalFilter.getMontoHasta() != null) {
            sql += "AND hc.contrato.monto <= :montoHasta  ";
        }
        if (buscarDocumentoLegalFilter.getIdTipoContrato() != null) {
            sql += "AND hc.contrato.tipo_contrato.id = :idTipoContrato  ";
        }
        if (buscarDocumentoLegalFilter.getEstado() != null) {
            sql += "AND hc.estado = :estado  ";
        }
        if (numeroExpedientes != null) {
            sql += "AND hc.expediente.numero in (:numeroExpedientes) ";
        }
        if (buscarDocumentoLegalFilter.getTipoSolicitud() != null) {
            sql += "AND (hc.expediente.proceso.id= :tipoSolicitud OR hc.expediente.proceso.idProcesoPadre= :tipoSolicitud)";
        }

        sql += "ORDER BY dbo.obtener_compania_numero(hc.numero) ASC, ";
        sql += "dbo.obtener_anio_numero(hc.numero) ASC, ";
        sql += "dbo.obtener_secuencia_numero(hc.numero) ASC, ";
        sql += "dbo.obtener_adenda_numero(hc.numero) ASC ";

        Query q = entityManager.createQuery(sql);

        List<Character> estadosPermitidos = new ArrayList<Character>();
        estadosPermitidos.add(Constantes.ESTADO_REGISTRADO);
        estadosPermitidos.add(Constantes.ESTADO_ARCHIVADO);
        estadosPermitidos.add(Constantes.ESTADO_GUARDADO);

        q.setParameter("estadosPermitidos", estadosPermitidos);

        if (buscarDocumentoLegalFilter.getNumero() != null) {
            q.setParameter("numero", buscarDocumentoLegalFilter.getNumero());
        }
        if (buscarDocumentoLegalFilter.getSumilla() != null) {
            q.setParameter("sumilla", buscarDocumentoLegalFilter.getSumilla());
        }
        if (buscarDocumentoLegalFilter.getFechaFirmaInicio() != null) {
            q.setParameter("fechaFirmaInicio", buscarDocumentoLegalFilter.getFechaFirmaInicio());
        }
        if (buscarDocumentoLegalFilter.getFechaFirmaFin() != null) {
            q.setParameter("fechaFirmaFin", buscarDocumentoLegalFilter.getFechaFirmaFin());
        }
        if (buscarDocumentoLegalFilter.getFechaVencimientoInicio() != null) {
            q.setParameter("fechaVencimientoInicio", buscarDocumentoLegalFilter.getFechaVencimientoInicio());
        }
        if (buscarDocumentoLegalFilter.getFechaVencimientoFin() != null) {
            q.setParameter("fechaVencimientoFin", buscarDocumentoLegalFilter.getFechaVencimientoFin());
        }
        if (buscarDocumentoLegalFilter.getIdPais() != null) {
            q.setParameter("idPais", buscarDocumentoLegalFilter.getIdPais());
        }
        if (buscarDocumentoLegalFilter.getIdCompania() != null) {
            q.setParameter("idCompania", buscarDocumentoLegalFilter.getIdCompania());
        }
        if (buscarDocumentoLegalFilter.getIdArea() != null) {
            q.setParameter("idArea", buscarDocumentoLegalFilter.getIdArea());
        }
        if (buscarDocumentoLegalFilter.getIdUbicacion() != null) {
            q.setParameter("idUbicacion", buscarDocumentoLegalFilter.getIdUbicacion());
        }
        if (buscarDocumentoLegalFilter.getIdSolicitante() != null) {
            q.setParameter("idSolicitante", buscarDocumentoLegalFilter.getIdSolicitante());
        }
        if (buscarDocumentoLegalFilter.getIdResponsable() != null) {
            q.setParameter("idResponsable", buscarDocumentoLegalFilter.getIdResponsable());
        }
        if (buscarDocumentoLegalFilter.getIdContraparte() != null) {
            q.setParameter("idContraparte", buscarDocumentoLegalFilter.getIdContraparte());
        }
        if (buscarDocumentoLegalFilter.getIdRepresentante() != null) {
            q.setParameter("idRepresentante", buscarDocumentoLegalFilter.getIdRepresentante());
        }
        if (buscarDocumentoLegalFilter.getMontoDesde() != null) {
            q.setParameter("montoDesde", buscarDocumentoLegalFilter.getMontoDesde());
        }
        if (buscarDocumentoLegalFilter.getMontoHasta() != null) {
            q.setParameter("montoHasta", buscarDocumentoLegalFilter.getMontoHasta());
        }
        if (buscarDocumentoLegalFilter.getIdTipoContrato() != null) {
            q.setParameter("idTipoContrato", buscarDocumentoLegalFilter.getIdTipoContrato());
        }
        if (buscarDocumentoLegalFilter.getEstado() != null) {
            q.setParameter("estado", buscarDocumentoLegalFilter.getEstado());
        }
        if (numeroExpedientes != null) {
            q.setParameter("numeroExpedientes", numeroExpedientes);
        }
        if (buscarDocumentoLegalFilter.getTipoSolicitud() != null) {
            q.setParameter("tipoSolicitud",buscarDocumentoLegalFilter.getTipoSolicitud());
        }
        return (List<FilaBusquedaDTS>) q.getResultList();
    }

    @Override
    public List<ArrendamientoReporteBean> buscarDocumentosLegalesArrendamiento(String numero, Date fechaFirmaInicio, Date fechaFirmaFin, Date fechaVencimientoInicio, Date fechaVencimientoFin, Integer idPais, Integer idCompania, Integer idArea, String tipoUbicacion, Integer idUbicacion, Integer idContraparte, Double montoDesde, Double montoHasta, Integer idTipoContrato, Character estado, List<String> numeroExpedientes, boolean datosAdicionales, boolean isArrendamiento) {
        String sql = "SELECT new com.hochschild.speed.back.model.reporte.ArrendamientoReporteBean(hc.expediente.id, "
                + "hc.id, "
                + "hc.numero,"
                + "hc.expediente.proceso.nombre, "
                + "(SELECT cn.tipo_contrato.nombre FROM HcContrato cn WHERE cn.id = hc.id), "
                + "hc.contraparte.tipo.codigo, "
                + "(hc.contraparte.apellidoPaterno ||' '|| hc.contraparte.apellidoMaterno ||' '|| hc.contraparte.nombre), "
                + "hc.contraparte.razonSocial, "
                + "hc.estado, "
                + "hc.area.compania.pais.codigo, "
                + "hc.area.compania.codigo, "
                + "hc.area.codigo,"
                + "hc.sumilla, "
                + "(CASE WHEN  (hc.expediente.aplicaArrendamiento = 'S') THEN 'Aplica' ELSE 'No aplica' END )";

        if (datosAdicionales) {
            sql += ", "
                    + "hc.area.compania.nombre, "
                    + "(hc.solicitante.apellidos ||' '|| hc.solicitante.nombres), "
                    + "(hc.responsable.apellidos ||' '|| hc.responsable.nombres), "
                    + "(SELECT cn.moneda.descripcion FROM HcContrato cn WHERE cn.id = hc.id), "
                    + "(SELECT cn.monto FROM HcContrato cn WHERE cn.id = hc.id), "
                    + "hc.area.compania.pais.nombre, "
                    + "hc.area.nombre";
        }

        sql += ") FROM HcDocumentoLegal hc WHERE hc.expediente.estado in (:estadosPermitidos) ";

        if (isArrendamiento) {
            sql += " AND (hc.contrato.arrendamiento = :isArrendamiento)";
        }
        if (numero != null) {
            sql += "AND dbo.unaccent(LOWER(hc.numero)) LIKE :numero ";
        }
        if (fechaFirmaInicio != null) {
            sql += "AND CAST(dbo.obtener_fecha_inicio(hc.id) AS date) >= :fechaFirmaInicio ";
        }
        if (fechaFirmaFin != null) {
            sql += "AND CAST(dbo.obtener_fecha_inicio(hc.id) AS date) <= :fechaFirmaFin ";
        }
        if (fechaVencimientoInicio != null) {
            sql += "AND CAST(dbo.obtener_fecha_vencimiento(hc.id) AS date) >= :fechaVencimientoInicio ";
        }
        if (fechaVencimientoFin != null) {
            sql += "AND CAST(dbo.obtener_fecha_vencimiento(hc.id) AS date) <= :fechaVencimientoFin ";
        }
        if (idPais != null) {
            sql += "AND hc.area.compania.pais.id = :idPais  ";
        }
        if (idCompania != null) {
            sql += "AND hc.area.compania.id = :idCompania  ";
        }
        if (idArea != null) {
            sql += "AND hc.area.id = :idArea  ";
        }
        if (idUbicacion != null) {
            sql += "AND :idUbicacion in (SELECT upd.id.ubicacion.id FROM HcUbicacionPorDocumento upd WHERE upd.id.documentoLegal.id = hc.id)  ";
        }
        if (idContraparte != null) {
            sql += "AND hc.contraparte.id = :idContraparte ";
        }
        if (montoDesde != null) {
            sql += "AND hc.contrato.monto >= :montoDesde  ";
        }
        if (montoHasta != null) {
            sql += "AND hc.contrato.monto <= :montoHasta  ";
        }
        if (idTipoContrato != null) {
            sql += "AND hc.contrato.tipo_contrato.id = :idTipoContrato  ";
        }
        if (estado != null) {
            sql += "AND hc.estado = :estado  ";
        }
        if (numeroExpedientes != null) {
            sql += "AND hc.expediente.numero in (:numeroExpedientes) ";
        }

        sql += "ORDER BY dbo.obtener_compania_numero(hc.numero) ASC, ";
        sql += "dbo.obtener_anio_numero(hc.numero) ASC, ";
        sql += "dbo.obtener_secuencia_numero(hc.numero) ASC, ";
        sql += "dbo.obtener_adenda_numero(hc.numero) ASC ";

        Query q = entityManager.createQuery(sql);

        List<Character> estadosPermitidos = new ArrayList<>();
        estadosPermitidos.add(Constantes.ESTADO_REGISTRADO);
        estadosPermitidos.add(Constantes.ESTADO_ARCHIVADO);
        estadosPermitidos.add(Constantes.ESTADO_GUARDADO);

        q.setParameter("estadosPermitidos", estadosPermitidos);

        if (numero != null) {
            q.setParameter("numero", numero);
        }

        if (fechaFirmaInicio != null) {
            q.setParameter("fechaFirmaInicio", fechaFirmaInicio);
        }
        if (fechaFirmaFin != null) {
            q.setParameter("fechaFirmaFin", fechaFirmaFin);
        }
        if (fechaVencimientoInicio != null) {
            q.setParameter("fechaVencimientoInicio", fechaVencimientoInicio);
        }
        if (fechaVencimientoFin != null) {
            q.setParameter("fechaVencimientoFin", fechaVencimientoFin);
        }
        if (idPais != null) {
            q.setParameter("idPais", idPais);
        }
        if (idCompania != null) {
            q.setParameter("idCompania", idCompania);
        }
        if (idArea != null) {
            q.setParameter("idArea", idArea);
        }
        if (idUbicacion != null) {
            q.setParameter("idUbicacion", idUbicacion);
        }

        if (idContraparte != null) {
            q.setParameter("idContraparte", idContraparte);
        }

        if (montoDesde != null) {
            q.setParameter("montoDesde", montoDesde.floatValue());
        }
        if (montoHasta != null) {
            q.setParameter("montoHasta", montoHasta.floatValue());
        }
        if (idTipoContrato != null) {
            q.setParameter("idTipoContrato", idTipoContrato);
        }
        if (estado != null) {
            q.setParameter("estado", estado);
        }
        if (numeroExpedientes != null) {
            q.setParameter("numeroExpedientes", numeroExpedientes);
        }
        if (isArrendamiento) {
            q.setParameter("isArrendamiento", isArrendamiento);
        }

        List<ArrendamientoReporteBean> resultado = (List<ArrendamientoReporteBean>) q.getResultList();
        return resultado;
    }

    @Override
    public String obtenerFechaInicio(Integer idDocumentoLegal) {
        Query q = entityManager.createNativeQuery("SELECT dbo.formato_fecha(dbo.obtener_fecha_inicio(?)) AS FECHA");
        q.setParameter(1, idDocumentoLegal);

        try {
            Object objFecha = q.getSingleResult();

            if (objFecha != null) {
                return objFecha.toString();
            }

        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "";
    }

    @Override
    public String obtenerFechaVencimiento(Integer idDocumentoLegal) {
        Query q = entityManager.createNativeQuery("SELECT dbo.formato_fecha(dbo.obtener_fecha_vencimiento(?)) AS FECHA");
        q.setParameter(1, idDocumentoLegal);

        try {
            Object objFecha = q.getSingleResult();

            if (objFecha != null) {
                return objFecha.toString();
            }

        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<SeguimientoProcesosReporteBean> buscarSeguimientoProcesosAll(Integer[] idSolicitante, Integer idCompania,
                                                                             String[] estadoMultiple, String idUbicacion) {

        /*for(int k=0;k<=idEstadoSP.length-1;k++){
			LOGGER.info(k+':'+idEstadoSP[k]);
		}*/
        //String estadoMultiple[] = {};
        String cadenaEstadoMultiple = "";
        int j = 0;
        while (j < estadoMultiple.length) {
            String strestado = "'" + estadoMultiple[j] + "'";
            if (cadenaEstadoMultiple.equals("")) {
                cadenaEstadoMultiple = strestado;
            } else {

                cadenaEstadoMultiple = cadenaEstadoMultiple + ',' + strestado;
            }
            j = j + 1;
        }

        //sqlContratos.append(" and dl.estado in ("+cadenaEstadoMultiple+") ");*/
        LOGGER.info(cadenaEstadoMultiple.toString());

        String cadena = " AND ";
        if (idSolicitante.length <= 1) {
            cadena = cadena + " sol.id = " + idSolicitante[0] + " ";
        } else {
            cadena = cadena + " ( ";
            for (int i = 0; i < idSolicitante.length; i++) {
                if (i == 0) {
                    cadena = cadena + " sol.id = " + idSolicitante[i] + " ";
                } else {
                    cadena = cadena + " OR sol.id = " + idSolicitante[i] + " ";
                }
            }
            cadena = cadena + " ) ";
        }

        List<SeguimientoProcesosReporteBean> result = new ArrayList<>();
        StringBuffer sqlContratos = new StringBuffer("SELECT dl.id,dl.numero,p.nombre, contra.razonSocial,"
                + "dl.sumilla,hc.nombre, sol.nombres + ' '+sol.apellidos as solicitante, dl.fechaSolicitud, dl.estado, res.nombres + ' ' + res.apellidos as responsable, t.id ");
        sqlContratos.append("FROM Expediente e, HcDocumentoLegal dl, Traza t,Proceso p,Cliente contra,Usuario sol, Usuario res,HcArea ha, HcCompania hc, HcUbicacionPorDocumento upd ");
        sqlContratos.append("WHERE e.id = dl.expediente.id and t.expediente.id = e.id and upd.id.documentoLegal.id = dl.id and "
                + "contra.id = dl.contraparte.id and "
                + " dl.solicitante.id = sol.id and"
                + " dl.responsable.id = res.id and"
                + " dl.area.id = ha.id and ha.compania.id = hc.id and "
                + "p.id = e.proceso.id and t.actual = '1' and dl.estado != 'O' and dl.estado != 'T' and e.estado != 'X' and e.estado != 'N'" + cadena);

        if (idCompania != 0) {
            sqlContratos.append(" and hc.id = :idCompania ");
        }
        sqlContratos.append(" and dl.estado in(" + cadenaEstadoMultiple + ") ");

        if (!idUbicacion.equals("vacio")) {
            sqlContratos.append(" and upd.id.ubicacion.nombre like :idUbicacion ");
        }

        sqlContratos.append(" GROUP BY dl.id,dl.numero,p.nombre, contra.razonSocial,dl.sumilla,hc.nombre, sol.nombres + ' '+sol.apellidos, dl.fechaSolicitud, dl.estado, res.nombres + ' ' + res.apellidos, t.id ");
        sqlContratos.append(" ORDER BY dl.fechaSolicitud DESC");
        LOGGER.info(sqlContratos.toString());
        Query qContratos = entityManager.createQuery(sqlContratos.toString());

        if (idCompania != 0) {
            qContratos.setParameter("idCompania", idCompania);
        }

        if (!idUbicacion.equals("vacio")) {
            qContratos.setParameter("idUbicacion", idUbicacion);
        }

        List<Object[]> contratos = qContratos.getResultList();
        SeguimientoProcesosReporteBean dl = null;
        List<HcUbicacion> ubicaciones = null;
        Traza traza = null;
        for (Object[] contrato : contratos) {
            dl = new SeguimientoProcesosReporteBean();
            Integer idDocLegal = (Integer) contrato[0];
            dl.setIdDocumentoLegal(idDocLegal);
            ubicaciones = hcDocumentoLegalRepository.obtenerUbicacionesDocLegal(idDocLegal);
            if (ubicaciones != null) {
                StringBuilder sbUbicaciones = new StringBuilder("");
                for (HcUbicacion ub : ubicaciones) {
                    sbUbicaciones.append(ub.getNombre()).append(" / ");
                }
                dl.setUbicaciones(sbUbicaciones.toString());
            }
            dl.setNumero((String) contrato[1]);
            dl.setTipoSolicitud((String) contrato[2]);
            dl.setNombreContraparte((String) contrato[3]);
            dl.setSumilla((String) contrato[4]);
            dl.setCompania((String) contrato[5]);
            dl.setNombreSolicitante((String) contrato[6]);
            dl.setFechaSolicitud((Date) contrato[7]);
            dl.setEstado(AppUtil.getNombreEstadoDL((Character) contrato[8]));
            dl.setNombreResponsable((String) contrato[9]);
            traza = trazaRepository.findById((Integer) contrato[10]);
            dl.setObservacion(traza.getObservacion());
            result.add(dl);
        }
        return result;
    }

    @Override
    public List<FilaSeguimientoSolicitudDTS> buscarSeguimientoSolicitud(String numero,
                                                                        String sumilla,
                                                                        Integer idContraparte,
                                                                        Integer idSolicitante,
                                                                        Integer idResponsable,
                                                                        String estado) {
        try {

            Query q = entityManager.createNativeQuery("{call stpr_Listado_SolicitudSeguimiento(?,?,?,?,?,?)}");

            q.setParameter(1, numero);
            q.setParameter(2, sumilla);
            q.setParameter(3, idContraparte);
            q.setParameter(4, idSolicitante);
            q.setParameter(5, idResponsable);
            q.setParameter(6, estado);

            LOGGER.info("---------------------------------buscarSeguimientoSolicitud parametros e------------------------------------------------");
            LOGGER.info("Numero " + numero);
            LOGGER.info("sumilla " + sumilla);
            LOGGER.info("idContraparte " + idContraparte);
            LOGGER.info("idResponsable " + idResponsable);
            LOGGER.info("idSolicitante " + idSolicitante);
            LOGGER.info("estado " + estado);

            List<Object[]> result = q.getResultList();

            return parseFromMapToFilaSeguimientoSolicitudDTS(result);

        } catch (NoResultException e) {
            LOGGER.error(e.getMessage(),e);
        }
        return null;
    }

    private List<FilaSeguimientoSolicitudDTS> parseFromMapToFilaSeguimientoSolicitudDTS(List<Object[]> result) {

        List<FilaSeguimientoSolicitudDTS> listadoSolicitudes = new ArrayList<>();

        for (Object[] solicitud : result) {

            FilaSeguimientoSolicitudDTS fila = new FilaSeguimientoSolicitudDTS();

            fila.setIdExpediente((Integer) solicitud[1]);
            fila.setIdDocumentoLegal((Integer) solicitud[2]);
            fila.setNumeroDocumento(String.valueOf(solicitud[3]));
            fila.setAbogado(String.valueOf(solicitud[4]));
            fila.setSumilla(String.valueOf(solicitud[5]));
            fila.setContraparte(String.valueOf(solicitud[6]));
            fila.setSolicitante(String.valueOf(solicitud[7]));
            LOGGER.info("solicitante -> " + solicitud[7]);
            LOGGER.info("fecha ultimo movimiento -> " + solicitud[8]);
            fila.setUltimoMovimiento((String) solicitud[8]);
            fila.setIdUbicacion((Integer) solicitud[9]);
            fila.setUbicacionDocumento(String.valueOf(solicitud[10]));
            fila.setEstado(String.valueOf(solicitud[11]));

            listadoSolicitudes.add(fila);
        }

        return listadoSolicitudes;
    }

@Override
    public Integer normalizarIdBusqueda(Integer id) {
        if (id != null) {
            if (id <= 0) {
                id = null;
            }
        }
        return id;
    }

    @Override
    public Double normalizarMonto(Double monto) {
        if (monto != null) {
            if (monto <= 0d) {
                monto = null;
            }
        }
        return monto;
    }

    @Override
    public List<FilaBusquedaDTS> filtrarPorVO(List<FilaBusquedaDTS> lista, List<String> valoresORG) {
        if (lista != null && !lista.isEmpty()) {
            for (FilaBusquedaDTS fila : lista) {
                fila.setPuedeVisualizar(validarPorVO(fila, valoresORG));
            }
        }
        return lista;
    }

    @Override
    public Boolean validarPorVO(FilaBusquedaDTS datos, List<String> valoresORG) {
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

    @Override
    public List<FilaBusquedaDTS> completarOrdenar(List<FilaBusquedaDTS> lista, Boolean integracionSCA) {

        List<FilaBusquedaDTS> salida = new ArrayList<>();
        List<FilaBusquedaDTS> sinNumero = new ArrayList<>();

        if (lista != null && !lista.isEmpty()) {
            for (FilaBusquedaDTS fila : lista) {
                if (!integracionSCA) {
                    fila.setPuedeVisualizar(true);
                }

                fila.setUbicacion(hcUbicacionPorDocumentoDao.obtenerUbicacionesReporte(fila.getIdDocumentoLegal()));
                fila.setEstado(AppUtil.getNombreEstadoDL(fila.getEst_codigo()));
                fila.setFechaInicio(obtenerFechaInicio(fila.getIdDocumentoLegal()));
                fila.setFechaVencimiento(obtenerFechaVencimiento(fila.getIdDocumentoLegal()));

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

    @Override
    public List<HcDocumentoLegal> obtenerVencimientoHoy(){
        String sql = "SELECT hc FROM HcDocumentoLegal hc WHERE dbo.obtener_fecha_vencimiento(hc.id) = :fecha";
        Query q = entityManager.createQuery(sql);
        q.setParameter("fecha", DateUtil.minimizarFecha(new Date()), TemporalType.DATE);
        return q.getResultList();
    }
    
    @Override
    public String obtenerAlerta(Integer idDocumentoLegal) {

        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery("SPEED_Alerta_Solicitudes");

        // Registrar el parámetro de entrada
        storedProcedureQuery.registerStoredProcedureParameter("id_documento_legal", Integer.class, ParameterMode.IN);

        // Establecer el parámetro de entrada
        storedProcedureQuery.setParameter("id_documento_legal", idDocumentoLegal);

        // Ejecutar el procedimiento
        storedProcedureQuery.execute();

        // Obtener el resultado
        List<String> resultados = storedProcedureQuery.getResultList();

        // Retornar el primer resultado o null si no hay resultados
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    @Override
    public List<FilaBusquedaDocDashboardBean> buscarDocumentosLegalesDashboard(BuscarDocumentoLegalFilter buscarDocumentoLegalFilter) {
        StringBuilder sql = new StringBuilder("SELECT " +
                "dl.id_documento_legal as idDocumentoLegal, " +
                "dl.numero as numeroDocumento, " +
                "a.nombre as area, " +
                "pu.valor as ubicacion,"+
                "CASE c.id_tipo_cliente " +
                "WHEN 1 THEN c.razon_social " +
                "ELSE c.apellido_paterno + ' ' + c.apellido_materno + ' ' + c.nombre " +
                "END AS contraparte, " +
                "dl.fecha_solicitud as fechaSolicitud, " +
                "p.nombre as proceso " +
                "FROM HC_DOCUMENTO_LEGAL dl " +
                "INNER JOIN EXPEDIENTE e ON dl.expediente = e.id_expediente " +
                "INNER JOIN PROCESO p ON p.id_proceso = e.id_proceso " +
                "INNER JOIN USUARIO_GESTION_REPORTE ur ON ur.id_usuario = dl.responsable AND ur.estado = 'S' " +
                "INNER JOIN USUARIO u ON u.id_usuario = ur.id_usuario " +
                "INNER JOIN HC_AREA a ON dl.hc_area = a.id_hc_area " +
                "INNER JOIN PARAMETRO pu ON dl.ubicacion_legal = pu.id_parametro " +
                "INNER JOIN HC_COMPANIA cia ON a.compania = cia.id_compania " +
                "INNER JOIN HC_PAIS pa ON cia.pais = pa.id_pais " +
                "LEFT JOIN CLIENTE c ON dl.contraparte = c.id_cliente " +
                "WHERE e.estado NOT IN ('N', 'X') " +
                "AND p.id_proceso != 5 ");

        if (buscarDocumentoLegalFilter.getFlagPracticante() != null && buscarDocumentoLegalFilter.getFlagPracticante()) {
            sql.append("AND dl.responsable = :idUsuario ");
        }

        if (buscarDocumentoLegalFilter.getEstado() != null) {
            sql.append("AND dl.estado = :estado ");
        }

        if (buscarDocumentoLegalFilter.getNumero() != null) {
            sql.append("AND dbo.unaccent(LOWER(dl.numero)) LIKE :numero ");
        }

        if (buscarDocumentoLegalFilter.getIdContraparte() != null) {
            sql.append("AND c.id_cliente = :idContraparte ");
        }

        if (buscarDocumentoLegalFilter.getIdPais() != null) {
            sql.append("AND pa.id_pais = :idPais ");
        }

        if (buscarDocumentoLegalFilter.getSumilla() != null) {
            sql.append("AND dbo.unaccent(LOWER(dl.sumilla)) LIKE :sumilla ");
        }

        if (buscarDocumentoLegalFilter.getTipoSolicitud() != null && buscarDocumentoLegalFilter.getTipoSolicitud() > 0) {
            sql.append("AND (p.id_proceso= :tipoSolicitud OR p.id_proceso_padre= :tipoSolicitud) ");
        }

        if (buscarDocumentoLegalFilter.getIdCompania() != null) {
            sql.append("AND cia.id_compania = :idCompania ");
        }

        if (buscarDocumentoLegalFilter.getIdArea() != null && buscarDocumentoLegalFilter.getIdArea()>0) {
            sql.append("AND a.id_hc_area = :idArea ");
        }

        if (buscarDocumentoLegalFilter.getIdUbicacion() != null && buscarDocumentoLegalFilter.getIdUbicacion()>0) {
            sql.append("AND pu.id_parametro = :idUbicacion ");
        }

        sql.append("ORDER BY dl.id_documento_legal ASC");

        Query q = entityManager.createNativeQuery(sql.toString());

        if (buscarDocumentoLegalFilter.getFlagPracticante() != null && buscarDocumentoLegalFilter.getFlagPracticante()) {
            q.setParameter("idUsuario", buscarDocumentoLegalFilter.getIdUsuario());
        }
        if (buscarDocumentoLegalFilter.getEstado() != null) {
            q.setParameter("estado", buscarDocumentoLegalFilter.getEstado());
        }
        if (buscarDocumentoLegalFilter.getNumero() != null) {
            q.setParameter("numero", "%" + buscarDocumentoLegalFilter.getNumero().toLowerCase() + "%");
        }
        if (buscarDocumentoLegalFilter.getIdContraparte() != null) {
            q.setParameter("idContraparte", buscarDocumentoLegalFilter.getIdContraparte());
        }
        if (buscarDocumentoLegalFilter.getIdPais() != null) {
            q.setParameter("idPais", buscarDocumentoLegalFilter.getIdPais());
        }
        if (buscarDocumentoLegalFilter.getSumilla() != null) {
            q.setParameter("sumilla", "%" + buscarDocumentoLegalFilter.getSumilla().toLowerCase() + "%");
        }
        if (buscarDocumentoLegalFilter.getTipoSolicitud() != null && buscarDocumentoLegalFilter.getTipoSolicitud() > 0) {
            q.setParameter("tipoSolicitud", buscarDocumentoLegalFilter.getTipoSolicitud());
        }
        if (buscarDocumentoLegalFilter.getIdCompania() != null) {
            q.setParameter("idCompania", buscarDocumentoLegalFilter.getIdCompania());
        }
        if (buscarDocumentoLegalFilter.getIdArea() != null && buscarDocumentoLegalFilter.getIdArea()>0) {
            q.setParameter("idArea", buscarDocumentoLegalFilter.getIdArea());
        }
        if (buscarDocumentoLegalFilter.getIdUbicacion() != null && buscarDocumentoLegalFilter.getIdUbicacion()>0) {
            q.setParameter("idUbicacion", buscarDocumentoLegalFilter.getIdUbicacion());
        }

        List<Object[]> results = q.getResultList();
        List<FilaBusquedaDocDashboardBean> documentos = new ArrayList<>();

        for (Object[] result : results) {
            FilaBusquedaDocDashboardBean documento = new FilaBusquedaDocDashboardBean(
                    ((Number) result[0]).intValue(),
                    (String) result[1],
                    (String) result[2],
                    (String) result[3],
                    (String) result[4],
                    result[5] != null ? result[5].toString() : null,
                    (String) result[6]
            );
            documentos.add(documento);
        }

        return documentos;
    }

}
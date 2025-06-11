package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcDocumentoLegal;
import com.hochschild.speed.back.model.domain.speed.HcUbicacion;
import com.hochschild.speed.back.model.domain.speed.utils.DocumentoLegalXAdendaDTS;
import com.hochschild.speed.back.model.bean.registrarSolicitud.ContratoSumillaBean;
import com.hochschild.speed.back.model.reporte.DatosR4;
import com.hochschild.speed.back.model.reporte.DocumentoLegalAlarmaContratoBean;
import com.hochschild.speed.back.model.reporte.DocumentoLegalReporteBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HcDocumentoLegalRepository extends JpaRepository<HcDocumentoLegal, Integer> {
    @Query("select e from HcDocumentoLegal e where e.id = :idHcDocumentoLegal")
    HcDocumentoLegal findById(@Param("idHcDocumentoLegal") Integer idHcDocumentoLegal);

    @Query("select e from HcDocumentoLegal e where e.expediente.id = :idExpediente")
    HcDocumentoLegal findByIdExpediente(@Param("idExpediente") Integer idExpediente);

    @Query("select e from HcDocumentoLegal e where e.estado = 'A' AND e.expediente.id = :idExpediente")
    HcDocumentoLegal findByIdExpedienteActive(@Param("idExpediente") Integer idExpediente);

    @Query("select hc from HcDocumentoLegal hc LEFT JOIN FETCH hc.expediente e where e.id = :idExpediente")
    HcDocumentoLegal obtenerHcDocumentoLegalPorExpediente(@Param("idExpediente") Integer idExpediente);

    @Query("SELECT new com.hochschild.speed.back.model.domain.speed.utils.DocumentoLegalXAdendaDTS(hc.area.id,"
            + "hc.area.nombre,"
            + "hc.area.compania.id,"
            + "hc.area.compania.nombre,"
            + "hc.area.compania.pais.nombre,"
            + "hc.contraparte.id,"
            + "hc.contraparte.razonSocial || ' (' || hc.contraparte.tipo.documento || ':' || hc.contraparte.numeroIdentificacion ||')',"
            + "hc.cnt_domicilio,"
            + "hc.cnt_nombre_contacto,"
            + "hc.cnt_telefono_contacto,"
            + "hc.cnt_correo_contacto) "
            + "FROM HcDocumentoLegal hc WHERE hc.id = :idDocumentoLegal")
    DocumentoLegalXAdendaDTS obtenerDatosParaAdenda(@Param("idDocumentoLegal") Integer idDocumentoLegal);

    @Query("SELECT new com.hochschild.speed.back.model.bean.registrarSolicitud.ContratoSumillaBean (" +
            "  hc.id" +
            ", concat(hc.numero,' - ', hc.sumilla) " +
            ") FROM HcDocumentoLegal hc " +
            "WHERE NOT EXISTS (" +
            " SELECT ad FROM HcAdenda ad " +
            " WHERE ad.documentoLegal.id = hc.id) " +
            " AND hc.estado != 'N' " +
            " AND hc.estado != 'R' " +
            //" AND lower(function('unaccent', concat(hc.numero, ' ', hc.sumilla))) LIKE lower(function('unaccent', concat('%', :numeroSumilla, '%')))"
            " AND  (" +
            "       hc.sumilla LIKE :numeroSumilla " +
            "       or hc.numero like :numeroSumilla" +
            "       )"
    )
    List<ContratoSumillaBean> autocompletarContrato(@Param("numeroSumilla") String numeroSumilla);

    @Query("SELECT u.id.ubicacion FROM HcUbicacionPorDocumento u WHERE u.id.documentoLegal.id = :idDocLegal")
    public List<HcUbicacion> obtenerUbicacionesDocLegal(@Param("idDocLegal") Integer idDocLegal);

    @Query("SELECT new com.hochschild.speed.back.model.reporte.DocumentoLegalReporteBean(h.id,COALESCE( h.numero, ''),COALESCE( h.area.compania.nombre, ''), COALESCE(h.contraparte.razonSocial,''), c.fechaInicio, h.fechaSolicitud, h.fechaBorrador, CONCAT(COALESCE(h.solicitante.nombres,'') , ' ', COALESCE(h.solicitante.apellidos,'') ), h.estado, CONCAT(COALESCE(h.responsable.nombres,'') , ' ', COALESCE(h.responsable.apellidos,'') ) ) FROM HcDocumentoLegal h JOIN h.contrato c  WHERE h.estado<>'N' AND h.expediente.estado='R' AND (h.responsable.id = :idResponsable OR :idResponsable IS NULL) ORDER BY h.expediente.fechaCreacion DESC")
    public List<DocumentoLegalReporteBean> buscarContratosPorAbogadoResponsable(@Param("idResponsable") Integer idResponsable);

    @Query("SELECT new com.hochschild.speed.back.model.reporte.DocumentoLegalReporteBean(h.id,COALESCE( h.numero, ''),COALESCE( h.area.compania.nombre, ''), COALESCE(h.contraparte.razonSocial,''), c.fechaInicio, h.fechaSolicitud, h.fechaBorrador, CONCAT(COALESCE(h.solicitante.nombres,'') , ' ', COALESCE(h.solicitante.apellidos,'') ), h.estado, CONCAT(COALESCE(h.responsable.nombres,'') , ' ', COALESCE(h.responsable.apellidos,'') ) ) FROM HcDocumentoLegal h JOIN h.adenda ad JOIN ad.contrato c WHERE h.estado<>'N' AND h.expediente.estado='R' AND (h.responsable.id = :idResponsable OR :idResponsable IS NULL) ORDER BY h.expediente.fechaCreacion DESC")
    public List<DocumentoLegalReporteBean> buscarAdendasPorAbogadoResponsable(@Param("idResponsable") Integer idResponsable);

    @Query("SELECT new com.hochschild.speed.back.model.reporte.DocumentoLegalAlarmaContratoBean(hd.numero, hd.area.compania.nombre, hd.contraparte.razonSocial, CONCAT(COALESCE(hd.responsable.nombres, ' '), ' ', COALESCE(hd.responsable.apellidos, ' ')), CONCAT(COALESCE(hd.solicitante.nombres, ' '), ' ', COALESCE(hd.solicitante.apellidos, ' ')), c.fechaFin, hd.estado, ha.fechaAlarma, ha.dias_activacion, ha.dias_intervalo, ha.titulo, ha.estado) FROM HcAlarma ha JOIN ha.documentoLegal hd JOIN hd.contrato c WHERE " +
            "(hd.responsable.id = :idAbogadoResponsable OR :idAbogadoResponsable IS NULL) " +
            "AND (hd.contraparte.id = :idContraparte OR :idContraparte IS NULL) " +
            "AND (hd.area.compania.id = :idCompania OR :idCompania IS NULL) " +
            "AND (hd.area.id = :idArea OR :idArea IS NULL) " +
            "AND (hd.numero LIKE %:numeroContrato% OR :numeroContrato IS NULL) " +
            "AND (ha.fechaAlarma BETWEEN :fechaInicio AND :fechaFin) ORDER BY hd.fechaSolicitud DESC")
    public List<DocumentoLegalAlarmaContratoBean> buscarAlarmasContratos(@Param("idAbogadoResponsable") Integer idAbogadoResponsable, @Param("idContraparte") Integer idContraparte, @Param("idCompania") Integer idCompania, @Param("idArea") Integer idArea, @Param("numeroContrato") String numeroContrato, @Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    @Query("SELECT new com.hochschild.speed.back.model.reporte.DocumentoLegalAlarmaContratoBean(hd.numero, hd.area.compania.nombre, hd.contraparte.razonSocial, CONCAT(COALESCE(hd.responsable.nombres, ' '), ' ', COALESCE(hd.responsable.apellidos, ' ')), CONCAT(COALESCE(hd.solicitante.nombres, ' '), ' ', COALESCE(hd.solicitante.apellidos, ' ')), c.fechaFin, hd.estado, ha.fechaAlarma, ha.dias_activacion, ha.dias_intervalo, ha.titulo, ha.estado) FROM HcAlarma ha JOIN ha.documentoLegal hd JOIN hd.adenda ad JOIN ad.contrato c WHERE" +
            " (hd.responsable.id = :idAbogadoResponsable OR :idAbogadoResponsable IS NULL) " +
            "AND (hd.contraparte.id = :idContraparte OR :idContraparte IS NULL) " +
            "AND (hd.area.compania.id = :idCompania OR :idCompania IS NULL) " +
            "AND (hd.area.id = :idArea OR :idArea IS NULL) " +
            "AND (hd.numero LIKE %:numeroContrato% OR :numeroContrato IS NULL) " +
            "AND (ha.fechaAlarma BETWEEN :fechaInicio AND :fechaFin) ORDER BY hd.fechaSolicitud DESC")
    public List<DocumentoLegalAlarmaContratoBean> buscarAlarmaAdendas(@Param("idAbogadoResponsable") Integer idAbogadoResponsable, @Param("idContraparte") Integer idContraparte, @Param("idCompania") Integer idCompania, @Param("idArea") Integer idArea, @Param("numeroContrato") String numeroContrato, @Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    @Query("SELECT new com.hochschild.speed.back.model.reporte.DatosR4( dl.estado, " +
            "CAST((SELECT COUNT(f.estado) FROM Traza t JOIN t.expediente e JOIN e.documentoLegal f WHERE t.actual = true AND f.estado = dl.estado AND MONTH(t.fechaCreacion) = 1 AND YEAR(t.fechaCreacion) = :year AND (f.responsable.id = :idResponsable OR :idResponsable IS NULL)) AS string ), " +
            "CAST((SELECT COUNT(f.estado) FROM Traza t JOIN t.expediente e JOIN e.documentoLegal f WHERE t.actual = true AND f.estado = dl.estado AND MONTH(t.fechaCreacion) = 2 AND YEAR(t.fechaCreacion) = :year AND (f.responsable.id = :idResponsable OR :idResponsable IS NULL)) AS string), " +
            "CAST((SELECT COUNT(f.estado) FROM Traza t JOIN t.expediente e JOIN e.documentoLegal f WHERE t.actual = true AND f.estado = dl.estado AND MONTH(t.fechaCreacion) = 3 AND YEAR(t.fechaCreacion) = :year AND (f.responsable.id = :idResponsable OR :idResponsable IS NULL)) AS string ), " +
            "CAST((SELECT COUNT(f.estado) FROM Traza t JOIN t.expediente e JOIN e.documentoLegal f WHERE t.actual = true AND f.estado = dl.estado AND MONTH(t.fechaCreacion) = 4 AND YEAR(t.fechaCreacion) = :year AND (f.responsable.id = :idResponsable OR :idResponsable IS NULL)) AS string), " +
            "CAST((SELECT COUNT(f.estado) FROM Traza t JOIN t.expediente e JOIN e.documentoLegal f WHERE t.actual = true AND f.estado = dl.estado AND MONTH(t.fechaCreacion) = 5 AND YEAR(t.fechaCreacion) = :year AND (f.responsable.id = :idResponsable OR :idResponsable IS NULL)) AS string), " +
            "CAST((SELECT COUNT(f.estado) FROM Traza t JOIN t.expediente e JOIN e.documentoLegal f WHERE t.actual = true AND f.estado = dl.estado AND MONTH(t.fechaCreacion) = 6 AND YEAR(t.fechaCreacion) = :year AND (f.responsable.id = :idResponsable OR :idResponsable IS NULL)) AS string), " +
            "CAST((SELECT COUNT(f.estado) FROM Traza t JOIN t.expediente e JOIN e.documentoLegal f WHERE t.actual = true AND f.estado = dl.estado AND MONTH(t.fechaCreacion) = 7 AND YEAR(t.fechaCreacion) = :year AND (f.responsable.id = :idResponsable OR :idResponsable IS NULL)) AS string), " +
            "CAST((SELECT COUNT(f.estado) FROM Traza t JOIN t.expediente e JOIN e.documentoLegal f WHERE t.actual = true AND f.estado = dl.estado AND MONTH(t.fechaCreacion) = 8 AND YEAR(t.fechaCreacion) = :year AND (f.responsable.id = :idResponsable OR :idResponsable IS NULL)) AS string), " +
            "CAST((SELECT COUNT(f.estado) FROM Traza t JOIN t.expediente e JOIN e.documentoLegal f WHERE t.actual = true AND f.estado = dl.estado AND MONTH(t.fechaCreacion) = 9 AND YEAR(t.fechaCreacion) = :year AND (f.responsable.id = :idResponsable OR :idResponsable IS NULL)) AS string), " +
            "CAST((SELECT COUNT(f.estado) FROM Traza t JOIN t.expediente e JOIN e.documentoLegal f WHERE t.actual = true AND f.estado = dl.estado AND MONTH(t.fechaCreacion) = 10 AND YEAR(t.fechaCreacion) = :year AND (f.responsable.id = :idResponsable OR :idResponsable IS NULL)) AS string), " +
            "CAST((SELECT COUNT(f.estado) FROM Traza t JOIN t.expediente e JOIN e.documentoLegal f WHERE t.actual = true AND f.estado = dl.estado AND MONTH(t.fechaCreacion) = 11 AND YEAR(t.fechaCreacion) = :year AND (f.responsable.id = :idResponsable OR :idResponsable IS NULL)) AS string), " +
            "CAST((SELECT COUNT(f.estado) FROM Traza t JOIN t.expediente e JOIN e.documentoLegal f WHERE t.actual = true AND f.estado = dl.estado AND MONTH(t.fechaCreacion) = 12 AND YEAR(t.fechaCreacion) = :year AND (f.responsable.id = :idResponsable OR :idResponsable IS NULL)) AS string) " +
            ") " +
            "FROM HcDocumentoLegal dl GROUP BY dl.estado")
    List<DatosR4> filtrarReporteR4(@Param("year") Integer year, @Param("idResponsable") Integer idResponsable);


    @Query(value = "SELECT dbo.formato_fecha(dbo.obtener_fecha_inicio(:idDocumentoLegal)) AS FECHA", nativeQuery = true)
    Date obtenerFechaInicioDocLegal(@Param("idDocumentoLegal") Integer idDocumentoLegal);

    @Query(value = "SELECT dbo.formato_fecha(dbo.obtener_fecha_vencimiento(:idDocumentoLegal)) AS FECHA", nativeQuery = true)
    Date obtenerFechaVctoDocLegal(@Param("idDocumentoLegal") Integer idDocumentoLegal);

    @Query("SELECT hc FROM HcDocumentoLegal hc LEFT JOIN FETCH hc.expediente e WHERE hc.id=(SELECT ad.contrato.id FROM HcAdenda ad WHERE ad.id=:idAdenda)")
    HcDocumentoLegal buscarContratoPorAdenda(@Param("idAdenda") Integer idAdenda);


    @Query("SELECT new com.hochschild.speed.back.model.bean.registrarSolicitud.ContratoSumillaBean (" +
            "  hc.id" +
            ", concat(hc.numero,' - ', hc.sumilla) " +
            ") FROM HcDocumentoLegal hc " +
            "WHERE  hc.id = :idHcDocLegal"
    )
    ContratoSumillaBean findContratoSumillaBeanByIdHcDocLegal(@Param("idHcDocLegal") Integer id);

}
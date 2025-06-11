package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Expediente;
import com.hochschild.speed.back.model.domain.speed.utils.FilaBandejaEntradaDTS;
import com.hochschild.speed.back.model.reporte.ExpedientePorAreaBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public interface ExpedienteRepository extends JpaRepository<Expediente, Integer> {
    @Query("select e from Expediente e where e.id = :idExpediente")
    Expediente findById(@Param("idExpediente") Integer idExpediente);
    @Query("SELECT e FROM Expediente e where e.numero=:numero")
    Expediente buscarExpediente(@Param("numero") String numero);

    @Query("SELECT new com.hochschild.speed.back.model.domain.speed.utils.FilaBandejaEntradaDTS(ex.documentoLegal.numero, "
            + "ex.documentoLegal.area.compania.nombre, "
            + "ex.documentoLegal.contraparte.razonSocial, "
            + "ex.documentoLegal.sumilla, "
            + "ex.documentoLegal.estado, "
            + "ex.documentoLegal.fechaSolicitud, "
            + "ex.documentoLegal.fechaBorrador, "
            + "ex.proceso.nombre) "
            + " FROM Expediente ex WHERE ex.id in :idsExpediente")
    List<FilaBandejaEntradaDTS> obtenerFilasBandejaExport(ArrayList<Integer> idsExpediente);

    @Query("select e from Expediente e where e.id = :idExpediente")
    Expediente findExpedienteById(@Param("idExpediente") Integer idExpediente);

    @Query("SELECT e FROM Expediente e LEFT JOIN FETCH e.documentos d where e.id=:idExpediente")
    Expediente obtenerExpedienteConDocumentos(@Param("idExpediente") Integer idExpediente);

    @Query("SELECT new com.hochschild.speed.back.model.reporte.ExpedientePorAreaBean(" +
            "aa.id, " +
            "aa.numero, " +
            "aa.titulo, " +
            "bb.usuario, " +
            "aa.fechaCreacion, " +
            "CONCAT(cc.nombre, ' ', cc.apellidoPaterno, ' ', cc.apellidoMaterno ), " +
            "cc.razonSocial, " +
            "dd.nombre, " +
            "aa.estado, " +
            "(SELECT a.nombre FROM Area a WHERE a.id = (SELECT c.area.id FROM Usuario c WHERE c.id = (SELECT x.remitente.id FROM Traza x WHERE x.id = (SELECT MIN(a2.id) FROM Traza a2 WHERE a2.expediente.id = aa.id)))), " +
            "(SELECT s.nombre FROM Sede s WHERE s.id = (SELECT a.sede.id FROM Area a WHERE a.id = (SELECT c.area.id FROM Usuario c WHERE c.id = (SELECT x.remitente.id FROM Traza x WHERE x.id = (SELECT MIN(a2.id) FROM Traza a2 WHERE aa.id = a2.expediente.id))))), " +
            "(SELECT a.nombre FROM Area a WHERE a.id = (SELECT c.area.id FROM Usuario c WHERE c.id = (SELECT MAX(ut.id.usuario.id) FROM UsuarioPorTraza ut WHERE ut.id.traza.id = (SELECT MIN(a.id) FROM Traza a WHERE a.expediente.id = aa.id)))), " +
            "(SELECT s.nombre FROM Sede s WHERE s.id = (SELECT a.sede.id FROM Area a WHERE a.id = (SELECT c.area.id FROM Usuario c WHERE c.id = (SELECT MAX(ut.id.usuario.id) FROM UsuarioPorTraza ut WHERE ut.id.traza.id =(SELECT MIN(a.id) FROM Traza a WHERE a.expediente.id = aa.id))))), " +
            "(SELECT tr.fechaCreacion FROM Traza tr WHERE tr.id = (SELECT MAX(t.id) FROM Traza t WHERE t.expediente.id = aa.id))" +
            ") " +
            "FROM Expediente aa JOIN aa.creador bb LEFT JOIN aa.cliente cc JOIN aa.proceso dd " +
            "WHERE (bb.id = :idUsuario OR :idUsuario IS NULL ) AND (dd.id = :idProceso OR :idProceso IS NULL) " +
            "AND (aa.numero LIKE %:numeroExpediente%) " +
            "AND (aa.fechaCreacion BETWEEN :fechaInicio AND :fechaFin) " +
            "AND (aa.estado = :estado OR :estado IS NULL) " +
            "AND (:areaCreadora IS NULL OR :areaCreadora = (SELECT us.area FROM Usuario us WHERE us.id = (SELECT MAX(tr.remitente.id) FROM Traza tr WHERE tr.expediente.id = aa.id AND tr.padre IS NULL AND tr.orden = 1 ))) " +
            "AND (:areaDestino IS NULL OR :areaDestino = (SELECT u.area.id FROM Usuario u WHERE u.id = (SELECT MAX(ut.id.usuario.id) FROM UsuarioPorTraza  ut WHERE ut.id.traza.id = (SELECT MIN(a.id) FROM Traza a WHERE a.expediente.id = aa.id AND a.padre IS NULL)))) " +
            "AND (:sedeOrigen IS NULL OR :sedeOrigen = (SELECT a.sede.id FROM Area a WHERE a.id = (SELECT c.area.id FROM Usuario c WHERE c.id = (SELECT x.remitente.id FROM Traza x WHERE x.id = (SELECT MIN(a.id) FROM Traza a WHERE a.expediente.id = aa.id AND a.padre IS NULL)))))" +
            "AND (:sedeDestino IS NULL OR :sedeDestino = (SELECT a.sede.id FROM Area a WHERE a.id = (SELECT c.area.id FROM Usuario c WHERE c.id = (SELECT ut.id.usuario.id FROM UsuarioPorTraza ut WHERE ut.id.traza.id = (SELECT MIN(a.id) FROM Traza a WHERE a.expediente.id = aa.id AND a.padre IS NULL))))) " +
            "AND (:idUsuarioFinal IS NULL OR :idUsuarioFinal IN (SELECT a.id.usuario.id FROM UsuarioPorTraza a WHERE a.id.traza.id = (SELECT MAX(a.id) FROM Traza a WHERE a.expediente.id = aa.id AND a.padre IS NULL))) " +
            "AND (:fechaUltDerivacion IS NULL OR CAST(:fechaUltDerivacion AS date) = (SELECT CAST(tr.fechaCreacion AS date) FROM Traza tr WHERE tr.id = (SELECT MAX(t.id) FROM Traza t WHERE t.expediente.id = aa.id AND t.padre IS NULL )))" +
            "ORDER BY aa.id" )
    List<ExpedientePorAreaBean> filtrarExpedientesPorArea(@Param("idUsuario") Integer idUsuario,
                                                          @Param("idProceso") Integer idProceso,
                                                          @Param("numeroExpediente") String numeroExpediente,
                                                          @Param("fechaInicio")Date fechaInicio,
                                                          @Param("fechaFin") Date fechaFin,
                                                          @Param("estado") Character estado,
                                                          @Param("areaCreadora") Integer areaCreadora,
                                                          @Param("areaDestino") Integer areaDestino,
                                                          @Param("sedeOrigen") Integer sedeOrigen,
                                                          @Param("sedeDestino") Integer sedeDestino,
                                                          @Param("idUsuarioFinal") Integer idUsuarioFinal,
                                                          @Param("fechaUltDerivacion") Date fechaUltDerivacion);
}
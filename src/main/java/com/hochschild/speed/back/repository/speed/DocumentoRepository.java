package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Documento;
import com.hochschild.speed.back.model.reporte.DocumentoPorAreaBean;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Integer> {
    @Query("select e from Documento e where e.id = :idDocumento")
    Documento findById(@Param("idDocumento") Integer idDocumento);

    @Query("SELECT e FROM Documento e WHERE e.expediente.id = :idExpediente ORDER BY e.fechaCreacion")
    List<Documento> obtenerPorExpediente(@Param("idExpediente") Integer idExpediente);

    @Query("SELECT m.documentos FROM Mensajeria m WHERE m.id = :idMensajeria")
    List<Documento> obtenerPorMensajeria(@Param("idMensajeria") Integer idMensajeria);

    @Query("SELECT d FROM Documento d WHERE d.expediente.id = :idExpediente AND d.tipoDocumento.id = :idTipoDocumento ORDER BY d.fechaCreacion")
    List<Documento> obtenerPorExpedienteYTipoDocumento(@Param("idExpediente") Integer idExpediente, @Param("idTipoDocumento") Integer idTipoDocumento);

    @Query("SELECT d FROM Documento d WHERE d.expediente.id = :idExpediente AND d.tipoDocumento.id = :idTipoDocumento ORDER BY d.fechaCreacion")
    Documento obtenerDocumentoPorExpedienteYTipoDocumento (@Param("idExpediente") Integer idExpediente,
                                                           @Param("idTipoDocumento") Integer idTipoDocumento);

    Documento findFirstByExpedienteIdAndTipoDocumentoIdOrderByFechaCreacionAsc(Integer idExpediente, Integer idTipoDocumento);

    //Queries for Reporting
    @Query(value = "SELECT new com.hochschild.speed.back.model.reporte.DocumentoPorAreaBean(c.nombre, d.nombre, a.numero, a.fechaCreacion, a.titulo, e.numero, e.estado, (SELECT f.nombre FROM Usuario g JOIN g.area f WHERE g.id = (SELECT h.remitente.id FROM Traza h WHERE h.id = (SELECT MAX (c.id) FROM Traza c WHERE c.expediente.id = a.expediente.id)) )) " +
            "FROM Documento a " +
            " JOIN a.autor b " +
            " JOIN b.area c " +
            " JOIN a.tipoDocumento d " +
            " JOIN a.expediente e " +
            "WHERE (c.id = :idArea OR :idArea IS NULL) " +
            "AND (d.id = :idTipoDocumento OR :idTipoDocumento IS NULL) " +
            "AND " +
            "(a.fechaCreacion BETWEEN :fechaInicio AND :fechaFin) " +
            "AND (:idAreaActual IS NULL OR :idAreaActual = (SELECT g.area.id FROM Usuario g WHERE g.id = (SELECT MAX(uxt.id.usuario.id) FROM UsuarioPorTraza uxt WHERE uxt.id.traza.id = (SELECT MAX(c.id) FROM Traza c WHERE c.expediente.id = a.expediente.id AND c.padre IS NULL) AND uxt.responsable = true) )) AND " +
            "(:estado IS NULL OR e.estado = :estado) " +
                    "AND ((e.numero LIKE '%'+:numero+'%') OR (LENGTH(TRIM(:numero)) = 0 )) ORDER BY  a.fechaCreacion DESC")
    List<DocumentoPorAreaBean> filterDocumentoPorArea(@Param("idArea") Integer idArea, @Param("idAreaActual") Integer idAreaActual, @Param("idTipoDocumento") Integer idTipoDocumento, @Param("fechaInicio")Date fechaInicio, @Param("fechaFin")Date fechaFin, @Param("estado") Character estado, @Param("numero") String numero );


}
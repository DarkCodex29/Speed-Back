package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Traza;
import com.hochschild.speed.back.model.domain.speed.utils.FilaBandejaEntradaDTS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface TrazaRepository extends JpaRepository<Traza, Integer> {
    @Query("SELECT e FROM Traza e WHERE e.id = :idTraza")
    Traza findById(@Param("idTraza") Integer idTraza);
    @Query("SELECT t from Traza t where t.expediente.id=:idExpediente and (t.accion=:accion or t.accion=:accion_manual) and t.orden <= 1")
    Traza obtenerPrimeraTrazaxExpediente(@Param("idExpediente") Integer idExpediente, @Param("accion") String accion, @Param("accion_manual") String accion_manual);

    @Query("SELECT t from Traza t where t.expediente.id = :idExpediente and t.actual = true ORDER BY t.id ")
    Traza obtenerUltimaTrazaPorExpediente(@Param("idExpediente") Integer idExpediente);

    @Query("SELECT new com.hochschild.speed.back.model.domain.speed.utils.FilaBandejaEntradaDTS(t.expediente.documentoLegal.numero, "
            + "t.expediente.documentoLegal.area.compania.nombre, "
            + "t.expediente.documentoLegal.contraparte.razonSocial, "
            + "t.expediente.documentoLegal.sumilla, "
            + "t.expediente.documentoLegal.estado, "
            + "t.expediente.documentoLegal.fechaSolicitud, "
            + "t.expediente.documentoLegal.fechaBorrador, "
            + "t.expediente.proceso.nombre) "
            + " FROM Traza t WHERE t.id in :idsTraza")
    List<FilaBandejaEntradaDTS> obtenerFilasBandejaExport(ArrayList<Integer> idsTraza);
    @Query("SELECT t FROM Traza t WHERE t.padre.id = :idPadre")
    List<Traza> obtenerPorPadre(@Param("idPadre") Integer idPadre);

    @Query("SELECT t FROM Traza t WHERE t.expediente.id = :idExpediente ORDER BY t.fechaCreacion ASC")
    List<Traza> obtenerTrazaPorExpediente(@Param("idExpediente") Integer idExpediente);

    @Query("SELECT u, upt FROM Usuario u, UsuarioPorTraza upt WHERE upt.id.traza.id = :idTraza AND upt.id.usuario.id=u.id AND upt.responsable=:responsable")
    List<Object[]> obtenerDestinatarios(@Param("idTraza") Integer idTraza, @Param("responsable") boolean responsable);

    @Query("SELECT COUNT(t) FROM Traza t WHERE t.expediente.id = :idExpediente AND t.accion = 'Adjuntar Borrador'")
    int obtenerNumeroBorradores(@Param("idExpediente") Integer idExpediente);
}
package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcGrupo;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcGrupoRepository extends JpaRepository<HcGrupo, Integer> {
    @Query("SELECT e FROM HcGrupo e WHERE e.id = :idHcGrupo")
    HcGrupo findById(@Param("idHcGrupo") Integer idHcGrupo);
    @Query("SELECT e FROM HcGrupo e WHERE " +
            "(e.nombre LIKE %:nombre% OR :nombre IS NULL) AND " +
            "e.estado = 'A' ORDER BY e.id DESC")
    List<HcGrupo> list(@Param("nombre") String nombre);
    @Query("SELECT hc FROM HcGrupo hc WHERE hc.nombre=:nombre")
    HcGrupo obtenerGrupoPorNombre(@Param("nombre") String nombre);
    @Query("SELECT hc.id.usuario FROM HcUsuarioPorGrupo hc WHERE hc.id.grupo.id=:idGrupo")
    List<Usuario> obtenerUsuariosGrupo(@Param("idGrupo") Integer idGrupo);
    @Query("SELECT hc.id.grupo FROM HcUsuarioPorGrupo hc WHERE hc.id.usuario.id=:idUsuario")
    List<HcGrupo> obtenerGrupoPorUsuario(@Param("idUsuario") Integer idUsuario);
    @Query("SELECT hc.id.usuario FROM HcUsuarioPorGrupo hc WHERE hc.id.grupo.id=:idGrupo")
    List<Usuario> obtenerUsuariosGrupoSolicitud(@Param("idGrupo") Integer idGrupo);
    @Query("SELECT hc FROM HcGrupo hc WHERE hc.estado = 'A'")
    List<HcGrupo> obtenerActivos();
}
package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    @Query("SELECT r FROM Rol r WHERE r.estado = 'A' ORDER BY r.nombre")
    List<Rol> getRolesActivos();

    @Query("SELECT e FROM Rol e WHERE e.id = :idRol")
    Rol findById(@Param("idRol") Integer idRol);

    @Query("SELECT e FROM Rol e where e.codigo = :codigo")
    Rol findByCodigo(@Param("codigo") String codigo);

    @Query("SELECT r FROM Rol r,Usuario u where r not in ELEMENTS(u.roles) AND u.id = :idUsuario AND u.estado = 'A' AND r.estado = 'A' ORDER BY r.nombre")
    List<Rol> buscarRolesNoAsignadosUsuario(@Param("idUsuario") Integer idUsuario);

    @Query("SELECT r FROM Rol r,Proceso u where r not in ELEMENTS(u.rolesParticipantes) AND u.id =:idProceso AND u.estado='A' AND r.estado='A' ORDER BY r.nombre")
    List<Rol> obtenerRolesParticipantesNoAsignados(@Param("idProceso") Integer idProceso);

    @Query("SELECT r FROM Rol r,Proceso u where r not in ELEMENTS(u.rolesCreadores) AND u.id =:idProceso AND u.estado='A' AND r.estado='A' ORDER BY r.nombre")
    List<Rol> obtenerRolesProcesosNoAsignados(@Param("idProceso") Integer idProceso);

    @Query("SELECT r FROM Rol r,Usuario u where r in ELEMENTS(u.roles) AND u.id = :idUsuario AND u.estado = 'A' ORDER BY r.nombre")
    List<Rol> obtenerAsignadosUsuario(@Param("idUsuario") Integer idUsuario);

    @Query("SELECT r FROM Rol r,Proceso p WHERE r IN ELEMENTS(p.rolesParticipantes) AND p.id=:idProceso AND r.estado = 'A' ORDER BY r.nombre")
    List<Rol> obtenerParticipantesPorProceso(@Param("idProceso") Integer idProceso);

    @Query("SELECT r FROM Rol r WHERE r IN (SELECT ELEMENTS(u.roles) FROM Usuario u WHERE u.id = :idUsuario) AND r.estado = 'A'")
    List<Rol> buscarActivosPorUsuario(@Param("idUsuario") Integer idUsuario);

    @Query("Select r.codigo FROM Rol r,Usuario u where r in ELEMENTS(u.roles) AND u.id = :idUsuario AND u.estado = 'A' ORDER BY r.nombre")
    List<String> obtenerAsignadosUsuarioString(@Param("idUsuario") Integer idUsuario);
    @Query("SELECT e FROM Rol e WHERE " +
            " (e.nombre LIKE %:nombre% OR :nombre IS NULL) " +
            "ORDER BY e.fechaCreacion DESC")
    List<Rol> list(@Param("nombre") String nombre);
}
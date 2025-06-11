package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Rol;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.domain.speed.utils.InteresadoDTS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("SELECT u FROM Usuario u WHERE u.id = :idUsuario AND u.estado = 'A'")
    Usuario findById(@Param("idUsuario") Integer idUsuario);
    @Query("SELECT e FROM Usuario e WHERE " +
            "(e.usuario LIKE %:usuario% OR :usuario IS NULL) AND " +
            "(e.nombres LIKE %:nombres% OR :nombres IS NULL) AND " +
            "(e.apellidos LIKE %:apellidos% OR :apellidos IS NULL) AND " +
            "e.estado = 'A' ORDER BY e.id ASC")
    List<Usuario> list(@Param("usuario") String usuario,
                       @Param("nombres") String nombres,
                       @Param("apellidos") String apellidos);
    @Query("SELECT r FROM Usuario r,Proceso u where r not in ELEMENTS(u.usuariosParticipantes) AND u.id =:idProceso AND u.estado='A' AND r.estado='A' ORDER BY r.id ASC")
    List<Usuario> obtenerNoAsignadosUsuario(@Param("idProceso") Integer idProceso);
    @Query("SELECT e FROM Usuario e WHERE " +
            "(e.id <> :idUsuario) AND " +
            "e.estado = 'A' ORDER BY e.id DESC")
    List<Usuario> listJefes(@Param("idUsuario") Integer idUsuario);
    @Query("SELECT u FROM Usuario u WHERE u.usuario = :username AND u.estado = 'A'")
    Usuario findByUsername(@Param("username") String username);
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.usuario)=LOWER(:usuario) AND u.estado = 'A'")
    Usuario obtenerPorUsuario(@Param("usuario") String usuario);
    @Query("SELECT new Usuario(u.id,u.usuario,u.nombres,u.apellidos,u.correo) FROM Usuario u WHERE (SELECT COUNT(r) FROM Rol r WHERE r in elements(u.roles) AND r.codigo = :codigo) > 0 AND u.estado = 'A'")
    List<Usuario> buscarUsuariosRol(@Param("codigo") String codigo);
    @Query(value = "SELECT * FROM Usuario u " +
            "inner join USUARIO_POR_PROCESO uppro on u.id_usuario =uppro.usuario " +
            "inner join PROCESO p on uppro.proceso = p.id_proceso " +
            "WHERE u.id_usuario in (select upp.usuario from USUARIO_POR_PROCESO upp) " +
            " or u.id_usuario in (select upr.usuario from USUARIO_POR_ROL upr " +
            " where upr.rol in (SELECT rr.id_rol from RESPONSABLE_ROL rr) " +
            "AND p.id =:idProceso " +
            "AND u.estado = 'A'" , nativeQuery = true)
    List<Usuario> obtenerResponsablesPorProceso(@Param("idProceso") Integer idProceso);
    @Query("SELECT NEW Usuario(p.id.usuario.id, "
            + "p.id.usuario.usuario, "
            + "p.id.usuario.nombres, "
            + "p.id.usuario.apellidos, "
            + "p.id.usuario.correo) FROM HcUsuarioPorGrupo p WHERE p.id.usuario.estado=:estado AND p.id.grupo.id=:idGrupo")
    List<Usuario> obtenerUsuariosxGrupo(@Param("estado") Character estado, @Param("idGrupo") Integer idGrupo);

    @Query("SELECT new Usuario(u.id,u.usuario,u.nombres,u.apellidos,u.correo,u.area.id,u.area.nombre) FROM Usuario u WHERE u.id=:idUsuario AND u.estado='A'")
    Usuario obtenerPorId(@Param("idUsuario") Integer idUsuario);

    @Query("SELECT new com.hochschild.speed.back.model.domain.speed.utils.InteresadoDTS('grupo', gr.id, 'Grupo - '||gr.nombre) FROM HcGrupo gr WHERE lower(dbo.unaccent(gr.nombre)) like :termino AND gr.estado= 'A' ORDER BY gr.nombre")
    List<InteresadoDTS>buscarInteresados(@Param("termino") String termino);

    @Query("SELECT new com.hochschild.speed.back.model.domain.speed.utils.InteresadoDTS('usuario', u.id, u.nombres||' '||u.apellidos) FROM Usuario u WHERE lower(dbo.unaccent(u.nombres||' '||u.apellidos)) like :termino AND u.estado= 'A' ORDER BY u.nombres,u.apellidos")
    List<InteresadoDTS>buscarInteresadosComplemento(@Param("termino") String termino);

    @Query("SELECT new com.hochschild.speed.back.model.domain.speed.utils.InteresadoDTS('grupo', gr.id, 'Grupo - '||gr.nombre) FROM HcGrupo gr WHERE lower(dbo.unaccent(gr.nombre)) like :termino AND gr.estado= 'A' AND gr.tipoGrupo.tipo=:tipoParametroGrupo AND gr.tipoGrupo.valor=:valorParametroGrupoSeguridad ORDER BY gr.nombre")
    List<InteresadoDTS>buscarInteresadosSeguridad(@Param("termino") String termino,
                                                  @Param("tipoParametroGrupo") String tipoParametroGrupo,
                                                  @Param("valorParametroGrupoSeguridad") String valorParametroGrupoSeguridad);

    @Query("SELECT new com.hochschild.speed.back.model.domain.speed.utils.InteresadoDTS('usuario', u.id, u.nombres||' '||u.apellidos) FROM Usuario u WHERE lower(dbo.unaccent(u.nombres||' '||u.apellidos)) like :termino AND u.estado= 'A' ORDER BY u.nombres,u.apellidos")
    List<InteresadoDTS>buscarInteresadosSeguridadComplemento(@Param("termino") String termino);

    @Query("SELECT new com.hochschild.speed.back.model.domain.speed.utils.InteresadoDTS('usuario', u.id, u.nombres||' '||u.apellidos) FROM Usuario u,Rol r WHERE r in elements(u.roles) AND r.codigo=:codigoRol AND u.estado=:estado AND lower(dbo.unaccent(u.nombres||' '||u.apellidos)) like :termino ORDER BY u.nombres,u.apellidos")
    List<InteresadoDTS> buscarInteresadosPorRol(@Param("termino") String termino, @Param("estado") Character estado, @Param("codigoRol") String codigoRol );

    @Query("SELECT new Usuario(u.id,u.usuario, u.nombres, u.apellidos, u.correo) FROM Usuario u WHERE lower(dbo.unaccent(u.nombres||' '||u.apellidos)) like :termino AND u.estado = 'A' ORDER BY u.nombres,u.apellidos")
    List<Usuario> buscarUsuariosActivosPorNombre(@Param("termino") String termino);

    @Query("SELECT new Usuario(u.id,u.usuario, u.nombres, u.apellidos, u.correo) FROM Usuario u WHERE u.estado = 'A' ORDER BY u.usuario")
    List<Usuario> obtenerTodosUsuariosActivos();

    @Query("SELECT new Usuario(u.id,u.usuario,u.nombres,u.apellidos,u.correo,u.area.id,u.area.nombre) FROM Usuario u,Proceso p WHERE u.estado=:estado and p.id=:idProceso AND (u in elements(p.usuariosParticipantes) OR u.id IN (SELECT u1.id FROM Usuario u1 JOIN u1.roles AS r WHERE r IN ELEMENTS(p.rolesParticipantes))) GROUP BY u.id,u.usuario,u.nombres,u.apellidos,u.correo,u.area.id,u.area.nombre ORDER BY u.nombres,u.apellidos")
    List<Usuario> obtenerParticipantesPorProceso(@Param("idProceso") Integer idProceso);
}
package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.UsuarioPorTraza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioPorTrazaRepository extends JpaRepository<UsuarioPorTraza, Integer> {
    @Query("SELECT e FROM UsuarioPorTraza e WHERE e.id = :idUsuarioPorTraza")
    UsuarioPorTraza findById(@Param("idUsuarioPorTraza") Integer idUsuarioPorTraza);
    @Query("SELECT ut FROM UsuarioPorTraza ut WHERE ut.id.traza.id=:idTraza and ut.responsable=true")
    UsuarioPorTraza buscarUsuarioPorTrazaPorId(@Param("idTraza") Integer idTraza);
    @Query("SELECT ut FROM UsuarioPorTraza ut WHERE ut.id.traza.id = :idTraza and ut.id.usuario.id = :idUsuario")
    UsuarioPorTraza obtenerUsuarioPorTraza(@Param("idTraza") Integer idTraza,
                                           @Param("idUsuario") Integer idUsuario);
    @Query("SELECT ut FROM UsuarioPorTraza ut WHERE ut.id.traza.id=:idTraza and ut.id.usuario.id=:idUsuario")
    UsuarioPorTraza buscarUsuarioPorTraza(@Param("idTraza") Integer idTraza,
                                          @Param("idUsuario") Integer idUsuario);
    @Query("SELECT upt FROM UsuarioPorTraza upt WHERE upt.id.traza.id=:idTraza")
    List<UsuarioPorTraza> obtenerPorTraza(@Param("idTraza") Integer idTraza);
    @Query("SELECT ut FROM UsuarioPorTraza ut WHERE ut.id.traza.id=:idTraza and ut.id.usuario.id<>:idUsuario")
    List<UsuarioPorTraza> buscarTrazasNoUsuario(@Param("idTraza") Integer idTraza,
                                               @Param("idUsuario") Integer idUsuario);
}
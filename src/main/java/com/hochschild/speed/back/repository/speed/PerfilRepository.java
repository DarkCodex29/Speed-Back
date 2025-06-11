package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    @Query("SELECT e FROM Perfil e WHERE e.id = :idPerfil")
    Perfil findById(@Param("idPerfil") Integer idPerfil);
    @Query("SELECT p FROM Perfil p WHERE p.estado= 'A' ORDER BY p.nombre")
    List<Perfil> getActivos();

    @Query("SELECT p FROM Perfil p,Usuario u WHERE p NOT IN ELEMENTS(u.perfiles) AND u.id = :idUsuario AND u.estado = 'A' ORDER BY p.nombre")
    List<Perfil> buscarPerfilesNoAsignadosUsuario(@Param("idUsuario") Integer idUsuario);
    @Query("SELECT e FROM Perfil e WHERE " +
            " (e.nombre LIKE %:nombre% OR :nombre IS NULL) " +
            "ORDER BY e.fechaCreacion DESC")
    List<Perfil> list(@Param("nombre") String nombre);
}
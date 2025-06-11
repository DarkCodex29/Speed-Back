package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.UsuarioPorRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioPorRolRepository extends JpaRepository<UsuarioPorRol, Integer> {
    @Query("SELECT e FROM UsuarioPorRol e WHERE e.id = :idUsuarioPorRol")
    UsuarioPorRol findById(@Param("idUsuarioPorRol") Integer idUsuarioPorRol);

    @Query("select e from UsuarioPorRol e where e.id.rol.id= :idRol AND e.id.usuario.estado = 'A'")
    List<UsuarioPorRol> findByIdRol(@Param("idRol") Integer idRol);
}
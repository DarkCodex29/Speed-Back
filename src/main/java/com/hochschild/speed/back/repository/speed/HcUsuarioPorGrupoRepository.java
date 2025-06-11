package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcUsuarioPorGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcUsuarioPorGrupoRepository extends JpaRepository<HcUsuarioPorGrupo, Integer> {
    @Query("SELECT ug FROM HcUsuarioPorGrupo ug WHERE ug.id.grupo.id = :idGrupo")
    List<HcUsuarioPorGrupo> findByIdGrupo(@Param("idGrupo") Integer idGrupo);
    @Modifying
    @Query("DELETE FROM HcUsuarioPorGrupo ug WHERE ug.id.grupo.id = :idGrupo")
    void eliminarUsuariosPorGrupo(@Param("idGrupo") Integer idGrupo);
}
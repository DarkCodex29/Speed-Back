package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Integer> {
    @Query("SELECT e FROM Recurso e WHERE e.id = :idRecurso")
    Recurso findById(@Param("idRecurso") Integer idRecurso);
}
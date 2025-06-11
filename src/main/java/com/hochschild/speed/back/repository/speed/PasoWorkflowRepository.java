package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.PasoWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PasoWorkflowRepository extends JpaRepository<PasoWorkflow, Integer> {
    @Query("select e from PasoWorkflow e where e.id = :idPasoWorkflow")
    PasoWorkflow findById(@Param("idPasoWorkflow") Integer idPasoWorkflow);
    @Query("select w from PasoWorkflow w where w.workflow.id = :idWorkflow AND w.primero = true")
    PasoWorkflow obtenerPrimerPaso(@Param("idWorkflow") Integer idWorkflow);
}
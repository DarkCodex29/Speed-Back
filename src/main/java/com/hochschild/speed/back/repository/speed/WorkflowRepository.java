package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Integer> {
    @Query("select e from Workflow e where e.id = :idWorkflow")
    Workflow findById(@Param("idWorkflow") Integer idWorkflow);
    @Query("select w from Workflow w where w.proceso.id = :idProceso AND w.version = (SELECT max(w1.version) FROM Workflow w1 WHERE w1.proceso.id = :idProceso)")
    Workflow obtenerUltimaVersionPorProceso(@Param("idProceso") Integer idProceso);
    @Query("SELECT e FROM Workflow e WHERE " +
            " (e.descripcion LIKE %:descripcion% OR :descripcion IS NULL) AND " +
            " (e.nombre LIKE %:nombre% OR :nombre IS NULL) " +
            "ORDER BY e.id DESC")
    List<Workflow> list(@Param("descripcion") String descripcion,
                        @Param("nombre") String nombre);
}
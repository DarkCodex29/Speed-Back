package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Proceso;
import com.hochschild.speed.back.model.domain.speed.Reemplazo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface ReemplazoRepository extends JpaRepository<Reemplazo, Integer> {
    @Query("SELECT e FROM Reemplazo e WHERE e.id = :idReemplazo")
    Reemplazo findById(@Param("idReemplazo") Integer idReemplazo);

    @Query("SELECT r FROM Reemplazo r WHERE r.reemplazante.id= :idReemplazante AND :proceso IN elements(r.procesos) AND :fecha BETWEEN r.desde AND r.hasta")
    Reemplazo buscarReemplazo(@Param("idReemplazante") Integer idReemplazante,
                              @Param("proceso") Proceso proceso,
                              @Param("fecha") Date fecha);
    @Query("SELECT e FROM Reemplazo e WHERE " +
            "(e.reemplazante.nombres LIKE %:reemplazante% OR :reemplazante IS NULL) AND " +
            "(e.reemplazado.nombres LIKE %:reemplazado% OR :reemplazado IS NULL) " +
            "ORDER BY e.id DESC")
    List<Reemplazo> list(@Param("reemplazante") String reemplazante,
                         @Param("reemplazado") String reemplazado);
}
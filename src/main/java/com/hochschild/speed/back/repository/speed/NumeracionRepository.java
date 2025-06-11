package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Numeracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NumeracionRepository extends JpaRepository<Numeracion, Integer> {
    @Query("SELECT e FROM Numeracion e WHERE e.id = :idNumeracion")
    Numeracion findById(@Param("idNumeracion") Integer idNumeracion);

    @Query("select e from Numeracion e where e.area.id = :idArea AND e.tipoDocumento.id = :idTipoDocumento")
    Numeracion numeracionPorAreayTipoDocumento(@Param("idArea") Integer idArea,
                                               @Param("idTipoDocumento") Integer idTipoDocumento);
    @Query("SELECT n from Numeracion n where n.id=:idNumeracion")
    Numeracion obtenerNumeracion(@Param("idNumeracion") Integer idNumeracion);
    @Query("SELECT e FROM Numeracion e WHERE " +
            "(e.valor = :numeroActual OR :numeroActual IS NULL) AND " +
            "(concat(e.preformato,e.postFormato) LIKE %:formato% OR :formato IS NULL) " +
            "ORDER BY e.id DESC")
    List<Numeracion> list(@Param("numeroActual") Integer numeroActual,
                          @Param("formato") String formato);
}
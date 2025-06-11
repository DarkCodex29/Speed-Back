package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Feriado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface FeriadoRepository extends JpaRepository<Feriado, Integer> {
    @Query("SELECT ad FROM Feriado ad WHERE ad.id = :idFeriado")
    Feriado findById(@Param("idFeriado") Integer idFeriado);
    @Query("SELECT f FROM Feriado f WHERE f.fecha=:fecha AND f.sede.id=:idSede")
    Feriado obtenerPorFechaSede(@Param("fecha") Date fecha, @Param("idSede") Integer idSede);
    @Query("SELECT e FROM Feriado e WHERE " +
            " (e.sede.nombre LIKE %:nombre% OR :nombre IS NULL) " +
            "ORDER BY e.fechaCreacion DESC")
    List<Feriado> list(@Param("nombre") String nombre);
}

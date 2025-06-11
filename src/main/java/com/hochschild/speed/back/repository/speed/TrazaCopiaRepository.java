package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.TrazaCopia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrazaCopiaRepository extends JpaRepository<TrazaCopia, Integer> {

    @Query("SELECT tc FROM TrazaCopia tc WHERE tc.referencia.id = :idTraza")
    List<TrazaCopia> obtenerPorTraza(@Param("idTraza") Integer idTraza);
}
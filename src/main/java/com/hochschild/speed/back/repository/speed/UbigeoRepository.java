package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Ubigeo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UbigeoRepository extends JpaRepository<Ubigeo, Integer> {
    @Query("SELECT e FROM Ubigeo e WHERE e.id = :idUbigeo")
    Ubigeo findById(@Param("idUbigeo") Integer idUbigeo);

    @Query("SELECT u FROM Ubigeo u WHERE u.padre is null")
    List<Ubigeo> obtenerDepartamentos();

    @Query("SELECT new Ubigeo(u.id,u.nombre) FROM Ubigeo u WHERE u.padre.id = :idPadre")
    List<Ubigeo> obtenerPorPadre(@Param("idPadre") Integer idPadre);
}
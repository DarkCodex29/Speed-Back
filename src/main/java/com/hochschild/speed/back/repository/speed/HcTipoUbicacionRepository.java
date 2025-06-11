package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcTipoUbicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcTipoUbicacionRepository extends JpaRepository<HcTipoUbicacion, Integer> {

    @Query("SELECT e FROM HcTipoUbicacion e WHERE e.id = :idHcTipoUbicacion")
    HcTipoUbicacion findById(@Param("idHcTipoUbicacion") Integer idHcTipoUbicacion);
    @Query("SELECT t FROM HcTipoUbicacion t WHERE t.codigo=:codigo")
    HcTipoUbicacion buscarPorCodigo(@Param("codigo") String codigo);
    @Query("SELECT p FROM HcTipoUbicacion p ORDER BY p.nombre ASC")
    List<HcTipoUbicacion> listarHcTipoUbicacion();
}
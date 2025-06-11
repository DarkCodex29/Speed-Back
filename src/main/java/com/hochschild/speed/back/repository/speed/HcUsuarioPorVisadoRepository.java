package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcUsuarioPorVisado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcUsuarioPorVisadoRepository extends JpaRepository<HcUsuarioPorVisado, Integer> {
    @Query("SELECT uv FROM HcUsuarioPorVisado uv WHERE uv.id.visado.id=:idVisado ORDER BY uv.orden ASC")
    List<HcUsuarioPorVisado> findByIdVisado(@Param("idVisado") Integer idVisado);
}
package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.AsistenteVCorreo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AsistenteVCorreoRepository extends JpaRepository<AsistenteVCorreo, Integer> {
    @Query("SELECT a FROM AsistenteVCorreo  a WHERE a.vigente = 'S'")
    List<AsistenteVCorreo> consultarCorreosVigentes();
}

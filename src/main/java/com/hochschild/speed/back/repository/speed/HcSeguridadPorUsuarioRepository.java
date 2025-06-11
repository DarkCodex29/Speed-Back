package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcSeguridadPorDocumentoLegal;
import com.hochschild.speed.back.model.domain.speed.HcSeguridadPorUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HcSeguridadPorUsuarioRepository extends JpaRepository<HcSeguridadPorUsuario, Integer> {
}

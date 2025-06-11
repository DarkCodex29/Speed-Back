package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.AsistenteVirtualUsabilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsistenteVirtualUsabilidadRepository extends JpaRepository<AsistenteVirtualUsabilidad, Integer> {
}

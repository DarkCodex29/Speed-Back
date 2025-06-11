package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.AsistenteVOtrasPreguntas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsistenteVOtrasPreguntasRepository extends JpaRepository<AsistenteVOtrasPreguntas, Integer> {
}

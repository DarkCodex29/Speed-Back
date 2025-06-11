package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.ClienteLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteLogRepository extends JpaRepository<ClienteLog, Integer> {

}

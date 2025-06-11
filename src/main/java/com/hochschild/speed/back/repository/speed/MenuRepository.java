package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    @Query("SELECT e FROM Menu e WHERE e.id = :idMenu")
    Menu findById(@Param("idMenu") Integer idMenu);
}
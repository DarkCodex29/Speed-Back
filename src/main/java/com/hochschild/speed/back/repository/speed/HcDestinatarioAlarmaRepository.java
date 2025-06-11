package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcDestinatarioAlarma;
import com.hochschild.speed.back.model.domain.speed.HcGrupo;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HcDestinatarioAlarmaRepository extends JpaRepository<HcDestinatarioAlarma, Integer> {
    @Query("select e from HcDestinatarioAlarma e where e.id = :idHcDestinatarioAlarma")
    HcDestinatarioAlarma findById(@Param("idHcDestinatarioAlarma") Integer idHcDestinatarioAlarma);

    @Query("select e from HcDestinatarioAlarma e where e.alarma.id = :idAlarma")
    List<HcDestinatarioAlarma> findByIdAlarma(@Param("idAlarma") Integer idAlarma);

    @Query("select e.id from HcDestinatarioAlarma e where e.usuario.id = :idUsuario and e.alarma.id = :idAlarma")
    List<Integer> findByIdUsuarioaAndIdAlarma(@Param("idUsuario") Integer idUsuario,@Param("idAlarma") Integer idAlarma);

    @Query("select e.id from HcDestinatarioAlarma e where e.grupo.id = :idUsuario and e.alarma.id = :idAlarma")
    List<Integer> findByGrupoAndIdAlarma(@Param("idUsuario") Integer idUsuario,@Param("idAlarma") Integer idAlarma);

    @Query("SELECT a FROM HcDestinatarioAlarma a WHERE a.alarma.id = :idAlarma")
    List<HcDestinatarioAlarma> obtenerDestinatariosAlarma(@Param("idAlarma") Integer idAlarma);

    @Query("SELECT a.usuario FROM HcDestinatarioAlarma a WHERE a.alarma.id = :idAlarma AND a.usuario IS NOT NULL")
    List<Usuario> obtenerUsuariosDestinatariosAlarmaPart1(@Param("idAlarma") Integer idAlarma);
    @Query("SELECT up.id.usuario FROM HcUsuarioPorGrupo up WHERE up.id.grupo.id IN (SELECT  a.grupo.id FROM HcDestinatarioAlarma a WHERE a.alarma.id = :idAlarma AND a.grupo IS NOT NULL) " +
            "AND up.id.usuario NOT IN (SELECT a2.usuario.id FROM HcDestinatarioAlarma a2 WHERE a2.alarma.id = :idAlarma AND a2.usuario IS NOT NULL)")
    List<Usuario> obtenerUsuariosDestinatariosAlarmaPart2(@Param("idAlarma") Integer idAlarma);
}
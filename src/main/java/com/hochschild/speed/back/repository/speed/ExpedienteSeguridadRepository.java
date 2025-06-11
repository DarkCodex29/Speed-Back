package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcSeguridadPorDocumentoLegal;
import com.hochschild.speed.back.model.domain.speed.HcSeguridadPorUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExpedienteSeguridadRepository extends JpaRepository<HcSeguridadPorDocumentoLegal, Integer> {
    @Query("select e from HcSeguridadPorDocumentoLegal e where e.id = :idHcSeguridad")
    HcSeguridadPorDocumentoLegal findById(@Param("idHcSeguridad") Integer idHcSeguridad);
    @Query("SELECT h FROM HcSeguridadPorDocumentoLegal h WHERE h.documentoLegal.id=:idDocumentoLegal AND h.eliminado=:eliminado")
    HcSeguridadPorDocumentoLegal obtenerHcSeguridadPorDocumentoLegalPorIdDocumentoLegal(@Param("idDocumentoLegal") Integer idDocumentoLegal, @Param("eliminado") String eliminado);

    @Query("SELECT h FROM HcSeguridadPorUsuario h WHERE h.hcSeguridadPorDocumentoLegal.id=:idHcSeguridadPorDocumentoLegal AND h.eliminado=:eliminado")
    List<HcSeguridadPorUsuario> obtenerUsuariosPorIdHcSeguridadPorDocumentoLegal(@Param("idHcSeguridadPorDocumentoLegal") Integer idHcSeguridadPorDocumentoLegal, @Param("eliminado") String eliminado);

    @Modifying
    @Query("UPDATE HcSeguridadPorDocumentoLegal h set h.eliminado=:eliminado WHERE h.id=:id")
    void eliminarHcSeguridadPorDocumentoLegalPorId(@Param("id") Integer id, @Param("eliminado") String eliminado);

    @Modifying
    @Query("UPDATE HcSeguridadPorUsuario h set h.eliminado=:eliminado WHERE h.hcSeguridadPorDocumentoLegal.id=:idHcSeguridadPorDocumentoLegal")
    void eliminarHcSeguridadPorUsuarioPorIdHcSeguridadPorDocumentoLegal(@Param("idHcSeguridadPorDocumentoLegal") Integer idHcSeguridadPorDocumentoLegal, @Param("eliminado") String eliminado);

}
package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcUbicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcUbicacionRepository extends JpaRepository<HcUbicacion, Integer> {
    @Query("SELECT e FROM HcUbicacion e WHERE e.id = :idHcUbicacion")
    HcUbicacion findById(@Param("idHcUbicacion") Integer idHcUbicacion);

    @Query("select e from HcUbicacion e WHERE e.estado = 'A' AND e.tipo_ubicacion.codigo = :tipoUbicacion AND e.compania.id =:idCompania AND e.nombre =:nombre")
    HcUbicacion getUbicacionActivaPorTipoCodigo(@Param("tipoUbicacion") String tipoUbicacion,
                                                      @Param("idCompania") Integer idCompania,
                                                      @Param("nombre") String nombre);
    @Query("select e from HcUbicacion e where e.tipo_ubicacion.codigo = :operacion AND e.compania.id = :idCompania AND e.estado = :estado")
    List<HcUbicacion> getUbicacionActivaPorTipo(@Param("operacion") String operacion,
                                                @Param("idCompania") Integer idCompania,
                                                @Param("estado") Character estado);
    @Query("SELECT upd.id.ubicacion FROM HcUbicacionPorDocumento upd WHERE upd.id.documentoLegal.id=:idDocumentoLegal")
    List<HcUbicacion> obtenerUbicacionesDocumentoLegal(@Param("idDocumentoLegal") Integer idDocumentoLegal);

    @Query("SELECT new HcUbicacion(c.id, c.nombre) FROM HcUbicacion c WHERE c.estado = 'A' AND c.tipo_ubicacion.codigo=:codigo AND c.compania.id =:idCompania ORDER BY c.nombre ASC")
    List<HcUbicacion> getUbicacionActivaPorTipoUbicacion(@Param("codigo") String codigo,
                                                         @Param("idCompania") Integer idCompania);
    @Query("SELECT c FROM HcUbicacion c WHERE c.estado = 'A' ORDER BY c.nombre ASC")
    List<HcUbicacion> listarUbicacionesActivas();
}
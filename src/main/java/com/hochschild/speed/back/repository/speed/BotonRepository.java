package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Boton;
import com.hochschild.speed.back.model.domain.speed.Perfil;
import com.hochschild.speed.back.model.domain.speed.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BotonRepository extends JpaRepository<Boton, Integer> {
    @Query("SELECT ad FROM Boton ad WHERE ad.id = :idBoton")
    Boton findById(@Param("idBoton") Integer idBoton);
    @Query("SELECT e FROM Boton e WHERE " +
            "(e.nombre LIKE %:nombre% OR :nombre IS NULL) AND " +
            "(e.codigo LIKE %:codigo% OR :codigo IS NULL) AND " +
            "(e.descripcion LIKE %:descripcion% OR :descripcion IS NULL) AND " +
            "e.estado = 'A' ORDER BY e.id ASC")
    List<Boton> list(@Param("nombre") String nombre,
                     @Param("codigo") String codigo,
                     @Param("descripcion") String descripcion);

    @Query("SELECT td  FROM Perfil td WHERE td.id NOT IN (SELECT id.perfil.id FROM BotonPorGridPorPerfil where id.boton.id = :idBoton)")
    List<Perfil> buscarPerfilesNoAsignados(@Param("idBoton") Integer idBoton);

    @Query("SELECT b FROM Boton b WHERE b.url = :url")
    Boton obtenerBotonPorUrl(@Param("url") String url);

    @Query("SELECT b FROM Boton b WHERE b.id IN (" +
            "SELECT r.id.recurso.id FROM RecursoPorPerfil r WHERE r.id.perfil = :idPerfil AND " +
            "(r.tipoProceso IS NULL OR r.tipoProceso.id = :tipoProceso OR :tipoProceso IS NULL) AND " +
            "(r.paraEstado IS NULL OR r.paraEstado = :paraEstado OR :paraEstado IS NULL) AND " +
            "(r.responsable IS NULL OR r.responsable = :responsable OR :responsable IS NULL) AND " +
            "(b.parametro NOT LIKE '%'+:parametro+'%')" +
            ") AND UPPER(b.codigo) = :codigoGrid AND b.estado = :estado AND (b.url != :url OR :url IS NULL) ORDER BY b.orden")
    List<Boton> buscarPorPerfilGridExceptoParametro(@Param("idPerfil") Integer idPerfil,
                                                                  @Param("tipoProceso") Integer tipoProceso,
                                                                  @Param("paraEstado") Character paraEstado,
                                                                  @Param("responsable") Boolean responsable,
                                                                  @Param("parametro") String parametro,
                                                                  @Param("codigoGrid") String codigoGrid,
                                                                  @Param("estado") Character estado,
                                                                  @Param("url") String url
                                                                  );



    @Query("SELECT b FROM Boton b WHERE b.id IN (" +
            "SELECT r.id.recurso.id FROM RecursoPorPerfil  r WHERE r.id.perfil.id = :idPerfil AND " +
            "(r.tipoProceso.id = :tipoProceso OR r.tipoProceso IS NULL OR :tipoProceso IS NULL) AND " +
            "(r.paraEstado IS NULL OR r.paraEstado = :paraEstado OR :paraEstado IS NULL) AND " +
            "(r.responsable IS NULL OR r.responsable = :responsable OR :responsable IS NULL) AND " +
            "(b.parametro NOT LIKE '%'+:parametro+'%') AND (r.id.recurso.id = :idRecurso)" +
            ") AND UPPER(b.codigo) = :codigoGrid AND b.estado = :estado AND (b.url != :url OR :url IS NULL) ORDER BY b.orden")
    List<Boton> getBotonAdendaAutomaticaPosicionContractual(@Param("idPerfil") Integer perfil,
                                                            @Param("codigoGrid")  String codigoGrid,
                                                            @Param("tipoProceso") Integer tipoProceso,
                                                            @Param("paraEstado") Character paraEstado,
                                                            @Param("responsable") Boolean responsable,
                                                            @Param("parametro") String parametro,
                                                            @Param("idRecurso") Integer idRecurso,
                                                            @Param("url") String url);

        @Query("SELECT b FROM Boton b WHERE b.id IN (" +
            "SELECT r.id.recurso.id FROM RecursoPorPerfil r WHERE r.id.perfil = :idPerfil AND " +
            "(r.tipoProceso IS NULL OR r.tipoProceso.id = :tipoProceso OR :tipoProceso IS NULL) AND " +
            "(r.paraEstado IS NULL OR r.paraEstado = :paraEstado OR :paraEstado IS NULL) AND " +
            "(r.responsable IS NULL OR r.responsable = :responsable OR :responsable IS NULL) AND " +
            "(b.parametro NOT LIKE '%'+:parametro+'%')" +
            ") AND UPPER(b.codigo) = :codigoGrid AND b.estado = :estado AND b.url != :url ORDER BY b.orden")
    List<Boton> buscarPorPerfilGridExceptoParametroConRestriccion();
}
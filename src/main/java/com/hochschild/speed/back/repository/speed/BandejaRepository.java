package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcArea;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface BandejaRepository extends PagingAndSortingRepository<HcArea, Integer> {
    @Query("SELECT ad FROM HcArea ad WHERE ad.id = :idHcArea")
    HcArea findById(@Param("idHcArea") Integer idHcArea);

//    @Query("SELECT new com.hochschild.speed.back.model.bean.BandejaBean (x.id.traza.id, " +
//            "x.id.traza.expediente.documentoLegal.numero, " +
//            "x.id.traza.expediente.documentoLegal.hc_area.compania.nombre, " +
//            "c.razonSocial," +
//            "x.id.traza.expediente.documentoLegal.sumilla, " +
//            "x.id.traza.expediente.documentoLegal.estado, " +
//            "x.id.traza.expediente.documentoLegal.fechaSolicitud, " +
//            "x.id.traza.expediente.documentoLegal.fechaBorrador, " +
//            "x.id.traza.expediente.proceso.nombre, " +
//            "x.id.traza.estado, " +
//            "x.leido) " +
//            "FROM UsuarioPorTraza x " +
//            "LEFT JOIN x.id.traza.expediente.documentoLegal.contraparte c " +
//            "WHERE ( (x.id.traza.expediente.documentoLegal.numero=:numero and :numero<>'') or (:numero = '') ) " +
//            "AND ( (x.id.traza.expediente.documentoLegal.hc_area.compania.id =:compania and :compania is not null) or (:compania is null) ) " +
//            "AND ( (c.id =:contraparte and :contraparte is not null) or (:contraparte is null) ) " +
//            "AND ( (UPPER(x.id.traza.expediente.documentoLegal.sumilla) like '%'+:sumilla+'%'  and :sumilla<>'') or (:sumilla = '') )" +
//            "AND ( (x.id.traza.expediente.proceso.id =:proceso and :proceso is not null) or (:proceso is null) ) " +
//            "AND ((x.id.traza.actual = true AND (x.id.traza.estado IS NULL OR x.id.traza.estado <> 'U')) " +
//            "   OR (x.id.traza.estado = 'P' AND x.estado = 'P')) " +
//            "AND x.id.traza.expediente.documentoLegal.estado IS NOT NULL " +
//            "AND x.id.traza.expediente.estado = :estado " +
//            "AND (x.rol IN elements(x.id.usuario.roles) OR x.rol IS NULL) " +
//            "AND (x.id.traza.expediente.copiado IS NULL OR x.id.traza.expediente.copiado = false) " +
//            "AND (x.id.usuario = :usuario " +
//            " OR EXISTS " +
//            "(SELECT r FROM Reemplazo r " +
//            "WHERE r.reemplazante = :usuario " +
//            "AND r.reemplazado = x.id.usuario " +
//            "AND x.id.traza.expediente.proceso IN elements(r.procesos) " +
//            "AND :fecha BETWEEN r.desde AND r.hasta " +
//            "AND r.reemplazado != :usuario)" +
//            ") " +
//            "ORDER BY x.id.traza.fechaCreacion DESC " +
//            " ")
//    Page<BandejaBean> obtenerBandejaEntrada(
//            @Param("estado") Character estado
//            , @Param("usuario") Usuario usuario
//            , @Param("fecha") Date fecha
//            , @Param("numero") String numero
//            , @Param("compania") Integer compania
//            , @Param("contraparte") Integer contraparte
//            , @Param("sumilla") String sumilla
//            , @Param("proceso") Integer proceso
//            , Pageable pageable
//            //,@Param("pais")        Integer      pais
//
//    );

    //---------------------------------------------------------------------------
//
//    @Query("SELECT new com.hochschild.speed.back.model.bean.MisPendientesBean (" +
//            "  x.id" +
//            ", x.documentoLegal.numero" +
//            ", x.documentoLegal.hc_area.compania.nombre" +
//            ", c.razonSocial" +
//            ", x.documentoLegal.sumilla" +
//            ", x.documentoLegal.estado" +
//            ", x.documentoLegal.fechaSolicitud" +
//            ", x.documentoLegal.fechaBorrador" +
//            ", x.proceso.nombre" +
//            ") " +
//            "FROM Expediente x LEFT JOIN x.documentoLegal.contraparte c " +
//            "WHERE " +
//            "       ( (x.documentoLegal.numero=:numero and :numero<>'') or (:numero = '') ) " +
//            "   AND ( (x.documentoLegal.hc_area.compania.id =:compania and :compania is not null) or (:compania is null) ) " +
//            "   AND ( (c.id =:contraparte and :contraparte is not null) or (:contraparte is null) ) " +
//            "   AND ( (UPPER(x.documentoLegal.sumilla) like '%'+:sumilla+'%'  and :sumilla<>'') or (:sumilla = '') )" +
//            "   AND x.estado = :estado " +
//            "   AND x.proceso.id = :proceso " +
//            "   AND x.documentoLegal.responsable = :usuario " +
//            "   AND x.documentoLegal.estado NOT IN ('N', 'T', 'O') " +
//            "ORDER BY x.fechaCreacion DESC")
//    Page<MisPendientesBean> obtenerMisPendientes(
//            @Param("estado") Character estado
//            , @Param("usuario") Usuario usuario
//            , @Param("numero") String numero
//            , @Param("compania") Integer compania
//            , @Param("contraparte") Integer contraparte
//            , @Param("sumilla") String sumilla
//            , @Param("proceso") Integer proceso
//            , Pageable pageable
//    );

//
//    @Query("SELECT new com.hochschild.speed.back.model.bean.MisPendientesBean (" +
//            "x.id " +
//            ", x.documentoLegal.numero " +
//            ", x.documentoLegal.hc_area.compania.nombre " +
//            ", c.razonSocial" +
//            ", x.documentoLegal.sumilla " +
//            ", x.documentoLegal.estado " +
//            ", x.documentoLegal.fechaSolicitud " +
//            ", x.documentoLegal.fechaBorrador " +
//            ", x.proceso.nombre) " +
//            "FROM Expediente x " +
//            "LEFT JOIN x.documentoLegal.contraparte c " +
//            "WHERE " +
//            "       ( (x.documentoLegal.numero=:numero and :numero<>'') or (:numero = '') ) " +
//            "   AND ( (x.documentoLegal.hc_area.compania.id =:compania and :compania is not null) or (:compania is null) ) " +
//            "   AND ( (c.id =:contraparte and :contraparte is not null) or (:contraparte is null) ) " +
//            "   AND ( (UPPER(x.documentoLegal.sumilla) like '%'+:sumilla+'%'  and :sumilla<>'') or (:sumilla = '') )" +
//            " AND    x.estado=:estado " +
//            " and   x.proceso.id = :proceso " +
//            "AND x.documentoLegal.solicitante=:usuario " +
//            "AND x.documentoLegal.estado!='T' " +
//            "AND x.documentoLegal.estado!='O' " +
//            "ORDER BY x.fechaCreacion DESC")
//    Page<MisPendientesBean> obtenerMisSolicitudes(
//            @Param("estado") Character estado
//            , @Param("usuario") Usuario usuario
//            , @Param("numero") String numero
//            , @Param("compania") Integer compania
//            , @Param("contraparte") Integer contraparte
//            , @Param("sumilla") String sumilla
//            , @Param("proceso") Integer proceso
//            , Pageable pageable
//    );

//    @Query("SELECT new com.hochschild.speed.back.model.bean.SolicitudesPorEnviarBean (" +
//            "x.id " +
//            ", x.documentoLegal.numero " +
//            ", x.documentoLegal.hc_area.compania.nombre " +
//            ", c.razonSocial" +
//            ", x.fechaCreacion " +
//            ", x.proceso.nombre) " +
//            "FROM Expediente x " +
//            "LEFT JOIN x.documentoLegal.contraparte c " +
//            "WHERE " +
//            "       ( (x.documentoLegal.numero=:numero and :numero<>'') or (:numero = '') ) " +
//            "   AND ( (x.documentoLegal.hc_area.compania.id =:compania and :compania is not null) or (:compania is null) ) " +
//            "   AND ( (c.id =:contraparte and :contraparte is not null) or (:contraparte is null) ) " +
//            " AND   x.creador=:usuario  " +
//            " and   x.proceso.id = :proceso " +
//            " AND   x.estado=:estado")
//    Page<SolicitudesPorEnviarBean> obtenerSolicitudesPorEnviar(
//            @Param("estado") Character estado
//            , @Param("usuario") Usuario usuario
//            , @Param("numero") String numero
//            , @Param("compania") Integer compania
//            , @Param("contraparte") Integer contraparte
//            , @Param("proceso") Integer proceso
//            , Pageable pageable
//    );
}
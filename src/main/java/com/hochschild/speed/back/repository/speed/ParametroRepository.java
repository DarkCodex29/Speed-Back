package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Integer> {
    @Query("SELECT DISTINCT p.tipo FROM Parametro p ORDER BY p.tipo")
    List<String> parameterType();
    @Query("SELECT e FROM Parametro e WHERE e.id = :idParametro")
    Parametro findById(@Param("idParametro") Integer idParametro);
    @Query("select e from Parametro e where e.tipo = :tipo AND e.valor = :valor")
    Parametro findObtenerPorTipoValor(@Param("tipo") String tipo, @Param("valor") String valor);
    @Query("SELECT p FROM Parametro p WHERE p.tipo=:tipo")
    List<Parametro> obtenerPorTipo(@Param("tipo") String tipo);
    @Query("SELECT p FROM Parametro p WHERE p.tipo=:tipo AND p.valor=:valor")
    Parametro obtenerPorTipoValor(@Param("tipo") String tipo, @Param("valor") String valor);
    @Query("SELECT p FROM Parametro p WHERE p.descripcion=:descripcion")
    Parametro obtenerParametroPorDescripcion(@Param("descripcion") String descripcion);
}
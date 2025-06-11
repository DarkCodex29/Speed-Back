package com.hochschild.speed.back.dao;

import java.util.List;
import com.hochschild.speed.back.model.domain.speed.Boton;
import com.hochschild.speed.back.model.domain.speed.Perfil;
import com.hochschild.speed.back.model.domain.speed.TipoProceso;

public interface BotonesDao {

    List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid);
    List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid, Boolean responsable);
    List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid, Character paraEstado);
    List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid, Character paraEstado, Boolean responsable);
    List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid, TipoProceso tipoProceso, Character paraEstado);
    List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid, TipoProceso tipoProceso, Character paraEstado, Boolean responsable);
    List<Boton> buscarPorPerfilGridExceptoParametro(Perfil perfil, String codigoGrid, TipoProceso tipoProceso, Character paraEstado, Boolean responsable, String parametro);
    List<Boton> getActivos();
    List<Boton> obtenerPorPerfilGrid(Integer idPerfil, Integer idGrid);
    List<Boton> getBotonesNoAsociadosGridPerfil(Integer idPerfil, Integer idGrid);
    List<Boton> getBotonAdendaAutomaticaPosicionContractual(Perfil perfil, String codigoGrid, TipoProceso tipoProceso, Character paraEstado, Boolean responsable, String parametro, Integer idRecurso);
    Boton obtenerBotonPorUrl(String url);
}
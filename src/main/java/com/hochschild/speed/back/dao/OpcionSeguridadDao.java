package com.hochschild.speed.back.dao;

import java.util.List;

public interface OpcionSeguridadDao {

    List<Long> getOpcionesSeguridad(Integer idAplicacion, String idUsuario);

    List<Long> getOpcionesSinUsuario(Integer idAplicacion, String idRol);

    List<String> getRolesPorUsuario(Integer idAplicacion, String idPuesto);

    List<String> getValoresOrganizacionales(Integer idAplicacion, String idUsuario);
}
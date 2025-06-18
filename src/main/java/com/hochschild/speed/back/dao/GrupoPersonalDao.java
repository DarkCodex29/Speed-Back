package com.hochschild.speed.back.dao;

import java.util.List;

public interface GrupoPersonalDao {
    
    /**
     * Obtiene la lista de correos electrónicos del grupo de personal
     * usando el procedimiento almacenado SPEED_ADM_ListarGrupoPersonal
     * 
     * @return Lista de correos electrónicos
     */
    List<String> obtenerCorreosGrupoPersonal();
} 
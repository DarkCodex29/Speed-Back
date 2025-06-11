package com.hochschild.speed.back.model.bean.mantenimiento;

import com.hochschild.speed.back.model.domain.speed.Rol;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import lombok.Data;
import java.util.List;

public @Data class ProcesoBean {

    private Integer id;
    private String nombre;
    private String descripcion;
    private String plazo;
    private Integer idTipoProceso;
    private Integer idConfidencialidad;
    private Integer idRolResponsable;
    private Integer idUsuarioResponsable;
    private Boolean estado;
    private Boolean creadorResponsable;
    private Boolean conCliente;

    List<Usuario> usuariosParticipantes;
    List<Rol> rolesParticipantes;
    List<Rol> rolesProcesos;
    List<TipoDocumentoProcesoBean> tipoDocumentos;
}
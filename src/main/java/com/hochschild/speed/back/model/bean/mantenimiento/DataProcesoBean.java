package com.hochschild.speed.back.model.bean.mantenimiento;

import com.hochschild.speed.back.model.domain.speed.Parametro;
import com.hochschild.speed.back.model.domain.speed.Rol;
import com.hochschild.speed.back.model.domain.speed.TipoProceso;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import lombok.Data;
import java.util.List;

public @Data class DataProcesoBean {

    private Integer id;
    private String nombre;
    private String descripcion;
    private TipoProceso tipoProceso;
    private Integer idProcesoPadre;
    private Parametro confidencialidad;
    private String plazoDias;
    private Boolean estado;
    private Boolean conCliente;

    private Boolean creadorResponsable;
    private Usuario usuarioResponsable;
    private Rol rolResponsable;

    private List<Usuario> usuariosParticipantes;
    private List<Rol> rolesParticipantes;
    private List<Rol> rolesProcesos;
    private List<TipoDocumentoProcesoBean> tipoDocumentos;
}
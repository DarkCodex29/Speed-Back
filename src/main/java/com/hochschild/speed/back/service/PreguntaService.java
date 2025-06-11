package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.GrupoAsistenteVirtual;
import com.hochschild.speed.back.model.domain.speed.PreguntasAsistenteVirtual;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.model.domain.speed.utils.AreaDTO;
import com.hochschild.speed.back.model.response.ResponseModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PreguntaService {
    public List<PreguntasAsistenteVirtual> consultarPreguntasPorArea(Integer codArea);

    public List<AreaDTO> listarAreasPreguntas();

    public List<AreaDTO> listarTemasByArea(Integer idArea);

    public GrupoAsistenteVirtual obtenerGrupo(Integer idArea, Integer idTema);

    public List<PreguntasAsistenteVirtual> listarPreguntas();

    public void registrarPreguntaRespuesta(PreguntasAsistenteVirtual preguntasAsistenteVirtual);

    public void modificarPreguntaRespuesta(PreguntasAsistenteVirtual preguntasAsistenteVirtual);

    public PreguntasAsistenteVirtual obtenerPreguntaRespuesta(Integer idAplicacion, Integer codigo);

    public String maxCodPregunta(String codigoArea);

    public String maxCodRespuesta(String codigoArea);

    public Integer maxCodigo();

    ResponseEntity<ResponseModel> registrarPregunta(Usuario usuario, String pregunta, Integer idUsuario);

    ResponseEntity<ResponseModel> registrarUsabilidad(Usuario usuario, Integer idOpcionAsistente, String esOpcionAsistente, Integer idUsuario);

}

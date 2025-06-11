package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.model.domain.speed.utils.AreaDTO;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.EnviarNotificacionService;
import com.hochschild.speed.back.service.PreguntaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PreguntaServiceImpl implements PreguntaService {

    private final PreguntaAsistenteVirtualRepository preguntaRespository;
    private final AsistenteVCorreoRepository asistenteVCorreoRepository;
    private final EnviarNotificacionService enviarNotificacionService;

    private final UsuarioRepository usuarioRepository;

    private final AsistenteVirtualUsabilidadRepository asistenteVirtualUsabilidadRepository;

    private final AsistenteVOtrasPreguntasRepository asistenteVOtrasPreguntasRepository;


    public PreguntaServiceImpl(PreguntaAsistenteVirtualRepository preguntaRespository,
                               AsistenteVCorreoRepository asistenteVCorreoRepository,
                               EnviarNotificacionService enviarNotificacionService,
                               UsuarioRepository usuarioRepository, AsistenteVOtrasPreguntasRepository asistenteVOtrasPreguntasRepository,
                               AsistenteVirtualUsabilidadRepository asistenteVirtualUsabilidadRepository) {
        this.preguntaRespository = preguntaRespository;
        this.asistenteVCorreoRepository = asistenteVCorreoRepository;
        this.enviarNotificacionService = enviarNotificacionService;
        this.usuarioRepository = usuarioRepository;
        this.asistenteVOtrasPreguntasRepository = asistenteVOtrasPreguntasRepository;
        this.asistenteVirtualUsabilidadRepository = asistenteVirtualUsabilidadRepository;
    }

    @Override
    public List<PreguntasAsistenteVirtual> consultarPreguntasPorArea(Integer codArea) {
        if(codArea == 0) {
            codArea = null;
        }
        return preguntaRespository.consultarPreguntasPorArea(codArea);
    }

    @Override
    public List<AreaDTO> listarAreasPreguntas() {
        return preguntaRespository.listarAreasPreguntas();
    }

    @Override
    public List<AreaDTO> listarTemasByArea(Integer idArea) {
        return preguntaRespository.listarTemasByArea(idArea);
    }

    @Override
    public GrupoAsistenteVirtual obtenerGrupo(Integer idArea, Integer idTema) {
        return preguntaRespository.obtenerGrupo(idArea, idTema);
    }

    @Override
    public List<PreguntasAsistenteVirtual> listarPreguntas() {
        return preguntaRespository.listarPreguntas();
    }

    @Override
    public void registrarPreguntaRespuesta(PreguntasAsistenteVirtual preguntasAsistenteVirtual) {
        preguntaRespository.save(preguntasAsistenteVirtual);
    }

    @Override
    public void modificarPreguntaRespuesta(PreguntasAsistenteVirtual preguntasAsistenteVirtual) {
        preguntaRespository.save(preguntasAsistenteVirtual);
    }

    @Override
    public PreguntasAsistenteVirtual obtenerPreguntaRespuesta(Integer idAplicacion, Integer codigo) {
        return preguntaRespository.obtenerPreguntaRespuesta(idAplicacion, codigo);
    }

    @Override
    public String maxCodPregunta(String codigoArea) {
        String result = preguntaRespository.maxCodPregunta();
        result = result == null ? codigoArea + "P01" : result;
        return result;
    }

    @Override
    public String maxCodRespuesta(String codigoArea) {
        String result = preguntaRespository.maxCodRespuesta();
        result = result == null ? codigoArea + "P01" : result;
        return result;
    }

    @Override
    public Integer maxCodigo() {
        return preguntaRespository.maxCodigo();
    }

    @Override
    public ResponseEntity<ResponseModel> registrarPregunta(Usuario usuario, String pregunta, Integer idUsuario) {
        ResponseModel responseModel = new ResponseModel();
        try {
            usuario = usuarioRepository.findById(idUsuario);
            AsistenteVOtrasPreguntas nuevaPregunta = new AsistenteVOtrasPreguntas();
            nuevaPregunta.setPregunta(pregunta);
            nuevaPregunta.setIdUsuario(nuevaPregunta.getIdUsuario());
            nuevaPregunta.setUsuarioCreacion(usuario.getUsuario());
            nuevaPregunta.setFechaCreacion(new Date());
            asistenteVOtrasPreguntasRepository.save(nuevaPregunta);
            responseModel.setMessage("Se registró con éxito");
            responseModel.setHttpSatus(HttpStatus.OK);
            sendNotificationQuestion(usuario, pregunta);
        } catch (Exception ex) {
            responseModel.setMessage("No se pudo registrar la pregunta");
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }

    @Override
    public ResponseEntity<ResponseModel> registrarUsabilidad(Usuario usuario, Integer idOpcionAsistente, String esOpcionAsistente, Integer idUsuario) {
        ResponseModel responseModel = new ResponseModel();
        try {
            usuario = usuarioRepository.findById(idUsuario);
            AsistenteVirtualUsabilidad usabilidad = new AsistenteVirtualUsabilidad();
            usabilidad.setIdOpcionAsistente(idOpcionAsistente);
            usabilidad.setEsOpcionAsistente(esOpcionAsistente);
            usabilidad.setUsuarioCreacion(usuario.getUsuario());
            usabilidad.setFechaCreacion(new Date());
            asistenteVirtualUsabilidadRepository.save(usabilidad);
            responseModel.setMessage("Se registró la traza de usabilidad");
            responseModel.setHttpSatus(HttpStatus.OK);
        } catch (Exception ex) {
            responseModel.setMessage("No se pudo registrar la traza de usabilidad");
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());
    }

    @Async
    public void sendNotificationQuestion(Usuario usuario, String pregunta) {
        List<AsistenteVCorreo> listaCorreos = asistenteVCorreoRepository.consultarCorreosVigentes();
        List<Usuario> usuariosDestino = new ArrayList<>();
        for (AsistenteVCorreo asistenteVCorreo : listaCorreos) {
            usuariosDestino.add(asistenteVCorreo.getUsuario());
        }
        enviarNotificacionService.registrarNotificacionPregunta(usuariosDestino, usuario, pregunta);
    }
}

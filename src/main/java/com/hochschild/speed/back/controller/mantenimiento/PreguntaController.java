package com.hochschild.speed.back.controller.mantenimiento;

import com.hochschild.speed.back.model.domain.speed.GrupoAsistenteVirtual;
import com.hochschild.speed.back.model.domain.speed.PreguntaAsistenteVirtualPK;
import com.hochschild.speed.back.model.domain.speed.PreguntasAsistenteVirtual;
import com.hochschild.speed.back.model.domain.speed.utils.AreaDTO;
import com.hochschild.speed.back.model.domain.speed.utils.PreguntaDTO;
import com.hochschild.speed.back.model.response.ResponseModel;
import com.hochschild.speed.back.service.PreguntaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("mantenimientoPregunta")
public class PreguntaController {
    private final PreguntaService preguntaService;

    public PreguntaController(PreguntaService preguntaService) {
        this.preguntaService = preguntaService;
    }

    @GetMapping(value = "/areas", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<AreaDTO> listarAreas() {
        List<AreaDTO> listAreas = preguntaService.listarAreasPreguntas();
        return listAreas;
    }

    @GetMapping(value = "/temasByArea", consumes = MediaType.APPLICATION_JSON_VALUE, params = {"codArea"})
    public List<AreaDTO> getTemas(@RequestParam Integer codArea) {
        List<AreaDTO> listTemas = preguntaService.listarTemasByArea(codArea);
        return listTemas;
    }

    @RequestMapping(value = "/listarPreguntas", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, params = {"codArea"})
    public ResponseEntity<List<PreguntasAsistenteVirtual>> listarPreguntas(@RequestParam("codArea")  Integer codArea) {
        try {
            List<PreguntasAsistenteVirtual> preguntas = preguntaService.consultarPreguntasPorArea(codArea);
            for (PreguntasAsistenteVirtual p : preguntas) {

                if (p.getDescripcionPregunta().length() > 40) {
                    p.setDescripcionPregunta(p.getDescripcionPregunta().substring(0, 40));
                }
                if (p.getDescripcionRespuesta().length() > 40) {
                    p.setDescripcionRespuesta(p.getDescripcionRespuesta().substring(0, 40));
                }

            }
            return new ResponseEntity<>(preguntas, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/guardarPreguntaModal", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> guardarPregunta(@RequestBody PreguntaDTO pregunta) {
        ResponseModel responseModel = new ResponseModel();
        try {
            String maxCodigoPregunta = preguntaService.maxCodPregunta(String.valueOf(pregunta.getCodigoArea())).trim();
            String maxCodigoRespuesta = preguntaService.maxCodRespuesta(String.valueOf(pregunta.getCodigoArea())).trim();
            Integer maxCodigo = preguntaService.maxCodigo();

            int nuevoCodigoPregunta = Integer.parseInt(maxCodigoPregunta.substring(maxCodigoPregunta.length() - 1));
            int nuevoCodigoRespuesta = Integer.parseInt(maxCodigoRespuesta.substring(maxCodigoRespuesta.length() - 1));
            int incrementandoCodigoPregunta = nuevoCodigoPregunta + 1;
            int incrementandoCodigoRespuesta = nuevoCodigoRespuesta + 1;
            int cantidadDigitosCodigoPregunta = String.valueOf(incrementandoCodigoPregunta).length();
            int cantidadDigitosCodigoRespuesta = String.valueOf(incrementandoCodigoRespuesta).length();

            String maxCodigoPreguntaAux = "";
            String maxCodigoRespuestaAux = "";
            if (cantidadDigitosCodigoPregunta == 1) {
                maxCodigoPreguntaAux = maxCodigoPregunta.substring(0, maxCodigoPregunta.length() - 1) + incrementandoCodigoPregunta;
            } else if (cantidadDigitosCodigoPregunta == 2) {
                maxCodigoPreguntaAux = maxCodigoPregunta.substring(0, maxCodigoPregunta.length() - 2) + incrementandoCodigoPregunta;
            }

            if (cantidadDigitosCodigoRespuesta == 1) {
                maxCodigoRespuestaAux = maxCodigoRespuesta.substring(0, maxCodigoRespuesta.length() - 1) + incrementandoCodigoRespuesta;
            } else if (cantidadDigitosCodigoRespuesta == 2) {
                maxCodigoRespuestaAux = maxCodigoRespuesta.substring(0, maxCodigoRespuesta.length() - 2) + incrementandoCodigoRespuesta;
            }

            PreguntaAsistenteVirtualPK preguntaPK = new PreguntaAsistenteVirtualPK(1, maxCodigo + 1);
            PreguntasAsistenteVirtual preguntasAsistenteVirtual = new PreguntasAsistenteVirtual();
            preguntasAsistenteVirtual.setId(preguntaPK);
            GrupoAsistenteVirtual grupo = preguntaService.obtenerGrupo(pregunta.getCodigoArea(), pregunta.getCodigoTema());
            if (grupo != null) {
                preguntasAsistenteVirtual.setGrupo(grupo);
            }
            preguntasAsistenteVirtual.setCodigoPregunta(maxCodigoPreguntaAux);
            preguntasAsistenteVirtual.setCodigoRespuesta(maxCodigoRespuestaAux);
            preguntasAsistenteVirtual.setDescripcionPregunta(pregunta.getDescripcionPregunta());
            preguntasAsistenteVirtual.setDescripcionRespuesta(pregunta.getDescripcionRespuesta());
            preguntasAsistenteVirtual.setVigente("S");
            preguntaService.registrarPreguntaRespuesta(preguntasAsistenteVirtual);
            responseModel.setMessage("OK");
            responseModel.setHttpSatus(HttpStatus.OK);
        } catch (Exception ex) {
            responseModel.setMessage("Error al registrar");
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());

    }

    @RequestMapping(value = "/pregunta", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, params = {"codigo"})
    public PreguntasAsistenteVirtual obtenerPreguntaPorCodigo(@RequestParam("codigo") Integer codigo) {
        PreguntasAsistenteVirtual preguntasAsistenteVirtual = preguntaService.obtenerPreguntaRespuesta(1, codigo);
        return preguntasAsistenteVirtual;
    }

    @ResponseBody
    @RequestMapping(value = "/modificarPreguntaModal", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<ResponseModel> modificarPregunta(@RequestBody PreguntaDTO pregunta) {
        ResponseModel responseModel = new ResponseModel();
        try {
            PreguntasAsistenteVirtual preguntasAsistenteVirtual = preguntaService.obtenerPreguntaRespuesta(pregunta.getIdAplicacion(), pregunta.getCodigo());
            preguntasAsistenteVirtual.setDescripcionPregunta(pregunta.getDescripcionPregunta());
            preguntasAsistenteVirtual.setDescripcionRespuesta(pregunta.getDescripcionRespuesta());
            preguntasAsistenteVirtual.setVigente(pregunta.getVigente());
            preguntaService.modificarPreguntaRespuesta(preguntasAsistenteVirtual);
            responseModel.setMessage("OK");
            responseModel.setHttpSatus(HttpStatus.OK);
        } catch (Exception ex) {
            responseModel.setMessage("Error al registrar");
            responseModel.setHttpSatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(responseModel, responseModel.getHttpSatus());

    }


}

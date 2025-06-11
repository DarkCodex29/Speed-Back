package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.GrupoAsistenteVirtual;
import com.hochschild.speed.back.model.domain.speed.PreguntaAsistenteVirtualPK;
import com.hochschild.speed.back.model.domain.speed.PreguntasAsistenteVirtual;
import com.hochschild.speed.back.model.domain.speed.utils.AreaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PreguntaAsistenteVirtualRepository extends JpaRepository<PreguntasAsistenteVirtual, PreguntaAsistenteVirtualPK> {
    @Query("SELECT p FROM PreguntasAsistenteVirtual p WHERE (p.vigente = 'S' AND p.grupo.area.id = :codArea) OR :codArea is null")
    List<PreguntasAsistenteVirtual> consultarPreguntasPorArea(@Param("codArea") Integer codArea);
    @Query("SELECT new com.hochschild.speed.back.model.domain.speed.utils.AreaDTO(p.id, p.descripcion) FROM Parametro p WHERE p.tipo = 'area_asistente'")
    List<AreaDTO> listarAreasPreguntas();
    @Query("SELECT new com.hochschild.speed.back.model.domain.speed.utils.AreaDTO(g.tema.id, g.tema.descripcion) FROM GrupoAsistenteVirtual g WHERE g.area.id = :idArea")
    List<AreaDTO> listarTemasByArea(@Param("idArea") Integer idArea);


    @Query("SELECT g FROM GrupoAsistenteVirtual g WHERE g.area.id = :idArea AND g.tema.id = :idTema")
    GrupoAsistenteVirtual obtenerGrupo(@Param("idArea") Integer idArea, @Param("idTema") Integer idTema);
    @Query("SELECT p FROM PreguntasAsistenteVirtual p WHERE p.vigente = 'S'")
    List<PreguntasAsistenteVirtual> listarPreguntas();
    @Query("SELECT tdp FROM PreguntasAsistenteVirtual tdp WHERE tdp.id.aplicacion = :idAplicacion AND tdp.id.codigo = :codigo")
    PreguntasAsistenteVirtual obtenerPreguntaRespuesta(@Param("idAplicacion") Integer idAplicacion, @Param("codigo") Integer codigo);
    @Query("SELECT MAX(p.codigoPregunta) FROM PreguntasAsistenteVirtual p")
    String maxCodPregunta();
    @Query("SELECT MAX(p.codigoRespuesta) FROM PreguntasAsistenteVirtual p")
    String maxCodRespuesta();

    @Query("SELECT MAX(p.id.codigo) FROM PreguntasAsistenteVirtual p")
    Integer maxCodigo();
}

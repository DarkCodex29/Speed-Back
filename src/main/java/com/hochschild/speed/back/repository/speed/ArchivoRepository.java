package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Integer> {
    @Query("SELECT ad FROM Archivo ad WHERE ad.id = :idArchivo")
    Archivo findById(@Param("idArchivo") Integer idArchivo);
    
    @Query("select e from Archivo e where e.estado = 'A' AND e.documento.id = :idDocumento  ORDER BY e.fechaCreacion desc")
    List<Archivo> obtenerPorDocumento(@Param("idDocumento") Integer idDocumento);

    @Query("select e from Archivo e where e.nombre = :nombre AND e.documento.id = :idDocumento")
    Archivo buscarArchivoPorNombreYDocumento(@Param("nombre") String nombre,
                                             @Param("idDocumento") Integer idDocumento);

    @Query("SELECT a FROM Archivo a WHERE a.nombre = :nombre AND a.documento.expediente.id = :idExpediente")
    Archivo obtenerPorNombreEnExpediente(@Param("nombre") String nombre,
                                         @Param("idExpediente") Integer idExpediente);
    @Query("SELECT a FROM Archivo a WHERE a.nombre = :nombre AND a.documento.id = :id")
    Archivo buscarArchivoPorNombre(@Param("nombre") String nombre, @Param("id") Integer id);
    @Query("SELECT a FROM Archivo a WHERE a.nombre = :nombre AND a.documento.expediente.id = :idExp AND a.documento.id != :idDoc")
    Archivo buscarArchivoPorNombreEnExpedienteDocumento(@Param("nombre") String nombre, @Param("idExp") Integer idExp, @Param("idDoc") Integer idDoc);
}
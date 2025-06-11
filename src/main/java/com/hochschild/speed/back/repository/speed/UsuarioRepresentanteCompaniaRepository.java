package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.RepresentanteComp;
import com.hochschild.speed.back.model.domain.speed.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioRepresentanteCompaniaRepository extends JpaRepository<RepresentanteComp, Integer> {
    @Query("select e from RepresentanteComp e where e.id = :idRepresentanteComp")
    RepresentanteComp findById(@Param("idRepresentanteComp") Integer idRepresentanteComp);
    @Query("SELECT t from RepresentanteComp t where t.estado = :estado")
    List<RepresentanteComp> getList(@Param("estado") String estado);

    @Query("SELECT t.representante from RepresentanteComp t where t.estado = :estado")
    List<Usuario> getListUsuariosRepresentantes(@Param("estado") String estado);
    @Query("SELECT e FROM RepresentanteComp e WHERE " +
            "(e.representante.nombres LIKE %:nombres% OR :nombres IS NULL) AND " +
            "(e.representante.apellidos LIKE %:apellidos% OR :apellidos IS NULL) AND " +
            "(e.nroDocumento LIKE %:numeroDocumento% OR :numeroDocumento IS NULL) AND " +
            "(e.correo LIKE %:correo% OR :correo IS NULL) " +
            "ORDER BY e.id DESC")
    List<RepresentanteComp> list(@Param("nombres") String nombres,
                                 @Param("apellidos") String apellidos,
                                 @Param("numeroDocumento") String numeroDocumento,
                                 @Param("correo") String correo);
}

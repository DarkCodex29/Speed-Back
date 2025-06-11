package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Query("select e from Cliente e where e.id = :idCliente")
    Cliente findById(@Param("idCliente") Integer idCliente);

    @Query("select e from Cliente e where e.estado <> :inactivo AND e.esRepresentante = true")
    List<Cliente> buscarClientesRepresentanteLegal(@Param("inactivo") Character inactivo);

    @Query("select e from Cliente e where e.numeroIdentificacion = :dni AND e.estado <> :inactivo ORDER BY e.id DESC")
    Cliente buscarClienteDocumentoIdentificacion(@Param("dni") String dni, @Param("inactivo") Character inactivo);

    @Query("select e from Cliente e where e.numeroIdentificacion = :dni AND e.estado <> :inactivo AND e.esContraparte = true ORDER BY e.id DESC")
    Cliente buscarClienteContraparteDocumentoIdentificacion(@Param("dni") String dni, @Param("inactivo") Character inactivo);

    @Query("select e from Cliente e where e.estado <> :inactivo AND e.esContraparte = true ORDER BY e.tipo.documento,e.numeroIdentificacion")
    List<Cliente> buscarClientesContraparte(@Param("inactivo") Character inactivo);

    @Query("SELECT e FROM Cliente e  ORDER BY e.id DESC ")
    List<Cliente> listarClientes();

    @Query("SELECT e FROM Cliente e WHERE " +
            "(e.tipo.id = :tipoCliente OR :tipoCliente IS NULL) AND " +
            "((e.razonSocial LIKE '%'+:filtroRazonSocial+'%') OR (e.nombre LIKE '%'+:filtroRazonSocial+'%') OR (e.apellidoPaterno LIKE '%'+:filtroRazonSocial+'%') OR (e.apellidoMaterno LIKE '%'+:filtroRazonSocial+'%') OR (LENGTH(TRIM(:filtroRazonSocial)) = 0)) AND" +
            "((e.correo LIKE '%'+:filtroCorreo+'%') OR (LENGTH(TRIM(:filtroCorreo)) = 0 )) AND" +
            "((e.numeroIdentificacion LIKE '%'+:filtroNumDocumento+'%') OR (LENGTH(TRIM(:filtroNumDocumento)) = 0)) " +
            "ORDER BY e.id DESC")
    List<Cliente> listarClientes(@Param("tipoCliente") Integer tipoCliente,
                                 @Param("filtroRazonSocial") String filtroRazonSocial,
                                 @Param("filtroCorreo") String filtroCorreo,
                                 @Param("filtroNumDocumento") String filtroNumDocumento);
}
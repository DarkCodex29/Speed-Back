package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Cliente;
import com.hochschild.speed.back.model.domain.speed.HcRepresentantePorDocumento;
import com.hochschild.speed.back.model.domain.speed.HcTipoContratoConfiguracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcRepresentantePorDocumentoRepository extends JpaRepository<HcRepresentantePorDocumento, Integer> {
    @Query("SELECT e FROM HcRepresentantePorDocumento e WHERE e.id = :idHcRepresentantePorDocumento")
    HcRepresentantePorDocumento findById(@Param("idHcRepresentantePorDocumento") Integer idHcRepresentantePorDocumento);
    @Query("select e from HcRepresentantePorDocumento e where e.id.documentoLegal.id = :idDocumentoLegal")
    List<HcRepresentantePorDocumento> obtenerHcRepresentantesDocumentoLegal(@Param("idDocumentoLegal") Integer idDocumentoLegal);

    @Query("SELECT rpd.id.cliente FROM HcRepresentantePorDocumento rpd WHERE rpd.id.documentoLegal.id=:idDocumentoLegal")
    List<Cliente> obtenerRepresentantesDocumentoLegal(@Param("idDocumentoLegal") Integer idDocumentoLegal);
}
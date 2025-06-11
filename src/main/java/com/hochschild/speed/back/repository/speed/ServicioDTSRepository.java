package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.ServicioDTS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import java.util.List;
@Repository
public class ServicioDTSRepository {
    private final EntityManager entityManager;

    @Autowired
    public ServicioDTSRepository(@Qualifier("speedEntityManagerFactory") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<ServicioDTS> consultarServiciosBusqueda(Integer contraparte,
                                  String numDocumento,
                                  String fechaVencDesde,
                                  String fechaVencHasta,
                                  Double montoInicial,
                                  Double montoFinal,
                                  Integer tipoContrato,
                                  String estado,
                                  Integer pais,
                                  Integer compania,
                                  Integer area,
                                  Integer tipoUbicacion,
                                  Integer ubicacion){
        StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery("consultarServiciosBusqueda");
        query.setParameter("id_cliente",contraparte);
        query.setParameter("numero",numDocumento);
        query.setParameter("vencimientoIni",fechaVencDesde);
        query.setParameter("vencimientoFin",fechaVencHasta);
        query.setParameter("montoIni",montoInicial);
        query.setParameter("montoFin",montoFinal);
        query.setParameter("tipo_contrato",tipoContrato);
        query.setParameter("estado",estado);
        query.setParameter("pais",pais);
        query.setParameter("compania",compania);
        query.setParameter("area",area);
        query.setParameter("tipoUbicacion",tipoUbicacion);
        query.setParameter("ubicacion",ubicacion);
        query.execute();
        //List<ServicioDTS> resultado = (List<ServicioDTS>) query.getResultList();
        List<ServicioDTS> resultado = (List<ServicioDTS>) query.getResultList();

        return resultado;
    }
}

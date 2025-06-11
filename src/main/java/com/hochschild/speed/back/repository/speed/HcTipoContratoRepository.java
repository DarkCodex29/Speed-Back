package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.HcTipoContrato;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcTipoContratoRepository extends PagingAndSortingRepository<HcTipoContrato, Integer> {
    @Query("SELECT e FROM HcTipoContrato e WHERE e.id = :idHcTipoContrato")
    HcTipoContrato findById(@Param("idHcTipoContrato") Integer idHcTipoContrato);
    HcTipoContrato findByNombre(String nombre);
    HcTipoContrato findByCodigo(char codigo);
    void deleteByNombre(String nombre);
    void deleteByCodigo(char codigo);
    @Query("SELECT e FROM HcTipoContrato e WHERE e.id = :idTipoContrato")
    HcTipoContrato findTipoContratoById(@Param("idTipoContrato") Integer idTipoContrato);
    void deleteById(int id);
    HcTipoContrato save(HcTipoContrato hcTipoContrato);
    @Query(" SELECT p FROM HcTipoContrato p ORDER BY p.nombre ASC ")
    List<HcTipoContrato> getListaTipoContrato();

    @Query("SELECT new HcTipoContrato(tc.id, tc.nombre, tc.codigo) FROM HcTipoContrato tc WHERE tc.codigo = :codigo")
    public List<HcTipoContrato> findLstBy(@Param("codigo") Character codigo);

    @Query("SELECT new HcTipoContrato(tc.id, tc.nombre, tc.codigo) FROM HcTipoContrato tc WHERE tc.codigo = :codigo AND tc.nombre =:nombre")
    public List<HcTipoContrato> findLstByAdendaContractual(@Param("codigo") Character codigo, @Param("nombre") String nombre);
}

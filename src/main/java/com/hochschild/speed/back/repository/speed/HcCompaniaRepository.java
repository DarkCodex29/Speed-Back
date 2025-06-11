package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.bean.ItemComboBean;
import com.hochschild.speed.back.model.domain.speed.HcCompania;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HcCompaniaRepository extends JpaRepository<HcCompania, Integer> {
    @Query("select e from HcCompania e where e.id = :idHcCompania")
    HcCompania findById(@Param("idHcCompania") Integer idHcCompania);
    @Query("SELECT c FROM HcCompania c WHERE c.estado= 'A' ORDER BY c.nombre ASC")
    List<HcCompania> getCompaniasActivas();
    @Query("SELECT c FROM HcCompania c WHERE c.estado= 'A' AND c.pais.id=:idPais ORDER BY c.nombre ASC")
    List<HcCompania> getCompaniasActivasPorPais(@Param("idPais") Integer idPais);
    @Query ("select new com.hochschild.speed.back.model.bean.ItemComboBean( " +
            " c.id" +
            ",c.nombre) " +
            "from HcCompania c " +
            "where c.pais.id = :pais " +
            "order by c.nombre")
    List<ItemComboBean> listarPorPais(@Param("pais") Integer pais);
}
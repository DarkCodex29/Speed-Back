package com.hochschild.speed.back.repository.speed;

import com.hochschild.speed.back.model.domain.speed.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VersionRepository extends JpaRepository<Version, Integer> {
    @Query("select e from Version e where e.id = :idVersion")
    Version findById(@Param("idVersion") Integer idVersion);

    @Query("select v from Version v where v.archivo.id=:idArchivo order by v.fechaCreacion desc")
    List<Version> getVersions(@Param("idArchivo") Integer idArchivo);
}
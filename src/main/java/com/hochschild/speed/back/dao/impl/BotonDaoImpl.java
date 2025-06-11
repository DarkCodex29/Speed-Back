package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.config.CydocConfig;
import com.hochschild.speed.back.dao.BotonDAO;
import com.hochschild.speed.back.model.domain.speed.Boton;
import com.hochschild.speed.back.model.domain.speed.Perfil;
import com.hochschild.speed.back.model.domain.speed.TipoProceso;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class BotonDaoImpl implements BotonDAO {
    private static final Logger LOGGER = Logger.getLogger(BotonDaoImpl.class.getName());
    private final EntityManager entityManager;
    private final CydocConfig cydocConfig;

    @Autowired
    public BotonDaoImpl(@Qualifier("speedEntityManagerFactory") EntityManager entityManager, CydocConfig cydocConfig) {
        this.entityManager = entityManager;
        this.cydocConfig = cydocConfig;
    }

    @Override
    public List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid) {
        return buscarPorPerfilGrid(perfil, codigoGrid, null, null, null);
    }

    @Override
    public List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid, boolean responsable) {
        return buscarPorPerfilGrid(perfil, codigoGrid, null, null, responsable);
    }

    @Override
    public List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid, Character paraEstado) {
        return buscarPorPerfilGrid(perfil, codigoGrid, null, paraEstado, null);
    }

    @Override
    public List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid, Character paraEstado, boolean responsable) {
        return buscarPorPerfilGrid(perfil, codigoGrid, null, paraEstado, responsable);
    }

    @Override
    public List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid, TipoProceso tipoProceso, Character paraEstado) {
        return buscarPorPerfilGrid(perfil, codigoGrid, tipoProceso, paraEstado, null);
    }

    @Override
    public List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid, TipoProceso tipoProceso, Character paraEstado, Boolean responsable) {
        String parametros = "\n\tPerfil:\t\t" + perfil.getLabel() + "\n\tCodigoGrid:\t" + codigoGrid;

        System.out.println("--------buscarPorPerfilGrid-/elements---------");
        System.out.println(perfil);
        System.out.println(codigoGrid);
        System.out.println(tipoProceso);
        System.out.println(paraEstado);
        System.out.println(responsable);
        System.out.println("---------------------------------------------");

        String sql = "SELECT b FROM Boton b WHERE b.id IN (SELECT r.id.recurso.id FROM RecursoPorPerfil r WHERE r.id.perfil.id=:idPerfil";
        if (tipoProceso != null) {
            sql += " AND (r.tipoProceso IS NULL OR r.tipoProceso=:tipoProceso)";
            parametros += "\n\tTipoProceso:\t" + tipoProceso.getLabel();
        }
        if (paraEstado != null) {
            sql += " AND (r.paraEstado IS NULL OR r.paraEstado=:paraEstado)";
            parametros += "\n\tParaEstado:\t" + paraEstado;
        }
        if (responsable != null) {
            sql += " AND (r.responsable IS NULL OR r.responsable=:responsable)";
            parametros += "\n\tResponsable:\t" + responsable;
        }
        LOGGER.debug("Buscando botones - Parametros:" + parametros);
        sql += ") AND UPPER(b.codigo)=:codigoGrid AND b.estado=:estado";
        //FIXME hardcode!
        boolean agregarRestriccion = false;

        if (cydocConfig.getDigitalizadorHabilitado()) {
            if (perfil.getCodigo().equals(Constantes.PERFIL_MESA_DE_PARTES) && codigoGrid.equals(Constantes.GRID_DOCUMENTO)) {
                sql += " AND b.url != :url ";
                agregarRestriccion = true;
            }
        }
        sql += " ORDER BY b.orden";
        Query q = entityManager.createQuery(sql);
        q.setParameter("idPerfil", perfil.getId());
        if (tipoProceso != null) {
            q.setParameter("tipoProceso", tipoProceso);
            System.out.println("--------tipoProceso---------");
            System.out.println(tipoProceso);
            System.out.println("-----------------");
        }
        if (paraEstado != null) {
            q.setParameter("paraEstado", paraEstado);
            System.out.println("--------paraEstado---------");
            System.out.println(paraEstado);
            System.out.println("-----------------");
        }
        if (responsable != null) {
            q.setParameter("responsable", responsable);
            System.out.println("--------responsable---------");
            System.out.println(responsable);
            System.out.println("-----------------");
        }
        q.setParameter("codigoGrid", codigoGrid.toUpperCase());
        q.setParameter("estado", Constantes.ESTADO_ACTIVO);
        if (agregarRestriccion) {
            q.setParameter("url", Constantes.URL_ADJUNTAR_ARCHIVO);
        }
        return q.getResultList();
    }

    @Override
    public List<Boton> buscarPorPerfilGridExceptoParametro(Perfil perfil, String codigoGrid, TipoProceso tipoProceso, Character paraEstado, Boolean responsable, String parametro) {
        String sql = "SELECT b FROM Boton b WHERE b.id IN (SELECT r.id.recurso.id FROM RecursoPorPerfil r WHERE r.id.perfil.id=:idPerfil";

        System.out.println("--------BuscarPorPerfilGridExceptoParametro Botones---------");

        if (tipoProceso != null) {
            System.out.println("--------tipoProceso---------");
            System.out.println(tipoProceso.getId());
            System.out.println("-----------------");

            sql += " AND (r.tipoProceso IS NULL OR r.tipoProceso=:tipoProceso)";
        }
        if (paraEstado != null) {
            System.out.println("--------paraEstado---------");
            System.out.println(paraEstado);
            System.out.println("-----------------");

            sql += " AND (r.paraEstado IS NULL OR r.paraEstado=:paraEstado)";
        }
        if (responsable != null) {
            System.out.println("--------responsable---------");
            System.out.println(responsable);
            System.out.println("-----------------");

            sql += " AND (r.responsable IS NULL OR r.responsable=:responsable)";
        }
        if (parametro != null) {
            System.out.println("--------parametro---------");
            System.out.println(parametro);
            System.out.println("-----------------");

            sql += " AND (b.parametro NOT LIKE :parametro)";
        }

        sql += ") AND UPPER(b.codigo)=:codigoGrid AND b.estado=:estado";

        boolean agregarRestriccion = false;
        if (cydocConfig.getDigitalizadorHabilitado()) {
            if (perfil.getCodigo().equals(Constantes.PERFIL_MESA_DE_PARTES) && codigoGrid.equals(Constantes.GRID_DOCUMENTO)) {
                sql += " AND b.url != :url ";
                agregarRestriccion = true;
            }
        }
        sql += " ORDER BY b.orden";
        Query q = entityManager.createQuery(sql);
        q.setParameter("idPerfil", perfil.getId());
        if (tipoProceso != null) {
            q.setParameter("tipoProceso", tipoProceso);
        }
        if (paraEstado != null) {
            q.setParameter("paraEstado", paraEstado);
        }
        if (responsable != null) {
            q.setParameter("responsable", responsable);
        }
        if (parametro != null) {
            q.setParameter("parametro", "%" + parametro + "%");
        }

        System.out.println("--------codigoGrid---------");
        System.out.println(codigoGrid.toUpperCase());
        System.out.println("-----------------");

        System.out.println("--------estado---------");
        System.out.println(Constantes.ESTADO_ACTIVO);
        System.out.println("-----------------");

        q.setParameter("codigoGrid", codigoGrid.toUpperCase());
        q.setParameter("estado", Constantes.ESTADO_ACTIVO);
        if (agregarRestriccion) {
            q.setParameter("url", Constantes.URL_ADJUNTAR_ARCHIVO);
        }

        List<Boton> botones = q.getResultList();

        for (Boton boton : botones) {
            System.out.println("--------BOTON---------");
            System.out.println(boton.getDescripcion());
            System.out.println("-----------------");

        }

        return botones;
    }

    @Override
    public List<Boton> getActivos() {
        LOGGER.debug("[DAO] - getActivos()");
        String sql = "SELECT b FROM Boton b WHERE b.estado = :estado ORDER BY b.nombre";
        return entityManager.createQuery(sql).setParameter("estado", Constantes.ESTADO_ACTIVO).getResultList();
    }

    @Override
    public List<Boton> obtenerPorPerfilGrid(Integer idPerfil, Integer idGrid) {
        LOGGER.debug("[DAO] - getBotonesAsociadosGridPerfil()");
        String sql = "SELECT bgp.id.boton FROM BotonPorGridPorPerfil bgp WHERE bgp.id.perfil.id = :idPerfil AND bgp.id.grid.id = :idGrid ORDER BY bgp.id.boton.orden";
        Query q = entityManager.createQuery(sql);
        q.setParameter("idPerfil", idPerfil);
        q.setParameter("idGrid", idGrid);
        return q.getResultList();
    }

    public List<Boton> getBotonesNoAsociadosGridPerfil(Integer idPerfil, Integer idGrid) {
        LOGGER.debug("[DAO] - getBotonesNoAsociadosGridPerfil()");
        String sql = "SELECT b FROM Boton b WHERE b.estado = :estado AND b.id NOT IN (SELECT bgp.id.boton.id FROM BotonPorGridPorPerfil bgp WHERE bgp.id.perfil.id = :idPerfil AND bgp.id.grid.id = :idGrid) ORDER BY b.nombre";
        return entityManager.createQuery(sql).setParameter("estado", Constantes.ESTADO_ACTIVO).setParameter("idPerfil", idPerfil).setParameter("idGrid", idGrid).getResultList();
    }

    @Override
    public Boton obtenerBotonPorUrl(String url) {
        String sql = "SELECT b FROM Boton b WHERE b.url = :url";
        Query q = entityManager.createQuery(sql);
        return (Boton) q.setParameter("url", url).setMaxResults(1).getSingleResult();
    }

    @Override
    public List<Boton> getBotonAdendaAutomaticaPosicionContractual(Perfil perfil, String codigoGrid, TipoProceso tipoProceso, Character paraEstado, Boolean responsable, String parametro, Integer idRecurso) {
        String sql = "SELECT b FROM Boton b WHERE b.id IN (SELECT r.id.recurso.id FROM RecursoPorPerfil r WHERE r.id.perfil.id=:idPerfil";

        System.out.println("---------------------------------------------");
        System.out.println("getBotonAdendaAutomaticaPosicionContractual");
        System.out.println("---------------------------------------------");

        if (tipoProceso != null) {
            System.out.println("--------tipoProceso---------");
            System.out.println(tipoProceso);
            System.out.println("-----------------");

            sql += " AND (r.tipoProceso IS NULL OR r.tipoProceso=:tipoProceso)";
        }
        if (paraEstado != null) {
            System.out.println("--------paraEstado---------");
            System.out.println(paraEstado);
            System.out.println("-----------------");

            sql += " AND (r.paraEstado IS NULL OR r.paraEstado=:paraEstado)";
        }
        if (responsable != null) {
            System.out.println("--------responsable---------");
            System.out.println(responsable);
            System.out.println("-----------------");

            sql += " AND (r.responsable IS NULL OR r.responsable=:responsable)";
        }
        if (parametro != null) {
            System.out.println("--------parametro---------");
            System.out.println(parametro);
            System.out.println("-----------------");

            sql += " AND (b.parametro NOT LIKE :parametro)";
        }

        if (idRecurso != null) {
            System.out.println("--------idRecurso---------");
            System.out.println(idRecurso);
            System.out.println("-----------------");

            sql += " AND (r.id.recurso.id=:idRecurso)";
        }

        sql += ") AND UPPER(b.codigo)=:codigoGrid AND b.estado=:estado";

        boolean agregarRestriccion = false;
        if (cydocConfig.getDigitalizadorHabilitado()) {
            if (perfil.getCodigo().equals(Constantes.PERFIL_MESA_DE_PARTES) && codigoGrid.equals(Constantes.GRID_DOCUMENTO)) {
                sql += " AND b.url != :url ";
                agregarRestriccion = true;
            }
        }
        sql += " ORDER BY b.orden";
        Query q = entityManager.createQuery(sql);
        q.setParameter("idPerfil", perfil.getId());

        if (tipoProceso != null) {
            q.setParameter("tipoProceso", tipoProceso);
        }
        if (paraEstado != null) {
            q.setParameter("paraEstado", paraEstado);
        }
        if (responsable != null) {
            q.setParameter("responsable", responsable);
        }
        if (parametro != null) {
            q.setParameter("parametro", "%" + parametro + "%");
        }
        if (idRecurso != null) {
            q.setParameter("idRecurso", idRecurso);
        }

        q.setParameter("codigoGrid", codigoGrid.toUpperCase());
        q.setParameter("estado", Constantes.ESTADO_ACTIVO);

        if (agregarRestriccion) {
            q.setParameter("url", Constantes.URL_ADJUNTAR_ARCHIVO);
        }

        return q.getResultList();
    }
}

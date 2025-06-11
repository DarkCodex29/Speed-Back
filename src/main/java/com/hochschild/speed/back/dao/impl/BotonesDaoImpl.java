package com.hochschild.speed.back.dao.impl;

import com.hochschild.speed.back.config.CydocConfig;
import com.hochschild.speed.back.dao.BotonesDao;
import com.hochschild.speed.back.model.domain.speed.Boton;
import com.hochschild.speed.back.model.domain.speed.Perfil;
import com.hochschild.speed.back.model.domain.speed.TipoProceso;
import com.hochschild.speed.back.util.Constantes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Query;

@Repository
public class BotonesDaoImpl implements BotonesDao {
    private static final org.apache.log4j.Logger LOGGER = Logger.getLogger(BotonesDaoImpl.class.getName());
    private final CydocConfig cydocConfig;
    private final EntityManager entityManager;
    @Autowired
    public BotonesDaoImpl(CydocConfig cydocConfig,
                          @Qualifier("speedEntityManagerFactory") EntityManager entityManager) {
        this.cydocConfig = cydocConfig;
        this.entityManager = entityManager;
    }

    @Override
    public List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid) {
        return buscarPorPerfilGrid(perfil, codigoGrid, null, null, null);

    }

    @Override
    public List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid, Boolean responsable) {
        return buscarPorPerfilGrid(perfil, codigoGrid, null, null, responsable);
    }

    @Override
    public List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid, Character paraEstado) {
        return buscarPorPerfilGrid(perfil, codigoGrid, null, paraEstado, null);
    }

    @Override
    public List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid, Character paraEstado, Boolean responsable) {
        return buscarPorPerfilGrid(perfil, codigoGrid, null, paraEstado, responsable);
    }

    @Override
    public List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid, TipoProceso tipoProceso, Character paraEstado) {
        return buscarPorPerfilGrid(perfil, codigoGrid, tipoProceso, paraEstado, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Boton> buscarPorPerfilGrid(Perfil perfil, String codigoGrid, TipoProceso tipoProceso, Character paraEstado, Boolean responsable) {
        String parametros = "\n\tPerfil:\t\t" + perfil.getLabel() + "\n\tCodigoGrid:\t" + codigoGrid;

        LOGGER.info("--------buscarPorPerfilGrid-/elements---------");
        LOGGER.info(perfil);
        LOGGER.info(codigoGrid);
        LOGGER.info(tipoProceso);
        LOGGER.info(paraEstado);
        LOGGER.info(responsable);
        LOGGER.info("---------------------------------------------");

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
        LOGGER.info("Buscando botones - Parametros:" + parametros);
        sql += ") AND UPPER(b.codigo)=:codigoGrid AND b.estado=:estado";
        //FIXME hardcode!
        Boolean agregarRestriccion = false;
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
            LOGGER.info("--------tipoProceso---------");
            LOGGER.info(tipoProceso);
            LOGGER.info("-----------------");
        }
        if (paraEstado != null) {
            q.setParameter("paraEstado", paraEstado);
            LOGGER.info("--------paraEstado---------");
            LOGGER.info(paraEstado);
            LOGGER.info("-----------------");
        }
        if (responsable != null) {
            q.setParameter("responsable", responsable);
            LOGGER.info("--------responsable---------");
            LOGGER.info(responsable);
            LOGGER.info("-----------------");
        }
        q.setParameter("codigoGrid", codigoGrid.toUpperCase());
        q.setParameter("estado", Constantes.ESTADO_ACTIVO);
        if (agregarRestriccion) {
            q.setParameter("url", Constantes.URL_ADJUNTAR_ARCHIVO);
        }
        return eliminarDuplicadosBotones(q.getResultList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Boton> buscarPorPerfilGridExceptoParametro(Perfil perfil, String codigoGrid, TipoProceso tipoProceso, Character paraEstado, Boolean responsable, String parametro) {
        String sql = "SELECT b FROM Boton b WHERE b.id IN (SELECT r.id.recurso.id FROM RecursoPorPerfil r WHERE r.id.perfil.id=:idPerfil";

        LOGGER.info("--------BuscarPorPerfilGridExceptoParametro Botones---------");

        if (tipoProceso != null) {
            LOGGER.info("--------tipoProceso---------");
            LOGGER.info(tipoProceso.getId());
            LOGGER.info("-----------------");

            sql += " AND (r.tipoProceso IS NULL OR r.tipoProceso=:tipoProceso)";
        }
        if (paraEstado != null) {
            LOGGER.info("--------paraEstado---------");
            LOGGER.info(paraEstado);
            LOGGER.info("-----------------");

            sql += " AND (r.paraEstado IS NULL OR r.paraEstado=:paraEstado)";
        }
        if (responsable != null) {
            LOGGER.info("--------responsable---------");
            LOGGER.info(responsable);
            LOGGER.info("-----------------");

            sql += " AND (r.responsable IS NULL OR r.responsable=:responsable)";
        }
        if (parametro != null) {
            LOGGER.info("--------parametro---------");
            LOGGER.info(parametro);
            LOGGER.info("-----------------");

            sql += " AND (b.parametro NOT LIKE :parametro)";
        }

        sql += ") AND UPPER(b.codigo)=:codigoGrid AND b.estado=:estado";

        Boolean agregarRestriccion = false;
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

        LOGGER.info("--------codigoGrid---------");
        LOGGER.info(codigoGrid.toUpperCase());
        LOGGER.info("-----------------");

        LOGGER.info("--------estado---------");
        LOGGER.info(Constantes.ESTADO_ACTIVO);
        LOGGER.info("-----------------");

        q.setParameter("codigoGrid", codigoGrid.toUpperCase());
        q.setParameter("estado", Constantes.ESTADO_ACTIVO);
        if (agregarRestriccion) {
            q.setParameter("url", Constantes.URL_ADJUNTAR_ARCHIVO);
        }

        return eliminarDuplicadosBotones(q.getResultList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Boton> getActivos() {
        LOGGER.info("[DAO] - getActivos()");
        String sql = "SELECT b FROM Boton b WHERE b.estado = :estado ORDER BY b.nombre";
        return eliminarDuplicadosBotones(entityManager.createQuery(sql).setParameter("estado", Constantes.ESTADO_ACTIVO).getResultList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Boton> obtenerPorPerfilGrid(Integer idPerfil, Integer idGrid) {
        LOGGER.info("[DAO] - getBotonesAsociadosGridPerfil()");
        String sql = "SELECT bgp.id.boton FROM BotonPorGridPorPerfil bgp WHERE bgp.id.perfil.id = :idPerfil AND bgp.id.grid.id = :idGrid ORDER BY bgp.id.boton.orden";
        Query q = entityManager.createQuery(sql);
        q.setParameter("idPerfil", idPerfil);
        q.setParameter("idGrid", idGrid);
        return eliminarDuplicadosBotones(q.getResultList());
    }

    @SuppressWarnings("unchecked")
    public List<Boton> getBotonesNoAsociadosGridPerfil(Integer idPerfil, Integer idGrid) {
        LOGGER.info("[DAO] - getBotonesNoAsociadosGridPerfil()");
        String sql = "SELECT b FROM Boton b WHERE b.estado = :estado AND b.id NOT IN (SELECT bgp.id.boton.id FROM BotonPorGridPorPerfil bgp WHERE bgp.id.perfil.id = :idPerfil AND bgp.id.grid.id = :idGrid) ORDER BY b.nombre";
        return eliminarDuplicadosBotones(entityManager.createQuery(sql).setParameter("estado", Constantes.ESTADO_ACTIVO).setParameter("idPerfil", idPerfil).setParameter("idGrid", idGrid).getResultList());
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

        LOGGER.info("---------------------------------------------");
        LOGGER.info("getBotonAdendaAutomaticaPosicionContractual");
        LOGGER.info("---------------------------------------------");

        if (tipoProceso != null) {
            LOGGER.info("--------tipoProceso---------");
            LOGGER.info(tipoProceso);
            LOGGER.info("-----------------");

            sql += " AND (r.tipoProceso IS NULL OR r.tipoProceso=:tipoProceso)";
        }
        if (paraEstado != null) {
            LOGGER.info("--------paraEstado---------");
            LOGGER.info(paraEstado);
            LOGGER.info("-----------------");

            sql += " AND (r.paraEstado IS NULL OR r.paraEstado=:paraEstado)";
        }
        if (responsable != null) {
            LOGGER.info("--------responsable---------");
            LOGGER.info(responsable);
            LOGGER.info("-----------------");

            sql += " AND (r.responsable IS NULL OR r.responsable=:responsable)";
        }
        if (parametro != null) {
            LOGGER.info("--------parametro---------");
            LOGGER.info(parametro);
            LOGGER.info("-----------------");

            sql += " AND (b.parametro NOT LIKE :parametro)";
        }

        if (idRecurso != null) {
            LOGGER.info("--------idRecurso---------");
            LOGGER.info(idRecurso);
            LOGGER.info("-----------------");

            sql += " AND (r.id.recurso.id=:idRecurso)";
        }

        sql += ") AND UPPER(b.codigo)=:codigoGrid AND b.estado=:estado";

        Boolean agregarRestriccion = false;
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

        return eliminarDuplicadosBotones(q.getResultList());
    }

    private List<Boton> eliminarDuplicadosBotones(List<Boton> botones){
        Iterator<Boton> iterator = botones.iterator();

        // Recorre la lista y elimina los objetos que cumplen con la condición
        while (iterator.hasNext()) {
            Boton objetoActual = iterator.next();
            if (existeOtroConMismoUrl(objetoActual, botones)) {
                iterator.remove();
            }
        }

        return botones;
    }

    private static boolean existeOtroConMismoUrl(Boton boton, List<Boton> lista) {
        for (Boton otroObjeto : lista) {
            if (otroObjeto != boton && otroObjeto.getUrl().equals(boton.getUrl())) {
                return true;
            }
        }
        return false;
    }
}

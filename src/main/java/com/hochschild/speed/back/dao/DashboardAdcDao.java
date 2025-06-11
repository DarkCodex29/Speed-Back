package com.hochschild.speed.back.dao;

import com.hochschild.speed.back.model.bean.dashboard.adc.ProcesoVisadoBean;
import com.hochschild.speed.back.model.bean.dashboard.adc.ProcesoFirmaBean;
import com.hochschild.speed.back.model.bean.dashboard.adc.GeneralAdcBean;
import java.util.List;

public interface DashboardAdcDao {
    List<GeneralAdcBean> solicitudesGenerales();
    List<GeneralAdcBean> solicitudesPorGrupo();
    List<ProcesoFirmaBean> procesoPorFirma();
    List<ProcesoVisadoBean> procesoPorVisado();
}

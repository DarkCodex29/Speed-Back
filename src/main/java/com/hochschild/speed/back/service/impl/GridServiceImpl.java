package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.Expediente;
import com.hochschild.speed.back.model.domain.speed.Proceso;
import com.hochschild.speed.back.service.GridService;
import com.hochschild.speed.back.util.DateUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("GridService")
public class GridServiceImpl implements GridService {

    @Override
    public Date calcularFechaFinconPlazo(Expediente e, Proceso p){
        if(p.getPlazo() != null){
            return DateUtil.sumarFechasDias(e.getFechaCreacion(),p.getPlazo());
        }
        return null;
    }
}

package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.ServicioDTS;
import com.hochschild.speed.back.model.filter.reportefilter.ServicioFilter;
import com.hochschild.speed.back.repository.speed.ServicioDTSRepository;
import com.hochschild.speed.back.service.ServicioDTSService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioDTSServiceImpl implements ServicioDTSService {
    private final ServicioDTSRepository servicioDTSRepository;

    public ServicioDTSServiceImpl(ServicioDTSRepository servicioDTSRepository) {
        this.servicioDTSRepository = servicioDTSRepository;
    }

    @Override
    public List<ServicioDTS> filtrarServicios(ServicioFilter servicioFilter) {
        String numDocumento = servicioFilter.getNum();
        String idPais = servicioFilter.getIdP();
        String idCompania = servicioFilter.getIdC();
        String idArea = servicioFilter.getIdA();
        String idTipoUBicacion = servicioFilter.getTUb();
        String idUbicacion = servicioFilter.getIdU();
        String idContraparte = servicioFilter.getIdCo();
        String fechaVencDesde = servicioFilter.getFVI();
        String fechaVencHasta = servicioFilter.getFVF();
        String valorContratoDesde = servicioFilter.getMD();
        String valorContratoHasta = servicioFilter.getMH();
        String idTipoContrato = servicioFilter.getIdTC();
        String idEstado = servicioFilter.getEst();

        Integer contraparte = 0;
        Integer pais = 0;
        Integer compania = 0;
        Integer area = 0;
        Integer tipoUbicacion = 0;
        Integer ubicacion = 0;
        Double montoInicial = 0.00;
        Double montoFinal = 0.00;
        String estado = "";
        Integer tipoContrato = 0;
        if ("".equals(idContraparte)) {
            contraparte = 0;
        } else {
            contraparte = Integer.valueOf(idContraparte);
        }
        if ("".equals(valorContratoDesde) && "".equals(valorContratoHasta)) {
            montoInicial = 0.000;
            montoFinal = 0.000;
        } else {
            montoInicial = Double.valueOf(valorContratoDesde);
            montoFinal = Double.valueOf(valorContratoHasta);
        }
        if ("0".equals(idEstado)) {
            estado = "";
        } else {
            estado = idEstado;
        }
        if ("".equals(fechaVencDesde) && "".equals(fechaVencHasta)) {
            fechaVencDesde = "";
            fechaVencHasta = "";
        }
        tipoContrato = Integer.valueOf(idTipoContrato);
        pais = Integer.valueOf(idPais);
        compania = Integer.valueOf(idCompania);
        area = Integer.valueOf(idArea);
        System.out.println("============= ID AREA" + area);
        tipoUbicacion = Integer.valueOf(idTipoUBicacion);
        ubicacion = Integer.valueOf(idUbicacion);
        return this.servicioDTSRepository.consultarServiciosBusqueda(contraparte, numDocumento, fechaVencDesde, fechaVencHasta, montoInicial, montoFinal, tipoContrato, estado, pais, compania, area, tipoUbicacion, ubicacion);
    }
}

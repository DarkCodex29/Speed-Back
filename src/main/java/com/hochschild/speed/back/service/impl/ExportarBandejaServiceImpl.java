package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.CydocConfig;
import com.hochschild.speed.back.model.domain.speed.utils.FilaBandejaEntradaDTS;
import com.hochschild.speed.back.repository.speed.ExpedienteRepository;
import com.hochschild.speed.back.repository.speed.HcDocumentoLegalRepository;
import com.hochschild.speed.back.repository.speed.TrazaRepository;
import com.hochschild.speed.back.service.ExportarBandejaService;
import com.hochschild.speed.back.util.AppUtil;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ExportarBandejaService")
public class ExportarBandejaServiceImpl implements ExportarBandejaService {

    private final CydocConfig cydocConfig;
    private final TrazaRepository trazaRepository;
    private final ExpedienteRepository expedienteRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;

    @Autowired
    public ExportarBandejaServiceImpl(CydocConfig cydocConfig,
                                      TrazaRepository trazaRepository,
                                      ExpedienteRepository expedienteRepository,
                                      HcDocumentoLegalRepository hcDocumentoLegalRepository) {
        this.cydocConfig = cydocConfig;
        this.trazaRepository = trazaRepository;
        this.expedienteRepository = expedienteRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
    }


    public Workbook exportarBandejaEntrada(ArrayList<Integer> trazas) {
        try {
            List<FilaBandejaEntradaDTS> filas = trazaRepository.obtenerFilasBandejaExport(trazas);
            return exportarFilasExcel(filas);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Workbook exportarBandejaExpedientes(ArrayList<Integer> expedientes) {
        try {
            List<FilaBandejaEntradaDTS> filas = expedienteRepository.obtenerFilasBandejaExport(expedientes);
            return exportarFilasExcel(filas);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Workbook exportarFilasExcel(List<FilaBandejaEntradaDTS> filas) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            XLSTransformer transformer = new XLSTransformer();

            if (filas != null && !filas.isEmpty()) {
                for (FilaBandejaEntradaDTS fila : filas) {
                    fila.setStrEstado(AppUtil.getNombreEstadoDL(fila.getEstado()));

                    if (fila.getFechaBorrador() != null) {
                        fila.setStrFechaBorrador(dateFormat.format(fila.getFechaBorrador()));
                    }
                    if (fila.getFechaSolicitud() != null) {
                        fila.setStrFechaSolicitud(dateFormat.format(fila.getFechaSolicitud()));
                    }
                }
            }

            Map beans = new HashMap();
            beans.put("filas", filas);

            File plantilla = new File(cydocConfig.getCarpetaPlantillasExcel() + File.separator + "ListaDocumentos.xls");
            FileInputStream plantillaStream = new FileInputStream(plantilla);

            return transformer.transformXLS(plantillaStream, beans);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

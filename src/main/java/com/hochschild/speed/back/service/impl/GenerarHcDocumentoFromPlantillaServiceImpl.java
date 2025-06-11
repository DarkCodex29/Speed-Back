package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.config.AlfrescoConfig;
import com.hochschild.speed.back.config.CydocConfig;
import com.hochschild.speed.back.dao.HcUbicacionPorDocumentoDao;
import com.hochschild.speed.back.model.domain.speed.*;
import com.hochschild.speed.back.repository.speed.*;
import com.hochschild.speed.back.service.AlfrescoService;
import com.hochschild.speed.back.service.GenerarHcDocumentoFromPlantillaService;
import com.hochschild.speed.back.util.AppUtil;
import com.hochschild.speed.back.util.Constantes;
import com.hochschild.speed.back.util.DateUtil;
import com.hochschild.speed.back.util.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("GenerarHcDocumentoFromPlantillaService")
public class GenerarHcDocumentoFromPlantillaServiceImpl implements GenerarHcDocumentoFromPlantillaService {
    private static final Logger LOGGER = Logger.getLogger(GenerarHcDocumentoFromPlantillaServiceImpl.class.getName());
    private final CydocConfig cydocConfig;

    private final AlfrescoConfig alfrescoConfig;
    private final AlfrescoService alfrescoService;
    private final HcAdendaRepository hcAdendaRepository;
    private final HcContratoRepository hcContratoRepository;
    private final HcPlantillaRepository hcPlantillaRepository;
    private final HcDocumentoLegalRepository hcDocumentoLegalRepository;
    private final HcRepresentantePorDocumentoRepository hcRepresentantePorDocumentoRepository;

    private final HcTipoContratoRepository hcTipoContratoRepository;
    private final HcPenalidadPorDocumentoLegalRepository hcPenalidadPorDocumentoLegalRepository;
    private final HcPenalidadRepository hcPenalidadRepository;
    private final HcReiteranciaPorPenalidadRepository hcReiteranciaPorPenalidadRepository;
    private final HcUbicacionRepository hcUbicacionRepository;

    private final HcUbicacionPorDocumentoDao hcUbicacionDao;

    private final ArchivoRepository archivoRepository;
    private final HcUbicacionPorDocumentoRepository hcUbicacionPorDocumentoRepository;


    private final HcTipoContratoConfiguracionRepository hcTipoContratoConfiguracionRepository;

    @Autowired
    public GenerarHcDocumentoFromPlantillaServiceImpl(CydocConfig cydocConfig, AlfrescoConfig alfrescoConfig, AlfrescoService alfrescoService, HcAdendaRepository hcAdendaRepository,
                                                      HcContratoRepository hcContratoRepository,
                                                      HcPlantillaRepository hcPlantillaRepository,
                                                      HcDocumentoLegalRepository hcDocumentoLegalRepository,
                                                      HcRepresentantePorDocumentoRepository hcRepresentantePorDocumentoRepository,
                                                      HcTipoContratoRepository hcTipoContratoRepository,
                                                      HcPenalidadPorDocumentoLegalRepository penalidadPorDocumentoLegalRepository,
                                                      HcPenalidadRepository hcPenalidadRepository,
                                                      HcReiteranciaPorPenalidadRepository hcReiteranciaPorPenalidadRepository,
                                                      HcUbicacionRepository hcUbicacionRepository,
                                                      HcUbicacionPorDocumentoDao hcUbicacionDao, ArchivoRepository archivoRepository,
                                                      HcUbicacionPorDocumentoRepository hcUbicacionPorDocumentoRepository,
                                                      HcTipoContratoConfiguracionRepository hcTipoContratoConfiguracionRepository) {
        this.cydocConfig = cydocConfig;
        this.alfrescoConfig = alfrescoConfig;
        this.alfrescoService = alfrescoService;
        this.hcAdendaRepository = hcAdendaRepository;
        this.hcContratoRepository = hcContratoRepository;
        this.hcPlantillaRepository = hcPlantillaRepository;
        this.hcDocumentoLegalRepository = hcDocumentoLegalRepository;
        this.hcRepresentantePorDocumentoRepository = hcRepresentantePorDocumentoRepository;
        this.hcTipoContratoRepository = hcTipoContratoRepository;
        this.hcPenalidadPorDocumentoLegalRepository = penalidadPorDocumentoLegalRepository;
        this.hcPenalidadRepository = hcPenalidadRepository;
        this.hcReiteranciaPorPenalidadRepository = hcReiteranciaPorPenalidadRepository;
        this.hcUbicacionRepository = hcUbicacionRepository;
        this.hcUbicacionDao = hcUbicacionDao;
        this.archivoRepository = archivoRepository;
        this.hcUbicacionPorDocumentoRepository = hcUbicacionPorDocumentoRepository;
        this.hcTipoContratoConfiguracionRepository = hcTipoContratoConfiguracionRepository;
    }

    @Override
    public List<HcTipoContrato> findLstHcTipoContratoCodigoAdenda() {
        return hcTipoContratoRepository.findLstBy(Constantes.TIPO_DOCUMENTO_CONTRATO_ADENDA);
    }

    @Override
    public String generarHcDocumentoFromPlantilla(HcDocumentoLegal documentoLegal, Integer idHcTipoContrato) {
        HcAdenda hcAdenda;
        HcAdenda adendaEncontrada;
        HcContrato contratoActual;
        HcDocumentoLegal documentoActual;
        HcPlantilla hcPlantilla;
        List<Cliente> representantes;
        List<HcAdenda> adendasContrato;
        List<Cliente> representantesContratoAdenda;
        List<HcUbicacion> ubicaciones;
        List<HcPlantilla> plantillas;
        Map<String, String> values;
        Map<String, Object> plantillaFromAlfresco;
        String dniRepresentanteLegal, nombreRespresentanteLegal, outputPathFileName;
        String[] nombreMes = {"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};

        Assert.notNull(documentoLegal, "hcDocumentoLegal was not received");
        LOGGER.info("hcDocumentoLegal " + documentoLegal.getId() + " idHcTipoContratoCodigoAdenda " + idHcTipoContrato);
        documentoActual = hcDocumentoLegalRepository.findById(documentoLegal.getId());
        hcAdenda = hcAdendaRepository.findById(documentoLegal.getId());
        if (hcAdenda != null) {
            contratoActual = hcAdenda.getContrato();
            LOGGER.info("Trabajando con ADENDA");
        } else {
            contratoActual = hcContratoRepository.findById(documentoLegal.getId());
            LOGGER.info("Trabajando con CONTRATO");
        }
        dniRepresentanteLegal = "";
        nombreRespresentanteLegal = "";
        representantes = hcRepresentantePorDocumentoRepository.obtenerRepresentantesDocumentoLegal(documentoActual.getId());
        if (representantes != null && !representantes.isEmpty()) {
            dniRepresentanteLegal = representantes.get(0).getNumeroIdentificacion();
            nombreRespresentanteLegal = representantes.get(0).getNombre() + " " + representantes.get(0).getApellidoPaterno() + " " + representantes.get(0).getApellidoMaterno();
        }

        values = new HashMap<>();
        values.put("numeroContrato", contratoActual.getDocumentoLegal().getNumero());

        LOGGER.info("VALOR A ID VALIDAD RAZON SOCIAL DOCUMENTO LEGAL" + documentoActual.getContraparte().getTipo().getNombre());

        if (documentoActual.getContraparte().getTipo().getCodigo().equals(Constantes.TIPO_CLIENTE_NATURAL)) {
            values.put("razonSocialContraparte", documentoActual.getContraparte().getNombres());

        } else if (documentoActual.getContraparte().getTipo().getCodigo().equals(Constantes.TIPO_CLIENTE_JURIDICA)) {
            values.put("razonSocialContraparte", documentoActual.getContraparte().getRazonSocial());
        }

        values.put("fechaInicioContrato", new SimpleDateFormat("dd/MM/yyyy").format(contratoActual.getFechaInicio()));
        values.put("ficD", new SimpleDateFormat("dd").format(contratoActual.getFechaInicio()));
        values.put("ficM", new SimpleDateFormat("MM").format(contratoActual.getFechaInicio()));
        values.put("ficA", new SimpleDateFormat("yyyy").format(contratoActual.getFechaInicio()));
        values.put("montoContrato", contratoActual.getMonto() != null ? String.format("%.2f", contratoActual.getMonto()) : " ");
        values.put("nombreSolicitante", documentoActual.getSolicitante().getNombres() + " " + documentoActual.getSolicitante().getApellidos());
        values.put("abogadoResponsable", documentoActual.getResponsable().getNombres() + " " + documentoActual.getResponsable().getApellidos());
        values.put("numeroRucContraparte", documentoActual.getContraparte().getNumeroIdentificacion());
        values.put("domicilio", documentoActual.getCnt_domicilio());
        values.put("telefono", documentoActual.getCnt_telefono_contacto());
        values.put("correo", documentoActual.getCnt_correo_contacto());
        values.put("plazoContrato", contratoActual.getPeriodicidad());
        values.put("fechaFinContrato", contratoActual.getIndefinido() ? "Indefinido" : new SimpleDateFormat("dd/MM/yyyy").format(contratoActual.getFechaFin()));
        values.put("totalDiasContrato", contratoActual.getIndefinido() ? "Indefinido" : String.valueOf(DateUtil.diferenciasDeFechas(contratoActual.getFechaInicio(), contratoActual.getFechaFin())));
        values.put("periodicidad", contratoActual.getPeriodicidad());
        values.put("montoAdelantado", contratoActual.getMonto_adelanto() == null ? "0" : contratoActual.getMonto_adelanto().toString());
        values.put("periodoRenovar", contratoActual.getPeriodo_renovar() == null ? "-" : contratoActual.getPeriodo_renovar().toString());
        values.put("dniRepresentante", dniRepresentanteLegal);
        values.put("nombreRepresentante", nombreRespresentanteLegal);
        if (documentoActual.getSolicitante().getArea() != null) {
            values.put("areaSolicitante", documentoActual.getSolicitante().getArea().getNombre());
        } else {
            values.put("areaSolicitante", "AREA");
        }
        values.put("observaciones", contratoActual.getDescripcion());
        values.put("moneda", contratoActual.getMoneda().getValor());
        //falta obtener ubicacionesreporte
        values.put("ubicacion", hcUbicacionDao.obtenerUbicacionesReporte(documentoActual.getId()));

        //ETQnumeroAdenda
        if (hcAdenda != null && hcAdenda.getSecuencia() != null) {
            values.put("numeroAdenda", documentoActual.getAdenda().getSecuencia().toString());
        } else {
            values.put("numeroAdenda", null);
        }

        obtenerDatosParaReemplazarPenalidades(documentoLegal.getExpediente().getId(), values);

        //ETQfechaFinAdenda
        if (hcAdenda != null && hcAdenda.getNuevaFechaFin() != null) {
            values.put("fechaFinAdenda", new SimpleDateFormat("dd/MM/yyyy").format(hcAdenda.getNuevaFechaFin()));
        } else {
            values.put("fechaFinAdenda", null);
        }

        if (hcAdenda != null) {

            ubicaciones = hcUbicacionPorDocumentoRepository.obtenerUbicacionesDocumentoLegal(documentoActual.getId());
            adendasContrato = hcAdendaRepository.obtenerAdendasByIdContrato(documentoActual.getAdenda().getContrato().getId());
            LOGGER.info("VALOR DEL CONTRATO ENCONTRADO EN LA ADENDA = " + documentoActual.getAdenda().getContrato().getId());
            if (!adendasContrato.isEmpty()) {
                LOGGER.info("CNT DE ADENDAS EXISTENTES = " + adendasContrato.size());
            }

            String dniRepresentanteLegalContratoAdenda = "";
            String nombreRespresentanteLegalContratoAdenda = "";

            String dniRepresentanteLegalContratoAdenda2 = "";
            String nombreRespresentanteLegalContratoAdenda2 = "";
            String outputTextoRepresentante = "";
            String outputLineaRepresentante = "";
            representantesContratoAdenda = hcRepresentantePorDocumentoRepository.obtenerRepresentantesDocumentoLegal(documentoActual.getAdenda().getContrato().getDocumentoLegal().getId());

            if (representantesContratoAdenda != null && !representantesContratoAdenda.isEmpty()) {
                dniRepresentanteLegalContratoAdenda = representantesContratoAdenda.get(0).getNumeroIdentificacion();
                nombreRespresentanteLegalContratoAdenda = representantesContratoAdenda.get(0).getNombre().toUpperCase() + " " + representantesContratoAdenda.get(0).getApellidoPaterno().toUpperCase() + " " + representantesContratoAdenda.get(0).getApellidoMaterno().toUpperCase();
                //ETQlineaRepresentante2
                outputTextoRepresentante = ".";
                //ETQtextoRepresentante2

                //ETQtextoRepresentante2
                values.put("nombreRepresentante2", " ");
                //ETQlineaRepresentante2
                values.put("dniRepresentante2", " ");
                values.put("lineaRepresentante2", " ");
                values.put("textoRepresentante2", outputTextoRepresentante);
            }

            if (representantes != null && !representantes.isEmpty() && representantes.size() > 1) {
                dniRepresentanteLegalContratoAdenda2 = representantes.get(1).getNumeroIdentificacion();
                nombreRespresentanteLegalContratoAdenda2 = representantes.get(1).getNombre().toUpperCase() + " " + representantes.get(1).getApellidoPaterno().toUpperCase() + " " + representantes.get(1).getApellidoMaterno().toUpperCase();

                //ETQnombreRepresentanteContrato
                values.put("nombreRepresentante2", nombreRespresentanteLegalContratoAdenda2);
                //ETQdniRepresentanteContrato
                values.put("dniRepresentante2", dniRepresentanteLegalContratoAdenda2);

                outputTextoRepresentante = "; y por " + nombreRespresentanteLegalContratoAdenda2 + " identificado con DNI/C.E No. " + dniRepresentanteLegalContratoAdenda2 + ".";
                outputLineaRepresentante = "__________________________";

                //ETQtextoRepresentante2
                values.put("textoRepresentante2", outputTextoRepresentante);
                //ETQlineaRepresentante2
                values.put("lineaRepresentante2", outputLineaRepresentante);
            }

            //ETQrazonSocialContraparteContrato
            if (documentoActual.getAdenda().getContrato().getDocumentoLegal().getContraparte().getTipo().getCodigo().equals(Constantes.TIPO_CLIENTE_NATURAL)) {
                values.put("razonSocialContraparteContrato", documentoActual.getAdenda().getContrato().getDocumentoLegal().getContraparte().getNombres());

            } else if (documentoActual.getAdenda().getContrato().getDocumentoLegal().getContraparte().getTipo().getCodigo().equals(Constantes.TIPO_CLIENTE_JURIDICA)) {
                values.put("razonSocialContraparteContrato", documentoActual.getAdenda().getContrato().getDocumentoLegal().getContraparte().getRazonSocial());
            }
            //ETQnumeroRucContraparteContrato
            values.put("numeroRucContraparteContrato", documentoActual.getAdenda().getContrato().getDocumentoLegal().getContraparte().getNumeroIdentificacion());

            //ETQnombreRepresentanteContrato
            values.put("nombreRepresentanteContrato", nombreRespresentanteLegalContratoAdenda);
            //ETQdniRepresentanteContrato
            values.put("dniRepresentanteContrato", dniRepresentanteLegalContratoAdenda);
            //ETQdireccionContraparteContrato
            values.put("direccionContraparteContrato", documentoActual.getAdenda().getContrato().getDocumentoLegal().getCnt_domicilio());
            //ETQcompania
            values.put("compania", ubicaciones.get(0).getCompania().getNombre().toUpperCase());
            //ETQsumillaContrato
            values.put("sumContrato", documentoActual.getAdenda().getContrato().getDocumentoLegal().getSumilla());
            //ETQtipoContrato
            values.put("tipoContrato", documentoActual.getAdenda().getContrato().getTipo_contrato().getNombre().toUpperCase());
            //ETQficDAdenda
            values.put("ficDAdenda", new SimpleDateFormat("dd").format(documentoActual.getAdenda().getInicioVigencia()));
            //ETQficMAdenda
            values.put("ficMAdenda", new SimpleDateFormat("MM").format(documentoActual.getAdenda().getInicioVigencia()));
            //ETQficAAdenda
            values.put("ficAAdenda", new SimpleDateFormat("yyyy").format(documentoActual.getAdenda().getInicioVigencia()));
            //ETQfechaInicioAdenda
            if (documentoActual.getAdenda().getInicioVigencia() != null) {
                values.put("fechaInicioAdenda", new SimpleDateFormat("dd/MM/yyyy").format(documentoActual.getAdenda().getInicioVigencia()));
            }
            //ETQpropositoAdenda
            if (documentoActual.getAdenda().getDescripcion() != null) {
                values.put("propositoAdenda", documentoActual.getAdenda().getDescripcion());
            }
            //ETQNombreMesAdenda
            int mesValue = Integer.valueOf(new SimpleDateFormat("MM").format(documentoActual.getAdenda().getInicioVigencia()));
            String output = nombreMes[mesValue - 1];
            values.put("nombreMesAdenda", output);

            //ETQtextoAdenda
            String outputFormatoTexto = "";
            String outputVariable = "";
            String textoAdendaVariable = "textoAdenda";
            String dia = "";
            String anio = "";
            String concatOutputFormatoTexto = "";
            int secuenciaAdendaActual = documentoActual.getAdenda().getSecuencia();

            LOGGER.info("ADENDA A EXCLUIR DEL FORMATO = " + documentoActual.getAdenda().getId());

            for (int i = 0; i < adendasContrato.size(); i++) {
                adendaEncontrada = hcAdendaRepository.obtenerByIdContratoAndSecuencia(documentoActual.getAdenda().getContrato().getId(), adendasContrato.get(i).getSecuencia());

                if (adendaEncontrada != null && adendaEncontrada.getInicioVigencia() != null) {
                    dia = new SimpleDateFormat("dd").format(adendaEncontrada.getInicioVigencia());
                    int mesValueFor = Integer.valueOf(new SimpleDateFormat("MM").format(adendaEncontrada.getInicioVigencia()));
                    String mesOutputFor = nombreMes[mesValueFor - 1];
                    anio = new SimpleDateFormat("yyyy").format(adendaEncontrada.getInicioVigencia());

                    if (adendaEncontrada.getSecuencia() != secuenciaAdendaActual) {
                        outputFormatoTexto = "El Contrato fue modificado por Adenda No." + adendaEncontrada.getSecuencia() + " de fecha " + dia + " de " + mesOutputFor + " de " + anio + ".";
                        outputVariable = textoAdendaVariable.concat(adendaEncontrada.getSecuencia().toString());
                        //SetiendoValores
                        values.put(outputVariable, outputFormatoTexto);
                        LOGGER.info("--------------------------");
                        LOGGER.info("variableTextoAdenda = " + outputVariable);
                        LOGGER.info("textoAdenda = " + outputFormatoTexto);
                        LOGGER.info("--------------------------");
                        if (i == 0) {
                            concatOutputFormatoTexto = outputFormatoTexto.trim();
                        } else {
                            concatOutputFormatoTexto = concatOutputFormatoTexto.trim() + "\r" + outputFormatoTexto.trim();
                        }
                    }
                }
            }
            values.put("concatTextoAdenda", concatOutputFormatoTexto);

            //ETQabrevCompania
            if (!ubicaciones.isEmpty() && ubicaciones.get(0).getCompania() != null) {
                values.put("abrevCompania", ubicaciones.get(0).getCompania().getAbreviatura());
            }
        }
        //ETQnombreContacto
        values.put("nombreContacto", documentoActual.getCnt_nombre_contacto());

        //ETQsumilla
        if (documentoActual.getSumilla() != null) {
            values.put("sumilla", documentoActual.getSumilla());
        }

        LOGGER.info("------------------------------DATA generarHcDocumentoFromPlantilla------------------------------A-----");
        LOGGER.info("numeroContrato " + values.get("numeroContrato"));
        LOGGER.info("razonSocialContraparte " + values.get("razonSocialContraparte"));
        LOGGER.info("fechaInicioContrato " + values.get("fechaInicioContrato"));
        LOGGER.info("ficD " + values.get("ficD"));
        LOGGER.info("ficM " + values.get("ficM"));
        LOGGER.info("ficA " + values.get("ficA"));
        LOGGER.info("montoContrato " + values.get("montoContrato"));
        LOGGER.info("nombreSolicitante " + values.get("nombreSolicitante"));
        LOGGER.info("abogadoResponsable " + values.get("abogadoResponsable"));
        LOGGER.info("numeroRucContraparte " + values.get("numeroRucContraparte"));
        LOGGER.info("domicilio " + values.get("domicilio"));
        LOGGER.info("telefono " + values.get("telefono"));
        LOGGER.info("correo " + values.get("correo"));
        LOGGER.info("plazoContrato " + values.get("plazoContrato"));
        LOGGER.info("fechaFinContrato " + values.get("fechaFinContrato"));
        LOGGER.info("totalDiasContrato " + values.get("totalDiasContrato"));
        LOGGER.info("periodicidad " + values.get("periodicidad"));
        LOGGER.info("montoAdelantado " + values.get("montoAdelantado"));
        LOGGER.info("periodoRenovar " + values.get("periodoRenovar"));

        LOGGER.info("dniRepresentante " + values.get("dniRepresentante"));
        LOGGER.info("nombreRepresentante " + values.get("nombreRepresentante"));

        LOGGER.info("areaSolicitante " + values.get("areaSolicitante"));
        LOGGER.info("observaciones " + values.get("observaciones"));
        LOGGER.info("moneda " + values.get("moneda"));
        LOGGER.info("ubicacion " + values.get("ubicacion"));

        if (hcAdenda != null) {
            LOGGER.info("------------------------------NV-----------------------------------");
            LOGGER.info("numeroAdenda " + values.get("numeroAdenda"));
            LOGGER.info("fechaFinAdenda " + values.get("fechaFinAdenda"));
            LOGGER.info("razonSocialContraparteContrato " + values.get("razonSocialContraparteContrato"));
            LOGGER.info("numeroRucContraparteContrato " + values.get("numeroRucContraparteContrato"));
            LOGGER.info("nombreRepresentanteContrato " + values.get("nombreRepresentanteContrato"));
            LOGGER.info("dniRepresentanteContrato " + values.get("dniRepresentanteContrato"));
            LOGGER.info("direccionContraparteContrato " + values.get("direccionContraparteContrato"));
            LOGGER.info("compania " + values.get("compania"));
            LOGGER.info("sumContrato " + values.get("sumContrato"));
            LOGGER.info("tipoContrato " + values.get("tipoContrato"));
            LOGGER.info("ficDAdenda " + values.get("ficDAdenda"));
            LOGGER.info("ficMAdenda " + values.get("ficMAdenda"));
            LOGGER.info("ficAAdenda " + values.get("ficAAdenda"));
            LOGGER.info("fechaInicioAdenda " + values.get("fechaInicioAdenda"));
            LOGGER.info("propositoAdenda " + values.get("propositoAdenda"));
            LOGGER.info("nombreMesAdenda " + values.get("nombreMesAdenda"));
            LOGGER.info("sumilla " + values.get("sumilla"));
            LOGGER.info("concatTextoAdenda  " + values.get("concatTextoAdenda"));

            LOGGER.info("------------------------------VARIABLES NUEVAS-----------------------------------");
            LOGGER.info("Nombre Representante 2 " + values.get("nombreRepresentante2"));
            LOGGER.info("Dni Representante 2 " + values.get("dniRepresentante2"));
            LOGGER.info("Texto Representante 2 " + values.get("textoRepresentante2"));
            LOGGER.info("Linea Representante 2 " + values.get("lineaRepresentante2"));
            LOGGER.info("--------------------------------------------------------------------------------------");
        }
        LOGGER.info("-----------------------------------------------------------------");

        idHcTipoContrato = (hcAdenda != null ? idHcTipoContrato : contratoActual.getTipo_contrato().getId());
        LOGGER.info("Tipo de Contrato [{}]" + idHcTipoContrato);

        if (documentoLegal.getAdenda() != null && documentoLegal.getAdenda().getHcTipoContrato() != null) {
            System.out.println("Adenda Automatica - generarHcDocumentoFromPlantilla");
            HcTipoContratoConfiguracion hcTipoContratoConfiguracion = new HcTipoContratoConfiguracion();
            hcTipoContratoConfiguracion = hcTipoContratoConfiguracionRepository.findByIdTipoContrato(documentoLegal.getAdenda().getHcTipoContrato().getId());

            if (hcTipoContratoConfiguracion.getEsPlantilla().equals(Constantes.ESTADO_PLANTILLA_HABILITADA.toString())) {

                idHcTipoContrato = documentoLegal.getAdenda().getHcTipoContrato().getId();
                System.out.println("Adenda Automatica de tipo contractual = " + idHcTipoContrato);
            }
            LOGGER.info("Tipo de Contrato [{}]" + idHcTipoContrato);
        }

        if (idHcTipoContrato == null) {
            throw new RuntimeException("There is no idHcTipoContrato to work with");
        }

        plantillas = hcPlantillaRepository.findLstBy(idHcTipoContrato, Constantes.ESTADO_ACTIVO);

        if (plantillas == null || plantillas.isEmpty()) {
            LOGGER.warn("No hay plantillas para el tipo de contrato con ID [{}]" + idHcTipoContrato);
            return null;
        }

        //TODO segun la BD, un tipo de contrato puede tener varias plantillas, esta pendiente manejar este tipo de casos.
        hcPlantilla = plantillas.get(0);

        if (StringUtils.isEmpty(hcPlantilla.getRuta())) {
            LOGGER.warn("Plantilla [{}-{}] no tiene ruta en Alfresco" + hcPlantilla.getId() + " " + hcPlantilla.getNombre());
            return null;
        }

        try {
            plantillaFromAlfresco = alfrescoService.descargarArchivo(alfrescoConfig.getSpacePlantillas(), hcPlantilla.getRuta().substring(hcPlantilla.getRuta().lastIndexOf("/") + 1));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        if (hcPlantilla.getRuta().endsWith(".doc")) {
            outputPathFileName = cydocConfig.getCarpetaGenerados() + "/" + documentoActual.getNumero() + ".doc";
        } else {
            outputPathFileName = cydocConfig.getCarpetaGenerados() + "/" + documentoActual.getNumero() + ".docx";
        }

        LOGGER.info("output [{}]" + outputPathFileName);

        if (StringUtils.isNotEmpty(outputPathFileName)) {
            if (outputPathFileName != null && outputPathFileName.toUpperCase().endsWith(".DOCX")) {
                generateDOCX((InputStream) plantillaFromAlfresco.get("stream"), outputPathFileName, values);
            } else {
                generate((InputStream) plantillaFromAlfresco.get("stream"), outputPathFileName, values);
            }
        }

        return outputPathFileName;
    }

    @Override
    public Boolean validarPlantilla(HcDocumentoLegal documentoLegal, Integer idHcTipoContrato) {

        HcAdenda hcAdenda;
        HcContrato contratoActual;
        HcPlantilla hcPlantilla;
        List<HcPlantilla> plantillas;

        Assert.notNull(documentoLegal, "hcDocumentoLegal was not received");
        LOGGER.info("hcDocumentoLegal [{}] idHcTipoContratoCodigoAdenda [{}]");
        hcAdenda = hcAdendaRepository.findById(documentoLegal.getId());

        if (hcAdenda != null) {
            contratoActual = hcAdenda.getContrato();
            LOGGER.info("Trabajando con ADENDA");
        } else {
            contratoActual = hcContratoRepository.findById(documentoLegal.getId());
            LOGGER.info("Trabajando con CONTRATO");
        }

        idHcTipoContrato = (hcAdenda != null ? idHcTipoContrato : contratoActual.getTipo_contrato().getId());
        LOGGER.info("Tipo de Contrato [{}]");

        if (documentoLegal.getAdenda() != null && documentoLegal.getAdenda().getHcTipoContrato() != null) {

            LOGGER.info("Adenda Automatica - validarPlantilla");
            HcTipoContratoConfiguracion hcTipoContratoConfiguracion = new HcTipoContratoConfiguracion();
            hcTipoContratoConfiguracion = hcTipoContratoConfiguracionRepository.findByIdTipoContrato(documentoLegal.getAdenda().getHcTipoContrato().getId());

            if (hcTipoContratoConfiguracion.getEsPlantilla().equals(Constantes.ESTADO_PLANTILLA_HABILITADA.toString())) {

                idHcTipoContrato = documentoLegal.getAdenda().getHcTipoContrato().getId();
                LOGGER.info("Adenda Automatica de tipo contractual = " + idHcTipoContrato);
            }
            LOGGER.info("Tipo de Contrato [{}]");
        }

        if (idHcTipoContrato == null) {
            throw new RuntimeException("There is no idHcTipoContrato to work with");
        }

        plantillas = hcPlantillaRepository.findLstBy(idHcTipoContrato, Constantes.ESTADO_ACTIVO);

        if (plantillas == null || plantillas.isEmpty()) {
            LOGGER.info("No hay plantillas para el tipo de contrato con ID [{}]");
            return false;
        }

        //TODO segun la BD, un tipo de contrato puede tener varias plantillas, esta pendiente manejar este tipo de casos.
        hcPlantilla = plantillas.get(0);

        if (StringUtils.isEmpty(hcPlantilla.getRuta())) {
            LOGGER.info("Plantilla [{}-{}] no tiene ruta en Alfresco");
            return false;
        }

        return true;
    }


    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    public void obtenerDatosParaReemplazarPenalidades(Integer idExpediente, Map<String, String> values) {
        List<HcPenalidad> pena = hcPenalidadRepository.findAll();
        List<HcPenalidadPorDocumentoLegal> lp = hcPenalidadPorDocumentoLegalRepository.obtenerPorIdExpediente(idExpediente);
        for (HcPenalidad p : pena) {
            if (buscarPenalidad(p, lp) != null) {
                HcPenalidadPorDocumentoLegal x = buscarPenalidad(p, lp);
                values.put(p.getEtiqueta(), x.isAplica() ? "SI" : "NO");
                List<HcReiteranciaPorPenalidad> lr = hcReiteranciaPorPenalidadRepository.obtenerPorHcPenalidadPorDocumentoLegal(x.getId());
                for (HcReiteranciaPorPenalidad r : lr) {
                    Integer indice = r.getIndex() + 1;
                    values.put(x.getPenalidad().getEtiqueta().concat("r").concat(indice.toString()), obtenerValor(r.getTipoValor(), r.getValor()));
                }
            } else {
                values.put(p.getEtiqueta(), "NO");
                Integer rr = p.getReiterancia();
                for (int i = 0; i < rr; i++) {
                    Integer indice = i + 1;
                    values.put(p.getEtiqueta().concat("r").concat(indice.toString()), "NA");
                }
            }
        }
    }

    public String obtenerValor(String moneda, String valor) {
        if (moneda == null) {
            return " ";
        }
        if (valor == null) {
            valor = "0.00";
        }
        switch (moneda) {
            case Constantes.MONEDA_SOLES:
                return "S/. ".concat(valor);
            case Constantes.MONEDA_DOLARES:
                return "$ ".concat(valor);
            case Constantes.MONEDA_PORCENTAJE:
                return valor.concat(" %");
            default:
                return moneda.concat(" ").concat(valor);
        }
    }

    public HcPenalidadPorDocumentoLegal buscarPenalidad(HcPenalidad p, List<HcPenalidadPorDocumentoLegal> lp) {
        for (HcPenalidadPorDocumentoLegal pp : lp) {
            if (p.getId().intValue() == pp.getPenalidad().getId().intValue()) {
                return pp;
            }
        }
        return null;
    }

    private static void generate(InputStream inputFile, String fileOutput, Map<String, String> values) {
        POIFSFileSystem fs = null;
        try {
            fs = new POIFSFileSystem(inputFile);
            HWPFDocument doc = new HWPFDocument(fs);
            for (Map.Entry<String, String> e : values.entrySet()) {
                String token = "${" + e.getKey() + "}";
                String value = e.getValue();
                System.out.println("value: "+value);  
                replaceText(doc, token, value);
            }
            saveWord(fileOutput, doc);
            doc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateDOCX(InputStream inputFile, String fileOutput, Map<String, String> values) {
        XWPFDocument doc = null;
        try {
            doc = new XWPFDocument(OPCPackage.open(inputFile));

            //Cabeceras
            List<XWPFHeader> cabeceras = doc.getHeaderList();
            if (cabeceras != null && !cabeceras.isEmpty()) {
                for (XWPFHeader cabecera : cabeceras) {
                    procesarParrafos(cabecera.getParagraphs(), values, false);
                    procesarTablas(cabecera.getTables(), values);
                }
            }

            //Cuerpo
            procesarParrafos(doc.getParagraphs(), values, false);
            procesarTablas(doc.getTables(), values);

            //Pies
            List<XWPFFooter> pies = doc.getFooterList();
            if (pies != null && !pies.isEmpty()) {
                for (XWPFFooter pie : pies) {
                    procesarParrafos(pie.getParagraphs(), values, false);
                    procesarTablas(pie.getTables(), values);
                }
            }

            doc.write(new FileOutputStream(fileOutput));
            doc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e1) {
            e1.printStackTrace();
        }
    }

    private static void procesarTablas(List<XWPFTable> tablas, Map<String, String> values) {
        for (XWPFTable table : tablas) {
            List<XWPFTableRow> rows = table.getRows();
            for (int i = 0; i < rows.size(); i++) {
                XWPFTableRow row = rows.get(i);
                for (XWPFTableCell cell : row.getTableCells()) {
                    procesarParrafos(cell.getParagraphs(), values, true);
                }
            }
        }
    }

    private static void procesarParrafos(List<XWPFParagraph> parrafos, Map<String, String> values, boolean esCelda) {
        for (XWPFParagraph p : parrafos) {
            boolean printLine = false;
            int crun = 0;
            while (crun < p.getRuns().size()) {
                XWPFRun r = p.getRuns().get(crun);
                String text = r.getText(0);
                if (!AppUtil.checkNullOrEmpty(text) || printLine) {
                    for (Map.Entry<String, String> e : values.entrySet()) {
                        //String token = "${" + e.getKey() + "}";
                        String token = "ETQ" + e.getKey();
                        String value = e.getValue();
                        if (printLine || text.equals(token)) {
                            if (!AppUtil.checkNullOrEmpty(value)) {
                                if (value.contains("\r")) {
                                    String[] lineas = value.split("\r");
                                    printLine = true;

                                    for (int i = 0; i < lineas.length; i++) {
                                        // For every run except last one, add a carriage return.
                                        String linea = lineas[i];

                                        if (i == 0) {
                                            r.setText(text.replace(token, linea), 0);
                                            if (esCelda) {
                                                r.addBreak();
                                            } else {
                                                r.addCarriageReturn();
                                            }
                                            p.insertNewRun(crun + 1);
                                        } else {
                                            XWPFRun newRun = p.getRuns().get(crun);
                                            CTRPr rPr = newRun.getCTR().isSetRPr() ? newRun.getCTR().getRPr() : newRun.getCTR().addNewRPr();
                                            rPr.set(r.getCTR().getRPr());
                                            newRun.setText(linea);
                                            if (i < lineas.length - 1) {
                                                if (esCelda) {
                                                    newRun.addBreak();
                                                } else {
                                                    newRun.addCarriageReturn();
                                                }
                                                p.insertNewRun(crun + 1);
                                            }
                                        }
                                        if (i == lineas.length - 1) {
                                            printLine = false;
                                        }
                                    }
                                } else {
                                    r.setText(text.replace(token, value), 0);
                                }
                            } else {
                                r.setText(text.replace(token, ""), 0);
                            }
                        }
                    }

                }
                crun++;
            }
        }
    }

    private static HWPFDocument replaceText(HWPFDocument doc, String findText, String replaceText) {
        Range r1 = doc.getRange();

//        System.out.println("Range: " + r1);
//        System.out.println("Number of sections: " + r1.numSections());
        if (r1 == null) {
            throw new IllegalStateException("The range is null. Cannot proceed with replacing text.");
        }
        for (int i = 0; i < r1.numSections(); ++i) {
            Section s = r1.getSection(i);
            //System.out.println("Section " + i + ": " + s);
            for (int x = 0; x < s.numParagraphs(); x++) {
                Paragraph p = s.getParagraph(x);
                //System.out.println("Paragraph " + x + ": " + p);
                for (int z = 0; z < p.numCharacterRuns(); z++) {
                	CharacterRun run = p.getCharacterRun(z);
                    if (run != null) {
                        String text = run.text();
                        if (text != null && text.contains(findText) && replaceText != null) {
                        	System.out.println("findText: "+findText);  
                        	System.out.println("replaceText: "+replaceText);  
                            run.replaceText(findText, replaceText);
                        }
                    }
                }
            }
        }

        return doc;
    }

    private static void saveWord(String filePath, HWPFDocument doc) throws IOException {
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            doc.write(out);
        }
    }

    @Override
    public List<HcTipoContrato> findLstHcTipoContratoCodigoAdendaContractual() {
        return hcTipoContratoRepository.findLstByAdendaContractual(Constantes.TIPO_DOCUMENTO_CONTRATO_ADENDA, Constantes.TIPO_DOCUMENTO_ADENDA_CONTRACTUAL);
    }

}

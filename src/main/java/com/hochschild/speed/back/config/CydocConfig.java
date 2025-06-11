package com.hochschild.speed.back.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:cydoc.properties")
public @Data class CydocConfig {

    //#####################################################################################################################################
    //#																																     #
    //#													ADJUNTOS          														         #
    //#																																	 #
    //#####################################################################################################################################

    @Value("${std.home}")
    private String home;

    @Value("${carpeta.archivosSubidos}")
    private String carpetaArchivosSubidos;

    @Value("${carpeta.notificaciones}")
    private String carpetaNotificaciones;

    @Value("${carpeta.plantillas.excel}")
    private String carpetaPlantillasExcel;

    @Value("${carpeta.generados}")
    private String carpetaGenerados;

    @Value("${std.titulo}")
    private String titulo;

    @Value("${digitalizador.habilitado}")
    private Boolean digitalizadorHabilitado;

    @Value("${controlCalidad.habilitado}")
    private Boolean controlCalidadHabilitado;

    @Value("${porcentaje.mostrar.alerta}")
    private Integer porcentajeMostrarAlerta;

    @Value("${dias.alerta.roja}")
    private Integer diasAlertaRoja;

    @Value("${dias.alerta.amarilla}")
    private Integer diasAlertaAmarilla;

    @Value("${expediente.documento.obligatorio}")
    private Boolean expedienteDocumentoObligatorio;

    @Value("${formato.fecha}")
    private String formatoFecha;

    //#####################################################################################################################################
    //#																																	 #
    //#											Configuracion de Archivos          														 #
    //#																																	 #
    //#####################################################################################################################################
    @Value("${documento.tamanio.maximo.bytes}")
    private Integer tamanioMaximoBytes;

    //#####################################################################################################################################
    //#																																	 #
    //#											Configuracion de Mensajeria        														 #
    //#																																	 #
    //#####################################################################################################################################
    @Value("${mensajeria.adjuntar.cargo}")
    private Boolean mensajeriaAdjuntarCargo;

    @Value("${mensajeria.tipo.documento}")
    private Integer mensajeriaTipoDocumento;

    //#####################################################################################################################################
    //#																																	 #
    //#											Configuracion de Dias utiles       														 #
    //#																																	 #
    //#####################################################################################################################################
    @Value("${laborable.sabado}")
    private Boolean laborableSabado;

    @Value("${laborable.domingo}")
    private Boolean laborableDomingo;
    //#####################################################################################################################################
    //#																																	#
    //#											Integracion SCA      														    		#
    //#																																	#
    //#####################################################################################################################################
    @Value("${sca.activo}")
    private Boolean scaActivo;

    @Value("${sca.id.aplicacion}")
    private Integer scaIdAplicacion;

    //#####################################################################################################################################
    //#																																	#
    //#													Configuracion de Web Service Sunat/Reniec										#
    //#																																	#
    //#####################################################################################################################################
    @Value("${consultaLocator.servidor}")
    private String consultaLocatorServidor;
}
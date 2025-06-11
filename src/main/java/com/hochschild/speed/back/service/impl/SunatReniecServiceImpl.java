package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.Cliente;
import com.hochschild.speed.back.service.SunatReniecService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service("SunatReniecWebService")
public class SunatReniecServiceImpl implements SunatReniecService {

    @Override
    public Cliente consultaSunatReniec(Integer tipoDocumento, String nroDocumento) {

        Cliente cliente = new Cliente();
        String nombres = "", apellidoPaterno = "", apellidoMaterno = "", razonSocial = "", direccion = "",
                situacion = "", mensaje = "";
        //ConsultaWS_ServiceLocator consultaLocator = new ConsultaWS_ServiceLocator(Config.getPropiedad("consultaLocator.servidor"));
        try {
            //ConsultaWS_PortType consulta = consultaLocator.getConsultaWSPort();
            if (tipoDocumento == 1) {
                //ClienteResponseWS consultaReniec = consulta.consultaReniec(nroDocumento);
                //nombres = consultaReniec.getNombres();
                //apellidoPaterno = consultaReniec.getApellidoPaterno();
                //apellidoMaterno = consultaReniec.getApellidoMaterno();
                //mensaje = consultaReniec.getMensaje();
            } else if (tipoDocumento == 6) {
                //ClienteResponseWS consultaSunat = consulta.consultaSunat(nroDocumento);
                //mensaje = consultaSunat.getMensaje();
                //razonSocial = consultaSunat.getRazonSocial();
                //direccion = consultaSunat.getDireccion();
                //situacion = consultaSunat.getSituacion();
            }
        } catch (ServiceException ex) {
            Logger.getLogger(SunatReniecServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        cliente.setMensajeSR(mensaje);
        cliente.setNombre(nombres);
        cliente.setApellidoPaterno(apellidoPaterno);
        cliente.setApellidoMaterno(apellidoMaterno);
        cliente.setRazonSocial(razonSocial);
        cliente.setSituacionSunat(situacion);
        cliente.setDireccionSunat(direccion);

        if (tipoDocumento == 1 && cliente.getMensajeSR().equals("Ingrese el c&oacute;digo que aparece en la imagen")) {
            System.out.println(">>>>>>>>>>>>>>>>>>>reenviando reniec (codigo no coincide)");
            return consultaSunatReniec(tipoDocumento, nroDocumento);
        } else {
            return cliente;
        }
    }
}

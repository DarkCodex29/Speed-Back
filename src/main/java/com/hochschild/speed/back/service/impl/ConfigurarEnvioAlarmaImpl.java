package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.repository.speed.ParametroRepository;
import com.hochschild.speed.back.service.EnvioAlarmaService;
import com.hochschild.speed.back.util.Constantes;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ConfigurarEnvioAlarmaImpl {

    private static final Logger log = LoggerFactory.getLogger(ConfigurarEnvioAlarmaImpl.class);
    @Autowired
    private EnvioAlarmaService envioAlarmaService;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ParametroRepository parametroRepository;
    @PostConstruct
    public void init(){
        String horaEnvio = parametroRepository.obtenerPorTipo(Constantes.PARAMETRO_HORA_ENVIO_ALARMA).get(0).getValor();

        String[] partes=horaEnvio.split(":");
        int hora=Integer.parseInt(partes[0]);
        int minuto=0;
        if(partes.length > 1){
            minuto=Integer.parseInt(partes[1]);
        }

        MethodInvokingJobDetailFactoryBean factoryResumenes=new MethodInvokingJobDetailFactoryBean();
        factoryResumenes.setName("Envio Alarmas");
        factoryResumenes.setTargetObject(envioAlarmaService);
        factoryResumenes.setTargetMethod("envioAlarmas");
        factoryResumenes.setConcurrent(false);
        try{
            factoryResumenes.afterPropertiesSet();
            Trigger triggerResumenes= TriggerBuilder.newTrigger().withIdentity("procesarResumenes","procesos").withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(hora,minuto)).build();
            scheduler.scheduleJob(factoryResumenes.getObject(),triggerResumenes);
            log.info("======================PRIMERA VEZ"+scheduler.getTrigger(triggerResumenes.getKey()).getNextFireTime().toString());
        }
        catch(ClassNotFoundException | NoSuchMethodException | SchedulerException e){
            log.error("No se pudo inicializar en proceso de envio de alarmas",e);
        }
    }

}

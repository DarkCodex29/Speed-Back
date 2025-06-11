package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.service.IntalioService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service("IntalioService")
public class IntalioServiceImpl implements IntalioService {
    private static final Logger LOGGER = Logger.getLogger(IntalioServiceImpl.class.getName());

    @Override
    public String getAjaxFormURL(Usuario usuario, Integer idExpediente) {

//        TaskManagementServices taskService;
//        try {
//            taskService = new TaskManagementServices();
//            String token = iniciarSesion(usuario);
//            if (token != null) {
//                TaskManagementServicesPortType taskManagementServicesPortType = taskService.getTaskManagementServicesSOAP();
//                List<TaskMetadata> tareas = getTareas(taskManagementServicesPortType, token, "ACTIVITY");
//                Calendar fechaUltimaTarea = null;
//                TaskMetadata ultimaTarea = null;
//                if (tareas != null) {
//                    String url = null;
//                    LOGGER.info("idExpediente: " + idExpediente);
//                    for (TaskMetadata tarea : tareas) {
//                        String taskState = tarea.getTaskState();
//                        if (taskState != null) {
//                            LOGGER.info("taskState: " + taskState);
//                            if ((taskState.equals("READY") || taskState.equals("CLAIMED")) && tarea.getTaskType().equals("ACTIVITY")) {
//                                String idTarea = tarea.getTaskId();
//                                LOGGER.info("idTarea: " + idTarea);
//                                GetTaskRequest peticion = new GetTaskRequest();
//                                peticion.setParticipantToken(token);
//                                peticion.setTaskId(idTarea);
//                                GetTaskResponse respuesta = null;
//                                respuesta = taskManagementServicesPortType.getTask(peticion);
//                                Task encontrada = respuesta.getTask();
//                                if (encontrada != null) {
//                                    TaskData input = encontrada.getInput();
//                                    if (input != null) {
//                                        List<Object> elementos = input.getAny();
//                                        for (Object elemento : elementos) {
//                                            ElementNSImpl formModel = (ElementNSImpl) elemento;
//                                            String nombreForm = formModel.getNodeName();
//                                            if (nombreForm.equals("FormModel")) {
//                                                NodeList nodeList = formModel.getChildNodes();
//                                                for (int i = 0; i < nodeList.getLength(); i++) {
//                                                    Node node = nodeList.item(i);
//                                                    if (node.getNodeName().equals("idExpediente")) {
//                                                        Node texto = node.getFirstChild();
//                                                        String id = "";
//                                                        LOGGER.info("idExpediente tarea: " + texto.getNodeValue());
//                                                        if (texto != null) {
//                                                            // LOGGER.info("texto.getNodeType() :" + texto.getNodeType());
//                                                            // LOGGER.info("Node.TEXT_NODE :" + Node.TEXT_NODE);
//                                                            if (texto.getNodeType() == Node.TEXT_NODE) {
//                                                                id = texto.getNodeValue();
//                                                            }
//                                                            // LOGGER.info("Valor del id nodo: " + id);
//                                                            // LOGGER.info("Valor del id Expediente: " + idExpediente);
//                                                            if (id.equals(new String("" + idExpediente))) {
//                                                                if (fechaUltimaTarea == null) {
//                                                                    fechaUltimaTarea = Calendar.getInstance();
//                                                                    fechaUltimaTarea.setTime(tarea.getCreationDate().toGregorianCalendar().getTime());
//
//                                                                    ultimaTarea = new TaskMetadata();
//                                                                    ultimaTarea.setFormUrl(tarea.getFormUrl());
//                                                                    ultimaTarea.setTaskId(tarea.getTaskId());
//                                                                } else {
//                                                                    Calendar fechaTarea = Calendar.getInstance();
//                                                                    fechaTarea.setTime(tarea.getCreationDate().toGregorianCalendar().getTime());
//
//                                                                    if (fechaTarea.after(fechaUltimaTarea)) {
//                                                                        fechaUltimaTarea.setTime(fechaTarea.getTime());
//
//                                                                        ultimaTarea = new TaskMetadata();
//                                                                        ultimaTarea.setFormUrl(tarea.getFormUrl());
//                                                                        ultimaTarea.setTaskId(tarea.getTaskId());
//                                                                    }
//                                                                }
//                                                                /*url="http://" + Config.getPropiedad("intalio.servidor") + ":" + Config.getPropiedad("intalio.puerto");
//																url+=URLDecoder.decode(tarea.getFormUrl().toString(),"UTF-8");
//																url+="?id=" + tarea.getTaskId();
//																url+="&url=" + tarea.getFormUrl().toString();
//																url+="&token=" + token;
//																url+="&user=" + usuario.getUsuario();
//																return url;*/
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            }
//
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if (ultimaTarea != null) {
//                        url = "http://" + Config.getPropiedad("intalio.servidor") + ":" + Config.getPropiedad("intalio.puerto");
//                        url += URLDecoder.decode(ultimaTarea.getFormUrl().toString(), "UTF-8");
//                        url += "?id=" + ultimaTarea.getTaskId();
//                        url += "&url=" + ultimaTarea.getFormUrl().toString();
//                        url += "&token=" + token;
//                        url += "&user=" + usuario.getUsuario();
//                    }
//                    if (url == null) {
//                        LOGGER.info("No se encontro una URL para el el expediente");
//                    }
//                    return url;
//                }
//                LOGGER.info("No se encontraron tareas para el usuario " + usuario.getUsuario());
//                return null;
//            }
//            LOGGER.info("El usuario " + usuario.getUsuario() + " no pudo iniciar sesion en Intalio");
//        } catch (InvalidInputMessageFault_Exception e1) {
//            LOGGER.info("Parametros ingresados no validos", e1);
//            return null;
//        } catch (InvalidParticipantTokenFault_Exception e1) {
//            LOGGER.info("Error al iniciar sesion en intalio", e1);
//            return null;
//        } catch (UnavailableTaskFault_Exception e1) {
//            LOGGER.info("La tarea buscada no ha sido encontrada", e1);
//            return null;
//        } catch (UnsupportedEncodingException e) {
//            LOGGER.info("No se pudo generar la url", e);
//            return null;
//        }
//        return null;
        return null;
    }
}

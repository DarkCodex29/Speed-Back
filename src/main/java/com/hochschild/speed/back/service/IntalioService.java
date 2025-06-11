package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.Usuario;

import java.util.List;

public interface IntalioService {
    String getAjaxFormURL(Usuario usuario, Integer idExpediente);
}

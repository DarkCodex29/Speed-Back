package com.hochschild.speed.back.model.bean;

import com.hochschild.speed.back.model.domain.speed.Cliente;
import lombok.Data;

public @Data class VerificacionClienteBean {
    private boolean existe;
    private Cliente cliente;
    private String message;
}

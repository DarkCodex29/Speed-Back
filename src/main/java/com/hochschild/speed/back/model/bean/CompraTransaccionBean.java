package com.hochschild.speed.back.model.bean;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompraTransaccionBean {

    private Integer id;

    @NotNull(message = "La descripcion es obligatoria")
    private String descripcion;

    @NotNull(message = "La cantidad es obligatoria")
    private Integer cantidad;
    private BigDecimal costo;
    private String estado;
}

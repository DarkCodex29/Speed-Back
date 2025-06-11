package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CLIENTE_LOG")
@Data
public class ClienteLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clienteLog")
    private Integer id;
    @Column(name = "id_cliente")
    private Integer idCliente;
    private String direccion;
    private String contacto;
    private String telefono;
    private String correo;
    @Column(name = "usuario_creacion")
    private Integer usuarioCreacion;
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
}


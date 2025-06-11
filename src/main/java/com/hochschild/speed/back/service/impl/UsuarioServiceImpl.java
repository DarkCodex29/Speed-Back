package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.model.domain.speed.Usuario;
import com.hochschild.speed.back.repository.speed.UsuarioRepository;
import com.hochschild.speed.back.service.UsuarioService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UsuarioService")
public class UsuarioServiceImpl implements UsuarioService {
    private static final Logger LOGGER = Logger.getLogger(UsuarioServiceImpl.class.getName());
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario dialogSeleccionarUsuarios(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    @Override
    public Usuario findUsuarioById(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }
}
package com.hochschild.speed.back.service.impl;

import com.hochschild.speed.back.repository.speed.HcAdendaRepository;
import com.hochschild.speed.back.service.HcAdendaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HcAdendaServiceImpl implements HcAdendaService {

    private static final Logger LOGGER = Logger.getLogger(HcAdendaServiceImpl.class.getName());
    private final HcAdendaRepository hcAdendaRepository;

    @Autowired
    public HcAdendaServiceImpl(HcAdendaRepository hcAdendaRepository) {
        this.hcAdendaRepository = hcAdendaRepository;
    }
}
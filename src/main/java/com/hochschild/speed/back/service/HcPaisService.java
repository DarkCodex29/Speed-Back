package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.HcPais;
import com.hochschild.speed.back.model.filter.HcPaisFilter;

import java.util.List;

public interface HcPaisService {
    List<HcPais> find(HcPaisFilter filter);
}

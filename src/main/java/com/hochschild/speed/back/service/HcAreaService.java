package com.hochschild.speed.back.service;

import com.hochschild.speed.back.model.domain.speed.HcArea;
import com.hochschild.speed.back.model.filter.HcAreaFilter;

import java.util.List;

public interface HcAreaService {
    List<HcArea> find(HcAreaFilter filter);
}

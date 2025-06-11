package com.hochschild.speed.back.service;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;

public interface ExportarBandejaService {

    Workbook exportarBandejaEntrada(ArrayList<Integer> trazas);

    Workbook exportarBandejaExpedientes(ArrayList<Integer> expedientes);
}
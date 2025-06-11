package com.hochschild.speed.back.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValoresUtil {

	public static Map<String, String[]> obtenerVO(List<String> listaVO){
		Map<String, String[]> mapa = new HashMap<>();
		
		if(listaVO != null && !listaVO.isEmpty()){
			String[] paises = new String[listaVO.size()];
			String[] companias = new String[listaVO.size()];
			String[] ubicaciones = new String[listaVO.size()];
			String[] areas = new String[listaVO.size()];
			
			int i = 0;
			for(String filaVO : listaVO){
				String[] vo = filaVO.split(",");
				
				paises[i] = new String(vo[0]);
				companias[i] = new String(vo[1]);
				ubicaciones[i] = new String(vo[2]);
				areas[i] = new String(vo[3]);
				
				i++;
			}
			
			mapa.put(Constantes.KEY_VO_PAISES, paises);
			mapa.put(Constantes.KEY_VO_COMPANIAS, companias);
			mapa.put(Constantes.KEY_VO_UBICACIONES, ubicaciones);
			mapa.put(Constantes.KEY_VO_AREAS, areas);
		}
		return mapa;
	}
	
	public static Map<String, String[]> obtenerVOCompanias(List<String> listaVO,String pais){
		Map<String, String[]> mapa = new HashMap<String, String[]>();
		
		if(listaVO != null && !listaVO.isEmpty()){
			String[] paises = new String[listaVO.size()];
			String[] companias = new String[listaVO.size()];
			String[] ubicaciones = new String[listaVO.size()];
			String[] areas = new String[listaVO.size()];
			
			int i = 0;
			for(String filaVO : listaVO){
				String[] vo = filaVO.split(",");
				
				if(vo[0].equals("*") || vo[0].equals(pais)){
					paises[i] = new String(vo[0]);
					companias[i] = new String(vo[1]);
					ubicaciones[i] = new String(vo[2]);
					areas[i] = new String(vo[3]);
					i++;
				}
				
			}
			
			mapa.put(Constantes.KEY_VO_PAISES, paises);
			mapa.put(Constantes.KEY_VO_COMPANIAS, companias);
			mapa.put(Constantes.KEY_VO_UBICACIONES, ubicaciones);
			mapa.put(Constantes.KEY_VO_AREAS, areas);
		}
		return mapa;
	}
	
	public static Map<String, String[]> obtenerVOUbicacion(List<String> listaVO,String compania){
		Map<String, String[]> mapa = new HashMap<String, String[]>();
		
		if(listaVO != null && !listaVO.isEmpty()){
			String[] paises = new String[listaVO.size()];
			String[] companias = new String[listaVO.size()];
			String[] ubicaciones = new String[listaVO.size()];
			String[] areas = new String[listaVO.size()];
			
			int i = 0;
			for(String filaVO : listaVO){
				String[] vo = filaVO.split(",");
				
				if(vo[1].equals("*") || vo[1].equals(compania)){
					paises[i] = new String(vo[0]);
					companias[i] = new String(vo[1]);
					ubicaciones[i] = new String(vo[2]);
					areas[i] = new String(vo[3]);
					i++;
				}
			}
			
			mapa.put(Constantes.KEY_VO_PAISES, paises);
			mapa.put(Constantes.KEY_VO_COMPANIAS, companias);
			mapa.put(Constantes.KEY_VO_UBICACIONES, ubicaciones);
			mapa.put(Constantes.KEY_VO_AREAS, areas);
		}
		return mapa;
	}
	
	public static Map<String, String[]> obtenerVOArea(List<String> listaVO,String compania){
		Map<String, String[]> mapa = new HashMap<String, String[]>();
		
		if(listaVO != null && !listaVO.isEmpty()){
			String[] paises = new String[listaVO.size()];
			String[] companias = new String[listaVO.size()];
			String[] ubicaciones = new String[listaVO.size()];
			String[] areas = new String[listaVO.size()];
			
			int i = 0;
			for(String filaVO : listaVO){
				String[] vo = filaVO.split(",");
				
				if(vo[1].equals("*") || vo[1].equals(compania)){
					paises[i] = new String(vo[0]);
					companias[i] = new String(vo[1]);
					ubicaciones[i] = new String(vo[2]);
					areas[i] = new String(vo[3]);
					i++;
				}
				
				
			}
			
			mapa.put(Constantes.KEY_VO_PAISES, paises);
			mapa.put(Constantes.KEY_VO_COMPANIAS, companias);
			mapa.put(Constantes.KEY_VO_UBICACIONES, ubicaciones);
			mapa.put(Constantes.KEY_VO_AREAS, areas);
		}
		return mapa;
	}
}

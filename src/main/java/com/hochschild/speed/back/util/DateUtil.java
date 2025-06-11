package com.hochschild.speed.back.util;

import com.hochschild.speed.back.model.domain.speed.Feriado;
import com.hochschild.speed.back.model.domain.speed.Sede;
import com.hochschild.speed.back.repository.speed.FeriadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class DateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    public static final String FORMAT_DATE = "dd/MM/yyyy";
    public static final String FORMAT_HOUR = "HH:mm";
    public static final String FORMAT_DATE_HOUR = "dd/MM/yyyy HH:mm";
    public static final String FORMAT_DATE_HYPHEN = "dd-MM-yyyy";
    public static final String FORMAT_DATE_HOUR_HYPHEN = "dd-MM-yyyy HH:mm";
    public static final String FORMAT_DATE_HOUR_MINUTE = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_DATE_ISO_8601 = "yyyy-MM-dd'T'HH:mm";
    public static final String FORMAT_DATE_XML = "yyyy-MM-dd";
    public static final String FORMAT_DATE_BD = "yyyy-MM-dd";
    public static final String FORMAT_DATE_RFC = "dd.MM.yyyy";

    public static Date convertStringToDate(String strDate, String format) {

        Date date = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            date = formatter.parse(strDate);
        } catch (Exception e) {
        }
        return date;
    }

    public static String convertDateToString(Date date, String format) {
        String strDate = "";
        try {
            if(date!=null){
                SimpleDateFormat formatter = new SimpleDateFormat(format);
                strDate = formatter.format(date);
            }
        } catch (Exception e) {
            strDate = "";
        }
        return strDate;
    }

    public static String changeStringDateFormat(String strDateInput, String formatInput, String formatOutput) {
        Date tempDate = convertStringToDate(strDateInput, formatInput);
        return convertDateToString(tempDate, formatOutput);
    }

    public static String getCurrentYearAsString() {
        return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }

    public static String getYearAsString(Date now) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        return formatter.format(now);
    }

    public static String getCurrentYearMonthAsString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        return formatter.format(new Date());
    }

    public static String getYearMonthAsString(Date now) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        return formatter.format(now);
    }
    public static Date maximizarFecha(Date fecha) {
        if (fecha != null) {
            Calendar date = Calendar.getInstance();
            date.setTime(fecha);
            date.set(Calendar.HOUR, date.getMaximum(Calendar.HOUR_OF_DAY));
            date.set(Calendar.MINUTE, date.getMaximum(Calendar.MINUTE));
            date.set(Calendar.SECOND, date.getMaximum(Calendar.SECOND));
            date.set(Calendar.MILLISECOND, date.getMaximum(Calendar.MILLISECOND));
            return date.getTime();
        }
        return fecha;
    }

    public static Date minimizarFecha(Date fecha) {
        if (fecha != null) {
            Calendar date = Calendar.getInstance();
            date.setTime(fecha);
            date.set(Calendar.HOUR, date.getMinimum(Calendar.HOUR_OF_DAY));
            date.set(Calendar.MINUTE, date.getMinimum(Calendar.MINUTE));
            date.set(Calendar.SECOND, date.getMinimum(Calendar.SECOND));
            date.set(Calendar.MILLISECOND, date.getMinimum(Calendar.MILLISECOND));
            return date.getTime();
        }
        return fecha;
    }


    public static int diferenciasDeFechas(Date fechaInicial, Date fechaFinal) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String fechaInicioString = df.format(fechaInicial);
        try {
            fechaInicial = df.parse(fechaInicioString);
        } catch (ParseException ex) {
            LOGGER.error(ex.getMessage(),ex);
        }
        String fechaFinalString = df.format(fechaFinal);
        try {
            fechaFinal = df.parse(fechaFinalString);
        } catch (ParseException ex) {
            LOGGER.error(ex.getMessage(),ex);
        }
        long fechaInicialMs = fechaInicial.getTime();
        long fechaFinalMs = fechaFinal.getTime();
        long diferencia = fechaFinalMs - fechaInicialMs;
        double dias = Math.floor((double) diferencia / (1000 * 60 * 60 * 24));
        return ((int) dias);
    }

    public static String getNombreMes(int mes) {
        String nombreMes;
        switch (mes) {
            case 1:
                nombreMes = "Enero";
                break;
            case 2:
                nombreMes = "Febrero";
                break;
            case 3:
                nombreMes = "Marzo";
                break;
            case 4:
                nombreMes = "Abril";
                break;
            case 5:
                nombreMes = "Mayo";
                break;
            case 6:
                nombreMes = "Junio";
                break;
            case 7:
                nombreMes = "Julio";
                break;
            case 8:
                nombreMes = "Agosto";
                break;
            case 9:
                nombreMes = "Setiembre";
                break;
            case 10:
                nombreMes = "Octubre";
                break;
            case 11:
                nombreMes = "Noviembre";
                break;
            case 12:
                nombreMes = "Diciembre";
                break;
            default:
                nombreMes = "Enero";
                break;
        }
        return nombreMes;
    }

    public static Date sumarFechasDias(Date fecha, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.DATE, dias);
        return cal.getTime();
    }

    public static long diferenciaFechasHoras(Date fechaInicio, Date fechaFin) {
        long milis1 = fechaInicio.getTime();
        long milis2 = fechaFin.getTime();
        long diff = milis2 - milis1;
        return diff / (60 * 60 * 1000);
    }

    public static Date sumarDiasUtiles(Date fecha, int dias, Sede sede, FeriadoRepository feriadoRepository) {

        ResourceBundle rb = ResourceBundle.getBundle("cydoc");

        boolean laborableSabado = Boolean.parseBoolean(rb.getString("laborable.sabado"));
        boolean laborableDomingo = Boolean.parseBoolean(rb.getString("laborable.domingo"));

        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        for (int i = 0; i < dias; i++) {
            c.add(Calendar.DAY_OF_YEAR, 1);
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && !laborableSabado) {
                dias++;
            } else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && !laborableDomingo) {
                dias++;
            } else {
                Feriado feriado = feriadoRepository.obtenerPorFechaSede(c.getTime(), sede.getId());
                if (feriado != null) {
                    dias++;
                }
            }
        }
        return c.getTime();
    }

    public static Date sumarDiasUtilesSinFeriados(Date fecha, int dias) {

        ResourceBundle rb = ResourceBundle.getBundle("cydoc");

        boolean laborableSabado = Boolean.parseBoolean(rb.getString("laborable.sabado"));
        boolean laborableDomingo = Boolean.parseBoolean(rb.getString("laborable.domingo"));

        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        for (int i = 0; i < dias; i++) {
            c.add(Calendar.DAY_OF_YEAR, 1);
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && !laborableSabado) {
                dias++;
            } else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && !laborableDomingo) {
                dias++;
            }
        }
        return c.getTime();
    }
}
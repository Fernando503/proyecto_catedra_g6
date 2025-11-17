/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import org.jdatepicker.impl.JDatePickerImpl;

/**
 *
 * @author Fernando Flamenco
 */
public class DatePickerUtils {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter dtft = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String sqlDateToString(java.sql.Date sqlDate) {
        if (sqlDate == null) return "";
        return sdf.format(sqlDate);
    }

    public static void setDateFromString(JDatePickerImpl datePicker, String fechaTexto) {
        try {
            Date date = sdf.parse(fechaTexto);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            datePicker.getModel().setDate(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            );
            datePicker.getModel().setSelected(true);
        } catch (ParseException e) {
            System.err.println("Error al parsear la fecha: " + fechaTexto);
        }
    }
    
    public static String localDateToString(LocalDate localDate) {
        if (localDate == null) return "";
        return localDate.format(dtf);
    }
    
     public static String localDateTimeToString(LocalDateTime localDatetime) {
        if (localDatetime == null) return "";
        return localDatetime.format(dtft);
    }


}

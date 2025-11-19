/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.tools;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.swing.JFormattedTextField.AbstractFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author Fernando Flamenco
 */
public class DatePicker {
    private final JDatePickerImpl datePicker;

    public DatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    public JDatePickerImpl getComponent() {
        return datePicker;
    }

    public String getFormattedDate() {
        Date date = (Date) datePicker.getModel().getValue();
        if (date != null) {
            return new SimpleDateFormat("dd/MM/yyyy").format(date);
        }
        return "";
    }

    private static class DateLabelFormatter extends AbstractFormatter {
        private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return sdf.parse(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                if (value instanceof Date) {
                    return sdf.format((Date) value);
                } else if (value instanceof Calendar) {
                    return sdf.format(((Calendar) value).getTime());
                }
            }
            return "";
        }

    }
    
    public void setDateFromString(String fecha) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date fechaUtil = sdf.parse(fecha);

            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaUtil);

            datePicker.getModel().setDate(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            );
            datePicker.getModel().setSelected(true);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    public void clearDate() {
        datePicker.getModel().setValue(null);
        datePicker.getModel().setSelected(false);
    }


}

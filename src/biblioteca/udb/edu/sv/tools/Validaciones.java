/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.tools;

import javax.swing.*;
import javax.swing.table.TableModel;

/**
 *
 * @author Fernando Flamenco
 */
public class Validaciones {
    public static String safeGetValue(TableModel model, int row, int col) {
        Object val = model.getValueAt(row, col);
        return val != null ? val.toString() : "";
    }
    
    public static boolean validarCamposRequeridos(JTextField[] camposTexto, JComboBox<?>[] combos) {
        for (JTextField campo : camposTexto) {
            if (campo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe completar todos los campos requeridos");
                campo.requestFocus();
                return false;
            }
        }

        for (JComboBox<?> combo : combos) {
            Object seleccionado = combo.getSelectedItem();
            if (seleccionado == null || seleccionado.toString().equalsIgnoreCase("Seleccione...")) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar una opción válida en el combo");
                combo.requestFocus();
                return false;
            }
        }

        return true;
    }
}

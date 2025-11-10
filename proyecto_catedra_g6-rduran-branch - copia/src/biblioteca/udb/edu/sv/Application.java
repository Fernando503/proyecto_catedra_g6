/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv;

import biblioteca.udb.edu.sv.tools.*;
import biblioteca.udb.edu.sv.vistas.*;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class Application {
     private static final Logger logger = LogManager.getLogger(Application.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        logger.info("Iniciando sistema de Mediateca UDB.");
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            logger.info("Error al instanciar lib Design: " + e.getMessage());
        }

        LoginFrm loginView = new LoginFrm();
        loginView.setVisible(true);
                
        /*DashboardFrm dash = new DashboardFrm();
        dash.configurarAccesosPorRol();
        dash.setVisible(true);*/
    }
}

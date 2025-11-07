/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.tools;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Fernando Flamenco
 */
public class LogManager {
    private static boolean initialized = false;
    static {PropertyConfigurator.configure("src/biblioteca/udb/edu/sv/log4j.properties"); }

    public static Logger getLogger(Class<?> clazz) {
        if (!initialized) {
            initialized = true;
        }
        return Logger.getLogger(clazz);
    }
}

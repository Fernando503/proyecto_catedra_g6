/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.tools;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Fernando Flamenco
 */
public class ConfigLoader {
    private static final Properties config = new Properties();

    static {
        try (InputStream input = new FileInputStream("src/biblioteca/udb/edu/sv/configGlobal.properties")) {
            config.load(input);
        } catch (IOException e) {
            System.err.println("Error al cargar configGlobal.properties: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return config.getProperty(key);
    }

}

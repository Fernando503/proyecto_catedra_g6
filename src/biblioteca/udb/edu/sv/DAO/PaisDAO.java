/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.Pais;
import biblioteca.udb.edu.sv.tools.Conexion;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class PaisDAO {
    private static final Logger logger = LogManager.getLogger(PaisDAO.class);
    
    public List<Pais> listarPaises() {
        List<Pais> lista = new ArrayList<>();

        String sql = "SELECT * FROM pais WHERE habilitado = TRUE";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pais p = new Pais();
                p.setPaisID(rs.getInt("pais_id"));
                p.setNombre(rs.getString("nombre"));
                p.setHabilitado(rs.getBoolean("habilitado"));
                lista.add(p);
            }

        } catch (SQLException e) {
            logger.error("Error al listar paises: " + e.getMessage());
        }
        return lista;
    }
     
    
}

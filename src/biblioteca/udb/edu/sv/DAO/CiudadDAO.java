/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.Ciudad;
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
public class CiudadDAO {
    private static final Logger logger = LogManager.getLogger(CiudadDAO.class);
    
    public List<Ciudad> listarCiudadesPorPais(Integer paisId) {
    List<Ciudad> lista = new ArrayList<>();

    String sql =
        "SELECT c.ciudad_id, c.nombre AS ciudad, p.pais_id, p.nombre AS pais " +
        "FROM ciudad c " +
        "INNER JOIN pais p ON c.pais_id = p.pais_id " +
        "WHERE c.habilitado = TRUE AND p.habilitado = TRUE AND p.pais_id = ?";

    try (Connection conn = Conexion.conectar();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, paisId);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ciudad c = new Ciudad();
                c.setCiudadID(rs.getInt("ciudad_id"));
                c.setNombre(rs.getString("ciudad"));

                Pais p = new Pais();
                p.setPaisID(rs.getInt("pais_id"));
                p.setNombre(rs.getString("pais"));

                c.setPais(p);

                lista.add(c);
            }
        }

    } catch (SQLException e) {
        logger.error("Error al listar ciudades por país: " + e.getMessage());
    }
    return lista;
}
    
}

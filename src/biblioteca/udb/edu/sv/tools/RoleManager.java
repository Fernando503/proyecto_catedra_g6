/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.tools;
import java.util.*;
/**
 *
 * @author Fernando Flamenco
 */
public class RoleManager {
    private static final Map<String, Map<String, Boolean>> permisos = new HashMap<>();

    static {
        // ==============================
        // ADMINISTRADOR
        // ==============================
        Map<String, Boolean> adminPermisos = new HashMap<>();
        adminPermisos.put("GESTION_USUARIOS", true);
        adminPermisos.put("GESTION_DOCUMENTOS", true);
        adminPermisos.put("GESTION_EJEMPLARES", true);
        adminPermisos.put("GESTION_UBICACIONES", true);
        adminPermisos.put("GESTION_PRESTAMOS", true);
        adminPermisos.put("GESTION_DEVOLUCIONES", true);
        adminPermisos.put("GESTION_MORAS", true);
        adminPermisos.put("GESTION_EDITORIALES", true);
        adminPermisos.put("CONFIGURACION_SISTEMA", true);
        adminPermisos.put("REPORTES_GENERALES", true);
        adminPermisos.put("AUDITORIA", true);
        permisos.put("Administrador", adminPermisos);

        // ==============================
        // PROFESOR
        // ==============================
        Map<String, Boolean> profesorPermisos = new HashMap<>();
        profesorPermisos.put("GESTION_USUARIOS", false);
        profesorPermisos.put("GESTION_DOCUMENTOS", true);
        profesorPermisos.put("GESTION_EJEMPLARES", true);
        profesorPermisos.put("GESTION_UBICACIONES", false);
        profesorPermisos.put("GESTION_PRESTAMOS", true);
        profesorPermisos.put("GESTION_DEVOLUCIONES", true);
        profesorPermisos.put("GESTION_MORAS", true);
        profesorPermisos.put("GESTION_EDITORIALES", false);
        profesorPermisos.put("CONFIGURACION_SISTEMA", false);
        profesorPermisos.put("REPORTES_GENERALES", true); // Parcial
        profesorPermisos.put("AUDITORIA", false);
        permisos.put("Profesor", profesorPermisos);

        // ==============================
        // ALUMNO
        // ==============================
        Map<String, Boolean> alumnoPermisos = new HashMap<>();
        alumnoPermisos.put("GESTION_USUARIOS", false);
        alumnoPermisos.put("GESTION_DOCUMENTOS", true);
        alumnoPermisos.put("GESTION_EJEMPLARES", true);
        alumnoPermisos.put("GESTION_UBICACIONES", false);
        alumnoPermisos.put("GESTION_PRESTAMOS", true);
        alumnoPermisos.put("GESTION_DEVOLUCIONES", true);
        alumnoPermisos.put("GESTION_MORAS", true);
        alumnoPermisos.put("GESTION_EDITORIALES", false);
        alumnoPermisos.put("CONFIGURACION_SISTEMA", false);
        alumnoPermisos.put("REPORTES_GENERALES", true);
        alumnoPermisos.put("AUDITORIA", false);
        permisos.put("Alumno", alumnoPermisos);
    }

    /**
     * Verifica si el rol actual tiene acceso al permiso solicitado
     */
    public static boolean tienePermiso(String permiso) {
        String rol = SesionUsuario.getInstancia().getRol();
        Map<String, Boolean> permisosRol = permisos.get(rol);
        if (permisosRol == null) return false;
        return permisosRol.getOrDefault(permiso, false);
    }
}

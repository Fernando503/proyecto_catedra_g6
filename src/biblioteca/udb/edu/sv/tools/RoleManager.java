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

    private static final Map<String, Map<String, Map<String, Boolean>>> permisos = new HashMap<>();

    static {
        // ADMINISTRADOR
        Map<String, Map<String, Boolean>> admin = new HashMap<>();
        admin.put("GESTION_USUARIOS", crearPermisos(true, true, true, true));
        admin.put("GESTION_DOCUMENTOS", crearPermisos(true, true, true, true));
        admin.put("GESTION_EJEMPLARES", crearPermisos(true, true, true, true));
        admin.put("GESTION_UBICACIONES", crearPermisos(true, true, true, true));
        admin.put("GESTION_PRESTAMOS", crearPermisos(true, true, true, true));
        admin.put("GESTION_DEVOLUCIONES", crearPermisos(true, true, true, true));
        admin.put("GESTION_MORAS", crearPermisos(true, true, true, true));
        admin.put("GESTION_EDITORIALES", crearPermisos(true, true, true, true));
        admin.put("CONFIGURACION_SISTEMA", crearPermisos(true, true, true, true));
        admin.put("REPORTES_GENERALES", crearPermisos(true, true, true, true));
        admin.put("AUDITORIA", crearPermisos(true, true, true, true));
        permisos.put("Administrador", admin);

        // PROFESOR
        Map<String, Map<String, Boolean>> profesor = new HashMap<>();
        profesor.put("GESTION_USUARIOS", crearPermisos(false, false, false, false));
        profesor.put("GESTION_DOCUMENTOS", crearPermisos(true, true, false, true));
        profesor.put("GESTION_EJEMPLARES", crearPermisos(true, true, false, true));
        profesor.put("GESTION_UBICACIONES", crearPermisos(false, false, false, false));
        profesor.put("GESTION_PRESTAMOS", crearPermisos(true, true, false, true));
        profesor.put("GESTION_DEVOLUCIONES", crearPermisos(true, true, false, true));
        profesor.put("GESTION_MORAS", crearPermisos(false, false, false, true));
        profesor.put("GESTION_EDITORIALES", crearPermisos(false, false, false, false));
        profesor.put("CONFIGURACION_SISTEMA", crearPermisos(false, false, false, false));
        profesor.put("REPORTES_GENERALES", crearPermisos(false, false, false, true));
        profesor.put("AUDITORIA", crearPermisos(false, false, false, false));
        permisos.put("Profesor", profesor);

        // ALUMNO
        Map<String, Map<String, Boolean>> alumno = new HashMap<>();
        alumno.put("GESTION_USUARIOS", crearPermisos(false, false, false, false));
        alumno.put("GESTION_DOCUMENTOS", crearPermisos(false, false, false, true));
        alumno.put("GESTION_EJEMPLARES", crearPermisos(false, false, false, true));
        alumno.put("GESTION_UBICACIONES", crearPermisos(false, false, false, false));
        alumno.put("GESTION_PRESTAMOS", crearPermisos(true, false, false, true));
        alumno.put("GESTION_DEVOLUCIONES", crearPermisos(true, false, false, true));
        alumno.put("GESTION_MORAS", crearPermisos(false, false, false, true));
        alumno.put("GESTION_EDITORIALES", crearPermisos(false, false, false, false));
        alumno.put("CONFIGURACION_SISTEMA", crearPermisos(false, false, false, false));
        alumno.put("REPORTES_GENERALES", crearPermisos(false, false, false, true));
        alumno.put("AUDITORIA", crearPermisos(false, false, false, false));
        permisos.put("Alumno", alumno);
    }

    private static Map<String, Boolean> crearPermisos(boolean agregar, boolean modificar, boolean eliminar, boolean listar) {
        Map<String, Boolean> acciones = new HashMap<>();
        acciones.put("AGREGAR", agregar);
        acciones.put("MODIFICAR", modificar);
        acciones.put("ELIMINAR", eliminar);
        acciones.put("LISTAR", listar);
        return acciones;
    }

    /**
     * Verifica si el rol actual tiene permiso para una acción específica en un módulo.
     *
     * @param modulo Nombre del módulo (ej. "GESTION_DOCUMENTOS")
     * @param accion Acción a validar (ej. "AGREGAR", "MODIFICAR", "ELIMINAR", "LISTAR")
     * @return true si tiene permiso, false si no
     */
    public static boolean tienePermiso(String modulo, String accion) {
        String rol = SesionUsuario.getInstancia().getRol();
        Map<String, Map<String, Boolean>> permisosRol = permisos.get(rol);
        if (permisosRol == null) return false;

        Map<String, Boolean> accionesModulo = permisosRol.get(modulo);
        if (accionesModulo == null) return false;

        return accionesModulo.getOrDefault(accion.toUpperCase(), false);
    }
}
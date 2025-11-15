/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.vistas;

import biblioteca.udb.edu.sv.controlador.AuditoriaController;
import biblioteca.udb.edu.sv.controlador.ConfiguracionController;
import biblioteca.udb.edu.sv.controlador.EditorialController;
import biblioteca.udb.edu.sv.controlador.EstadoEjemplarController;
import biblioteca.udb.edu.sv.controlador.EstadoPrestamoController;
import biblioteca.udb.edu.sv.controlador.TipoDocumentoController;
import biblioteca.udb.edu.sv.entidades.Auditoria;
import biblioteca.udb.edu.sv.entidades.Ciudad;
import biblioteca.udb.edu.sv.entidades.Configuracion;
import biblioteca.udb.edu.sv.entidades.Editorial;
import biblioteca.udb.edu.sv.entidades.EstadoEjemplar;
import biblioteca.udb.edu.sv.entidades.EstadoPrestamo;
import biblioteca.udb.edu.sv.entidades.Pais;
import biblioteca.udb.edu.sv.entidades.TipoDocumento;
import biblioteca.udb.edu.sv.tools.LogManager;
import biblioteca.udb.edu.sv.tools.Validaciones;
import java.awt.HeadlessException;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;



/**
 *
 * @author Fernando Flamenco
 */
public class ConfiguracionFrm extends javax.swing.JFrame {
    private static final Logger logger = LogManager.getLogger(ConfiguracionFrm.class);
    
    private final DashboardFrm dashboardFrm;
    private final DefaultTableModel modeloConfiguracion;
    private final DefaultTableModel modeloAuditoria;
    private final DefaultTableModel modelTipoDocumento;
    private final DefaultTableModel modelEditoriales;
    private final DefaultTableModel modelEstadoEjemplar;
    private final DefaultTableModel modelEstadoPrestamo;
    private final ConfiguracionController configController = new ConfiguracionController();
    private final AuditoriaController auditController = new AuditoriaController();
    private final TipoDocumentoController tipoDocController = new TipoDocumentoController();
    private final EditorialController editorialController = new EditorialController();
    private final EstadoEjemplarController estadoEjempController = new EstadoEjemplarController();
    private final EstadoPrestamoController estadoPrestController = new EstadoPrestamoController();


    /**
     * Creates new form ConfiguracionFrm
     * @param dashboardFrm
     */
     public ConfiguracionFrm(DashboardFrm dashboardFrm) {
        super("Formulario Configuración");
        this.dashboardFrm = dashboardFrm;
        initComponents();
       
        txt_id_config.setVisible(false);
        txt_id_tip_doc.setVisible(false);
        txt_id_editorial.setVisible(false);
        txt_id_est_ejemplar.setVisible(false);
        txt_id_est_prestamo.setVisible(false);
        
        // INICIALIZACION DE TABLAS
        modeloConfiguracion = (DefaultTableModel) tbl_configuracion.getModel();
        cargarDatosConfig(configController.listar());
        modeloAuditoria = (DefaultTableModel) tbl_auditoria.getModel();
        cargarAuditoria(auditController.listar());
        modelTipoDocumento = (DefaultTableModel) tbl_tipo_documento.getModel();
        cargarTipoDocumento(tipoDocController.listar());
        modelEditoriales = (DefaultTableModel) tbl_editoriales.getModel();
        cargarDatosEditoriales(editorialController.listar());
        modelEstadoEjemplar = (DefaultTableModel) tbl_estado_ejemplar.getModel();
        cargarEstadoEjemplar(estadoEjempController.listar());
        modelEstadoPrestamo = (DefaultTableModel) tbl_estado_prestamo.getModel();
        cargarEstadoPrestamo(estadoPrestController.listar());
        
        //INICIALIZACION DE COMBOS
        List<Pais> paises = new ArrayList<>();
        paises.add(new Pais(0, "Seleccione...", true));
        paises.addAll(editorialController.obtenerPaises());
        cmb_pais_editorial.setModel(new DefaultComboBoxModel<>(paises.toArray(new Pais[0])));
        
        List<Ciudad> ciudades =  new ArrayList<>();
        ciudades.add(new Ciudad(0, "Seleccione...",new Pais(), true));
        cmb_ciudad_editorial.setModel(new DefaultComboBoxModel<>(ciudades.toArray(new Ciudad[0])));

    }
     
    /**
     * El orden que se cargan al modeloConfiguracion es como se mostraran, si se desea se puede cambiar el orden acá
     *
     */
    private void cargarDatosConfig (List<Configuracion> config) {
        modeloConfiguracion.setRowCount(0); // Limpiar tabla
        config.forEach(c -> {
            modeloConfiguracion.addRow(new Object[]{
                c.getIdConfig(),
                c.getParametro(),
                c.getValor(),
                c.getDescripcion(),
                c.getHabilitado()
            });
        });
    }
    
    private void cargarAuditoria (List<Auditoria> audit) {
        modeloAuditoria.setRowCount(0); // Limpiar tabla
        audit.forEach(a -> {
            modeloAuditoria.addRow(new Object[]{
                a.getIdAuditoria(),
                a.getUsuario().getIdUsuario(),
                a.getUsuario().getNombre(),
                a.getFechaAccion(),
                a.getTipoAccion(),
                a.getDetalleAccion()
            });
        });
    }
    
    private void cargarTipoDocumento (List<TipoDocumento> tipDoc){
        modelTipoDocumento.setRowCount(0);// Limpiar tabla
        tipDoc.forEach(td -> {
            modelTipoDocumento.addRow(new Object[]{
                td.getTipoDocumentoID(),
                td.getNombre(),
                td.getDescripcion(),
                td.getHabilitado()
            });
        });
    }
    
    private void cargarEstadoEjemplar (List<EstadoEjemplar> estEje){
        modelEstadoEjemplar.setRowCount(0);// Limpiar tabla
        estEje.forEach(ee -> {
            modelEstadoEjemplar.addRow(new Object[]{
                ee.getEstadoEjemplarID(),
                ee.getNombre(),
                ee.getDescripcion(),
                ee.getHabilitado()
            });
        });
    }
    
    private void cargarEstadoPrestamo (List<EstadoPrestamo> estPres){
        modelEstadoPrestamo.setRowCount(0);// Limpiar tabla
        estPres.forEach(ep -> {
            modelEstadoPrestamo.addRow(new Object[]{
                ep.getEstadoPrestamoID(),
                ep.getNombre(),
                ep.getDescripcion(),
                ep.getHabilitado()
            });
        });
    }
    
    private void cargarDatosEditoriales (List<Editorial> edito){
        modelEditoriales.setRowCount(0);// Limpiar tabla
        edito.forEach(e -> {
            modelEditoriales.addRow(new Object[]{
                e.getEditorialID(),
                e.getNombreEditorial(),
                e.getSitioWeb(),
                e.getPaisEditorial(),
                e.getCiudadEditorial(),
                e.getContactoEmail(),
                e.getContactoPhone(),
                e.getObservaciones(),
                e.getHabilitado()
            });
        });
    }
    
    private void limpiarConfig(){
        txt_id_config.setText("");
        txt_parametro_config.setText("");
        txt_valor_config.setText("");
        txt_descripcion_config.setText("");
        txt_parametro_config.setEditable(true);
    }
    
    private void limpiarTipoDocumento(){
        txt_id_tip_doc.setText("");
        txt_nombre_tip_doc.setText("");
        txt_descripcion_tip_doc.setText("");
    }
    
    private void limpiarEstEjemplar(){
        txt_id_est_ejemplar.setText("");
        txt_nombre_est_ejemplar.setText("");
        txt_descripcion_est_ejemplar.setText("");
    }
    
    private void limpiarEstPrestamo(){
        txt_id_est_prestamo.setText("");
        txt_nombre_est_prestamo.setText("");
        txt_descripcion_est_prestamo.setText("");
    }

    private void limpiarEditorial(){
        txt_id_editorial.setText("");
        txt_nombre_editorial.setText("");
        txt_sitio_web_editorial.setText("");
        txt_observaciones_editorial.setText("");
        txt_correo_editorial.setText("");
        txt_telefono_editorial.setText("");
        if (cmb_pais_editorial.getItemCount() > 0) {
            cmb_pais_editorial.setSelectedIndex(0);
        }
        if (cmb_ciudad_editorial.getItemCount() > 0) {
            List<Ciudad> ciudades =  new ArrayList<>();
            ciudades.add(new Ciudad(0, "Seleccione...",new Pais(), true));
            cmb_ciudad_editorial.setModel(new DefaultComboBoxModel<>(ciudades.toArray(new Ciudad[0])));
            cmb_ciudad_editorial.setSelectedIndex(0);
        }

        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_header_config = new javax.swing.JPanel();
        lbl_title_configuracion = new javax.swing.JLabel();
        pnl_config_volver = new javax.swing.JPanel();
        lbl_btn_volver = new javax.swing.JLabel();
        tabConfig = new javax.swing.JTabbedPane();
        pnl_menu_config = new javax.swing.JPanel();
        pnl_datos_config = new javax.swing.JPanel();
        txt_parametro_config = new javax.swing.JTextField();
        txt_valor_config = new javax.swing.JTextField();
        txt_descripcion_config = new javax.swing.JTextField();
        txt_id_config = new javax.swing.JTextField();
        lbl_parametro_config = new javax.swing.JLabel();
        lbl_valor_config = new javax.swing.JLabel();
        lbl_descripcion_config = new javax.swing.JLabel();
        pnl_acciones_config = new javax.swing.JPanel();
        btn_agregar_config = new javax.swing.JButton();
        btn_editar_config = new javax.swing.JButton();
        btn_eliminar_config = new javax.swing.JButton();
        btn_limpiar_config = new javax.swing.JButton();
        scp_tbl_config = new javax.swing.JScrollPane();
        tbl_configuracion = new javax.swing.JTable();
        pnl_menu_editorial = new javax.swing.JPanel();
        pnl_datos_editorial = new javax.swing.JPanel();
        txt_nombre_editorial = new javax.swing.JTextField();
        txt_sitio_web_editorial = new javax.swing.JTextField();
        txt_id_editorial = new javax.swing.JTextField();
        lbl_nombre_editorial = new javax.swing.JLabel();
        lbl_sitio_web_editorial = new javax.swing.JLabel();
        lbl_observaciones_editorial = new javax.swing.JLabel();
        scp_txt_obser_editorial = new javax.swing.JScrollPane();
        txt_observaciones_editorial = new javax.swing.JTextArea();
        lbl_pais_editorial = new javax.swing.JLabel();
        cmb_pais_editorial = new javax.swing.JComboBox<>();
        lbl_ciudad_editorial = new javax.swing.JLabel();
        cmb_ciudad_editorial = new javax.swing.JComboBox<>();
        lbl_correo_editorial = new javax.swing.JLabel();
        txt_correo_editorial = new javax.swing.JTextField();
        txt_telefono_editorial = new javax.swing.JTextField();
        lbl_telefono_editorial = new javax.swing.JLabel();
        pnl_acciones_editorial = new javax.swing.JPanel();
        btn_agregar_editorial = new javax.swing.JButton();
        btn_editar_editorial = new javax.swing.JButton();
        btn_eliminar_editorial = new javax.swing.JButton();
        btn_limpiar_editorial = new javax.swing.JButton();
        pnl_buscar_editorial = new javax.swing.JPanel();
        txt_buscar_editorial = new javax.swing.JTextField();
        btn_buscar_editorial = new javax.swing.JButton();
        scp_tbl_editoriales = new javax.swing.JScrollPane();
        tbl_editoriales = new javax.swing.JTable();
        pnl_menu_tipo_documentos = new javax.swing.JPanel();
        pnl_datos_tip_doc = new javax.swing.JPanel();
        txt_nombre_tip_doc = new javax.swing.JTextField();
        txt_descripcion_tip_doc = new javax.swing.JTextField();
        txt_id_tip_doc = new javax.swing.JTextField();
        lbl_nombre_tip_doc = new javax.swing.JLabel();
        lbl_descripcion_tip_doc = new javax.swing.JLabel();
        pnl_acciones_tip_doc = new javax.swing.JPanel();
        btn_agregar_tip_doc = new javax.swing.JButton();
        btn_editar_tip_doc = new javax.swing.JButton();
        btn_eliminar_tip_doc = new javax.swing.JButton();
        btn_limpiar_tip_doc = new javax.swing.JButton();
        scp_tbl_tip_doc = new javax.swing.JScrollPane();
        tbl_tipo_documento = new javax.swing.JTable();
        pnl_buscar_tip_doc = new javax.swing.JPanel();
        txt_buscar_tip_doc = new javax.swing.JTextField();
        btn_buscar_tip_doc = new javax.swing.JButton();
        pnl_menu_estado_ejemplar = new javax.swing.JPanel();
        pnl_datos_est_ejemplar = new javax.swing.JPanel();
        txt_nombre_est_ejemplar = new javax.swing.JTextField();
        txt_descripcion_est_ejemplar = new javax.swing.JTextField();
        txt_id_est_ejemplar = new javax.swing.JTextField();
        lbl_nombre_est_ejemplar = new javax.swing.JLabel();
        lbl_descripcion_est_ejemplar = new javax.swing.JLabel();
        pnl_acciones_est_ejemplar = new javax.swing.JPanel();
        btn_agregar_est_ejemplar = new javax.swing.JButton();
        btn_editar_est_ejemplar = new javax.swing.JButton();
        btn_eliminar_est_ejemplar = new javax.swing.JButton();
        btn_limpiar_est_ejemplar = new javax.swing.JButton();
        pnl_buscar_est_ejemplar = new javax.swing.JPanel();
        txt_buscar_est_ejemplar = new javax.swing.JTextField();
        btn_buscar_est_ejemplar = new javax.swing.JButton();
        scp_tbl_est_ejemplar = new javax.swing.JScrollPane();
        tbl_estado_ejemplar = new javax.swing.JTable();
        pnl_menu_estado_prestamo = new javax.swing.JPanel();
        pnl_datos_est_prestamo = new javax.swing.JPanel();
        txt_nombre_est_prestamo = new javax.swing.JTextField();
        txt_descripcion_est_prestamo = new javax.swing.JTextField();
        txt_id_est_prestamo = new javax.swing.JTextField();
        lbl_nombre_est_prestamo = new javax.swing.JLabel();
        lbl_descripcion_est_prestamo = new javax.swing.JLabel();
        pnl_acciones_est_prestamo = new javax.swing.JPanel();
        btn_agregar_est_prestamo = new javax.swing.JButton();
        btn_editar_est_prestamo = new javax.swing.JButton();
        btn_eliminar_est_prestamo = new javax.swing.JButton();
        btn_limpiar_est_prestamo = new javax.swing.JButton();
        pnl_buscar_est_prestamo = new javax.swing.JPanel();
        txt_buscar_est_prestamo = new javax.swing.JTextField();
        btn_buscar_est_prestamo = new javax.swing.JButton();
        scp_tbl_est_prestamo = new javax.swing.JScrollPane();
        tbl_estado_prestamo = new javax.swing.JTable();
        pnl_menu_auditoria = new javax.swing.JPanel();
        scp_auditoria = new javax.swing.JScrollPane();
        tbl_auditoria = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnl_header_config.setBackground(new java.awt.Color(14, 20, 56));

        lbl_title_configuracion.setFont(new java.awt.Font("Raleway", 1, 24)); // NOI18N
        lbl_title_configuracion.setForeground(new java.awt.Color(255, 215, 0));
        lbl_title_configuracion.setText("Configuración");

        pnl_config_volver.setBackground(new java.awt.Color(14, 20, 56));
        pnl_config_volver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnl_config_volverMouseClicked(evt);
            }
        });

        lbl_btn_volver.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        lbl_btn_volver.setForeground(new java.awt.Color(255, 215, 0));
        lbl_btn_volver.setText("< Volver");

        javax.swing.GroupLayout pnl_config_volverLayout = new javax.swing.GroupLayout(pnl_config_volver);
        pnl_config_volver.setLayout(pnl_config_volverLayout);
        pnl_config_volverLayout.setHorizontalGroup(
            pnl_config_volverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_config_volverLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_btn_volver)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        pnl_config_volverLayout.setVerticalGroup(
            pnl_config_volverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_config_volverLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(lbl_btn_volver)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnl_header_configLayout = new javax.swing.GroupLayout(pnl_header_config);
        pnl_header_config.setLayout(pnl_header_configLayout);
        pnl_header_configLayout.setHorizontalGroup(
            pnl_header_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_header_configLayout.createSequentialGroup()
                .addComponent(pnl_config_volver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(386, 386, 386)
                .addComponent(lbl_title_configuracion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl_header_configLayout.setVerticalGroup(
            pnl_header_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_header_configLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lbl_title_configuracion)
                .addContainerGap(35, Short.MAX_VALUE))
            .addComponent(pnl_config_volver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnl_datos_config.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos"));

        lbl_parametro_config.setText("Parametro:");

        lbl_valor_config.setText("Valor:");

        lbl_descripcion_config.setText("Descripción:");

        javax.swing.GroupLayout pnl_datos_configLayout = new javax.swing.GroupLayout(pnl_datos_config);
        pnl_datos_config.setLayout(pnl_datos_configLayout);
        pnl_datos_configLayout.setHorizontalGroup(
            pnl_datos_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_datos_configLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_id_config, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(pnl_datos_configLayout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(pnl_datos_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_parametro_config)
                    .addComponent(lbl_valor_config)
                    .addComponent(lbl_descripcion_config))
                .addGap(18, 18, 18)
                .addGroup(pnl_datos_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_valor_config, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_parametro_config, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_descripcion_config, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(326, Short.MAX_VALUE))
        );
        pnl_datos_configLayout.setVerticalGroup(
            pnl_datos_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_datos_configLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_id_config, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnl_datos_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_parametro_config, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_parametro_config))
                .addGap(18, 18, 18)
                .addGroup(pnl_datos_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_valor_config, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_valor_config))
                .addGap(18, 18, 18)
                .addGroup(pnl_datos_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_descripcion_config, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_descripcion_config))
                .addContainerGap(97, Short.MAX_VALUE))
        );

        pnl_acciones_config.setBorder(javax.swing.BorderFactory.createTitledBorder("Acciones"));

        btn_agregar_config.setText("Agregar");
        btn_agregar_config.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_agregar_configActionPerformed(evt);
            }
        });

        btn_editar_config.setText("Editar");
        btn_editar_config.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editar_configActionPerformed(evt);
            }
        });

        btn_eliminar_config.setText("Eliminar");
        btn_eliminar_config.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminar_configActionPerformed(evt);
            }
        });

        btn_limpiar_config.setText("Limpiar");
        btn_limpiar_config.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_limpiar_configActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_acciones_configLayout = new javax.swing.GroupLayout(pnl_acciones_config);
        pnl_acciones_config.setLayout(pnl_acciones_configLayout);
        pnl_acciones_configLayout.setHorizontalGroup(
            pnl_acciones_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_acciones_configLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(pnl_acciones_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_limpiar_config, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eliminar_config, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_editar_config, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_agregar_config, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        pnl_acciones_configLayout.setVerticalGroup(
            pnl_acciones_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_acciones_configLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(btn_agregar_config, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_editar_config, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_eliminar_config, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_limpiar_config, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        tbl_configuracion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Parametro", "Valor", "Descripción", "Habilitado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_configuracion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_configuracionMouseClicked(evt);
            }
        });
        scp_tbl_config.setViewportView(tbl_configuracion);
        if (tbl_configuracion.getColumnModel().getColumnCount() > 0) {
            tbl_configuracion.getColumnModel().getColumn(0).setMaxWidth(50);
            tbl_configuracion.getColumnModel().getColumn(1).setMinWidth(300);
            tbl_configuracion.getColumnModel().getColumn(1).setPreferredWidth(300);
            tbl_configuracion.getColumnModel().getColumn(1).setMaxWidth(300);
            tbl_configuracion.getColumnModel().getColumn(2).setMinWidth(200);
            tbl_configuracion.getColumnModel().getColumn(2).setPreferredWidth(200);
            tbl_configuracion.getColumnModel().getColumn(2).setMaxWidth(200);
            tbl_configuracion.getColumnModel().getColumn(2).setHeaderValue("Valor");
            tbl_configuracion.getColumnModel().getColumn(4).setMinWidth(75);
            tbl_configuracion.getColumnModel().getColumn(4).setPreferredWidth(75);
            tbl_configuracion.getColumnModel().getColumn(4).setMaxWidth(75);
        }

        javax.swing.GroupLayout pnl_menu_configLayout = new javax.swing.GroupLayout(pnl_menu_config);
        pnl_menu_config.setLayout(pnl_menu_configLayout);
        pnl_menu_configLayout.setHorizontalGroup(
            pnl_menu_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_menu_configLayout.createSequentialGroup()
                .addContainerGap(811, Short.MAX_VALUE)
                .addComponent(pnl_acciones_config, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(pnl_menu_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_menu_configLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(pnl_menu_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scp_tbl_config)
                        .addGroup(pnl_menu_configLayout.createSequentialGroup()
                            .addComponent(pnl_datos_config, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 290, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        pnl_menu_configLayout.setVerticalGroup(
            pnl_menu_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_menu_configLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnl_acciones_config, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(304, Short.MAX_VALUE))
            .addGroup(pnl_menu_configLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl_menu_configLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnl_datos_config, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(scp_tbl_config, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        tabConfig.addTab("Configuración", pnl_menu_config);

        pnl_datos_editorial.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos"));

        lbl_nombre_editorial.setText("Nombre:");

        lbl_sitio_web_editorial.setText("Sitio web:");

        lbl_observaciones_editorial.setText("Observaciones:");

        txt_observaciones_editorial.setColumns(20);
        txt_observaciones_editorial.setRows(5);
        scp_txt_obser_editorial.setViewportView(txt_observaciones_editorial);

        lbl_pais_editorial.setText("País:");

        cmb_pais_editorial.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_pais_editorialItemStateChanged(evt);
            }
        });

        lbl_ciudad_editorial.setText("Ciudad:");

        lbl_correo_editorial.setText("Correo:");

        lbl_telefono_editorial.setText("Teléfono:");

        javax.swing.GroupLayout pnl_datos_editorialLayout = new javax.swing.GroupLayout(pnl_datos_editorial);
        pnl_datos_editorial.setLayout(pnl_datos_editorialLayout);
        pnl_datos_editorialLayout.setHorizontalGroup(
            pnl_datos_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_datos_editorialLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(pnl_datos_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_correo_editorial)
                    .addComponent(lbl_pais_editorial)
                    .addComponent(lbl_nombre_editorial)
                    .addComponent(lbl_sitio_web_editorial))
                .addGap(10, 10, 10)
                .addGroup(pnl_datos_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnl_datos_editorialLayout.createSequentialGroup()
                        .addComponent(txt_nombre_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(lbl_observaciones_editorial))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_datos_editorialLayout.createSequentialGroup()
                        .addComponent(cmb_pais_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_ciudad_editorial))
                    .addComponent(txt_sitio_web_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnl_datos_editorialLayout.createSequentialGroup()
                        .addComponent(txt_correo_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_telefono_editorial)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnl_datos_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scp_txt_obser_editorial, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                    .addComponent(cmb_ciudad_editorial, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_telefono_editorial))
                .addGap(34, 34, 34))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_datos_editorialLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_id_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnl_datos_editorialLayout.setVerticalGroup(
            pnl_datos_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_datos_editorialLayout.createSequentialGroup()
                .addGroup(pnl_datos_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_datos_editorialLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(pnl_datos_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_nombre_editorial)
                            .addComponent(txt_nombre_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_observaciones_editorial))
                        .addGap(18, 18, 18)
                        .addGroup(pnl_datos_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_sitio_web_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_sitio_web_editorial)))
                    .addGroup(pnl_datos_editorialLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txt_id_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scp_txt_obser_editorial, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnl_datos_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_pais_editorial)
                    .addComponent(cmb_pais_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_ciudad_editorial)
                    .addComponent(cmb_ciudad_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnl_datos_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_correo_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_telefono_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_telefono_editorial)
                    .addComponent(lbl_correo_editorial))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl_acciones_editorial.setBorder(javax.swing.BorderFactory.createTitledBorder("Acciones"));

        btn_agregar_editorial.setText("Agregar");
        btn_agregar_editorial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_agregar_editorialMouseClicked(evt);
            }
        });
        btn_agregar_editorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_agregar_editorialActionPerformed(evt);
            }
        });

        btn_editar_editorial.setText("Editar");
        btn_editar_editorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editar_editorialActionPerformed(evt);
            }
        });

        btn_eliminar_editorial.setText("Eliminar");
        btn_eliminar_editorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminar_editorialActionPerformed(evt);
            }
        });

        btn_limpiar_editorial.setText("Limpiar");
        btn_limpiar_editorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_limpiar_editorialActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_acciones_editorialLayout = new javax.swing.GroupLayout(pnl_acciones_editorial);
        pnl_acciones_editorial.setLayout(pnl_acciones_editorialLayout);
        pnl_acciones_editorialLayout.setHorizontalGroup(
            pnl_acciones_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_acciones_editorialLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(pnl_acciones_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_limpiar_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eliminar_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_editar_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_agregar_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        pnl_acciones_editorialLayout.setVerticalGroup(
            pnl_acciones_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_acciones_editorialLayout.createSequentialGroup()
                .addComponent(btn_agregar_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_editar_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_eliminar_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_limpiar_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl_buscar_editorial.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar"));

        btn_buscar_editorial.setText("Buscar");
        btn_buscar_editorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscar_editorialActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_buscar_editorialLayout = new javax.swing.GroupLayout(pnl_buscar_editorial);
        pnl_buscar_editorial.setLayout(pnl_buscar_editorialLayout);
        pnl_buscar_editorialLayout.setHorizontalGroup(
            pnl_buscar_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_buscar_editorialLayout.createSequentialGroup()
                .addComponent(txt_buscar_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_buscar_editorial)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnl_buscar_editorialLayout.setVerticalGroup(
            pnl_buscar_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txt_buscar_editorial, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btn_buscar_editorial, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        tbl_editoriales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre editorial", "Sitio web", "País", "Ciudad", "Correo", "Teléfono", "Observaciones", "Habilitado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_editoriales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_editorialesMouseClicked(evt);
            }
        });
        scp_tbl_editoriales.setViewportView(tbl_editoriales);
        if (tbl_editoriales.getColumnModel().getColumnCount() > 0) {
            tbl_editoriales.getColumnModel().getColumn(0).setMinWidth(50);
            tbl_editoriales.getColumnModel().getColumn(0).setPreferredWidth(50);
            tbl_editoriales.getColumnModel().getColumn(0).setMaxWidth(50);
            tbl_editoriales.getColumnModel().getColumn(6).setMinWidth(100);
            tbl_editoriales.getColumnModel().getColumn(6).setPreferredWidth(100);
            tbl_editoriales.getColumnModel().getColumn(6).setMaxWidth(100);
            tbl_editoriales.getColumnModel().getColumn(8).setMinWidth(75);
            tbl_editoriales.getColumnModel().getColumn(8).setPreferredWidth(75);
            tbl_editoriales.getColumnModel().getColumn(8).setMaxWidth(75);
        }

        javax.swing.GroupLayout pnl_menu_editorialLayout = new javax.swing.GroupLayout(pnl_menu_editorial);
        pnl_menu_editorial.setLayout(pnl_menu_editorialLayout);
        pnl_menu_editorialLayout.setHorizontalGroup(
            pnl_menu_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_menu_editorialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_menu_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scp_tbl_editoriales)
                    .addComponent(pnl_buscar_editorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_menu_editorialLayout.createSequentialGroup()
                        .addComponent(pnl_datos_editorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(pnl_acciones_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnl_menu_editorialLayout.setVerticalGroup(
            pnl_menu_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_menu_editorialLayout.createSequentialGroup()
                .addGroup(pnl_menu_editorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnl_acciones_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnl_datos_editorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnl_buscar_editorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scp_tbl_editoriales, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabConfig.addTab("Editoriales", pnl_menu_editorial);

        pnl_datos_tip_doc.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos"));

        lbl_nombre_tip_doc.setText("Nombre:");

        lbl_descripcion_tip_doc.setText("Descripción:");

        javax.swing.GroupLayout pnl_datos_tip_docLayout = new javax.swing.GroupLayout(pnl_datos_tip_doc);
        pnl_datos_tip_doc.setLayout(pnl_datos_tip_docLayout);
        pnl_datos_tip_docLayout.setHorizontalGroup(
            pnl_datos_tip_docLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_datos_tip_docLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_id_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(pnl_datos_tip_docLayout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(pnl_datos_tip_docLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_nombre_tip_doc)
                    .addComponent(lbl_descripcion_tip_doc))
                .addGap(18, 18, 18)
                .addGroup(pnl_datos_tip_docLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_nombre_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_descripcion_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(326, Short.MAX_VALUE))
        );
        pnl_datos_tip_docLayout.setVerticalGroup(
            pnl_datos_tip_docLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_datos_tip_docLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_id_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnl_datos_tip_docLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_nombre_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_nombre_tip_doc))
                .addGap(18, 18, 18)
                .addGroup(pnl_datos_tip_docLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_descripcion_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_descripcion_tip_doc))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl_acciones_tip_doc.setBorder(javax.swing.BorderFactory.createTitledBorder("Acciones"));

        btn_agregar_tip_doc.setText("Agregar");
        btn_agregar_tip_doc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_agregar_tip_docMouseClicked(evt);
            }
        });
        btn_agregar_tip_doc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_agregar_tip_docActionPerformed(evt);
            }
        });

        btn_editar_tip_doc.setText("Editar");
        btn_editar_tip_doc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editar_tip_docActionPerformed(evt);
            }
        });

        btn_eliminar_tip_doc.setText("Eliminar");
        btn_eliminar_tip_doc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminar_tip_docActionPerformed(evt);
            }
        });

        btn_limpiar_tip_doc.setText("Limpiar");
        btn_limpiar_tip_doc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_limpiar_tip_docActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_acciones_tip_docLayout = new javax.swing.GroupLayout(pnl_acciones_tip_doc);
        pnl_acciones_tip_doc.setLayout(pnl_acciones_tip_docLayout);
        pnl_acciones_tip_docLayout.setHorizontalGroup(
            pnl_acciones_tip_docLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_acciones_tip_docLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(pnl_acciones_tip_docLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_limpiar_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eliminar_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_editar_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_agregar_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        pnl_acciones_tip_docLayout.setVerticalGroup(
            pnl_acciones_tip_docLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_acciones_tip_docLayout.createSequentialGroup()
                .addComponent(btn_agregar_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_editar_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_eliminar_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_limpiar_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 23, Short.MAX_VALUE))
        );

        tbl_tipo_documento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Descripción", "Habilitado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_tipo_documento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_tipo_documentoMouseClicked(evt);
            }
        });
        scp_tbl_tip_doc.setViewportView(tbl_tipo_documento);
        if (tbl_tipo_documento.getColumnModel().getColumnCount() > 0) {
            tbl_tipo_documento.getColumnModel().getColumn(0).setMaxWidth(50);
            tbl_tipo_documento.getColumnModel().getColumn(1).setMinWidth(300);
            tbl_tipo_documento.getColumnModel().getColumn(1).setPreferredWidth(300);
            tbl_tipo_documento.getColumnModel().getColumn(1).setMaxWidth(300);
            tbl_tipo_documento.getColumnModel().getColumn(3).setMinWidth(75);
            tbl_tipo_documento.getColumnModel().getColumn(3).setPreferredWidth(75);
            tbl_tipo_documento.getColumnModel().getColumn(3).setMaxWidth(75);
        }

        pnl_buscar_tip_doc.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar"));

        btn_buscar_tip_doc.setText("Buscar");
        btn_buscar_tip_doc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscar_tip_docActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_buscar_tip_docLayout = new javax.swing.GroupLayout(pnl_buscar_tip_doc);
        pnl_buscar_tip_doc.setLayout(pnl_buscar_tip_docLayout);
        pnl_buscar_tip_docLayout.setHorizontalGroup(
            pnl_buscar_tip_docLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_buscar_tip_docLayout.createSequentialGroup()
                .addComponent(txt_buscar_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_buscar_tip_doc)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnl_buscar_tip_docLayout.setVerticalGroup(
            pnl_buscar_tip_docLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txt_buscar_tip_doc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btn_buscar_tip_doc, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        javax.swing.GroupLayout pnl_menu_tipo_documentosLayout = new javax.swing.GroupLayout(pnl_menu_tipo_documentos);
        pnl_menu_tipo_documentos.setLayout(pnl_menu_tipo_documentosLayout);
        pnl_menu_tipo_documentosLayout.setHorizontalGroup(
            pnl_menu_tipo_documentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_menu_tipo_documentosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_menu_tipo_documentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(scp_tbl_tip_doc)
                    .addComponent(pnl_buscar_tip_doc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnl_menu_tipo_documentosLayout.createSequentialGroup()
                        .addComponent(pnl_datos_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnl_acciones_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        pnl_menu_tipo_documentosLayout.setVerticalGroup(
            pnl_menu_tipo_documentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_menu_tipo_documentosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnl_menu_tipo_documentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnl_datos_tip_doc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnl_acciones_tip_doc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_buscar_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(scp_tbl_tip_doc, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        tabConfig.addTab("Tipos de documentos", pnl_menu_tipo_documentos);

        pnl_datos_est_ejemplar.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos"));

        lbl_nombre_est_ejemplar.setText("Nombre:");

        lbl_descripcion_est_ejemplar.setText("Descripción:");

        javax.swing.GroupLayout pnl_datos_est_ejemplarLayout = new javax.swing.GroupLayout(pnl_datos_est_ejemplar);
        pnl_datos_est_ejemplar.setLayout(pnl_datos_est_ejemplarLayout);
        pnl_datos_est_ejemplarLayout.setHorizontalGroup(
            pnl_datos_est_ejemplarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_datos_est_ejemplarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_id_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(pnl_datos_est_ejemplarLayout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(pnl_datos_est_ejemplarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_nombre_est_ejemplar)
                    .addComponent(lbl_descripcion_est_ejemplar))
                .addGap(18, 18, 18)
                .addGroup(pnl_datos_est_ejemplarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_nombre_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_descripcion_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(326, Short.MAX_VALUE))
        );
        pnl_datos_est_ejemplarLayout.setVerticalGroup(
            pnl_datos_est_ejemplarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_datos_est_ejemplarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_id_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnl_datos_est_ejemplarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_nombre_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_nombre_est_ejemplar))
                .addGap(18, 18, 18)
                .addGroup(pnl_datos_est_ejemplarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_descripcion_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_descripcion_est_ejemplar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl_acciones_est_ejemplar.setBorder(javax.swing.BorderFactory.createTitledBorder("Acciones"));

        btn_agregar_est_ejemplar.setText("Agregar");
        btn_agregar_est_ejemplar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_agregar_est_ejemplarMouseClicked(evt);
            }
        });
        btn_agregar_est_ejemplar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_agregar_est_ejemplarActionPerformed(evt);
            }
        });

        btn_editar_est_ejemplar.setText("Editar");
        btn_editar_est_ejemplar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editar_est_ejemplarActionPerformed(evt);
            }
        });

        btn_eliminar_est_ejemplar.setText("Eliminar");
        btn_eliminar_est_ejemplar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminar_est_ejemplarActionPerformed(evt);
            }
        });

        btn_limpiar_est_ejemplar.setText("Limpiar");
        btn_limpiar_est_ejemplar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_limpiar_est_ejemplarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_acciones_est_ejemplarLayout = new javax.swing.GroupLayout(pnl_acciones_est_ejemplar);
        pnl_acciones_est_ejemplar.setLayout(pnl_acciones_est_ejemplarLayout);
        pnl_acciones_est_ejemplarLayout.setHorizontalGroup(
            pnl_acciones_est_ejemplarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_acciones_est_ejemplarLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(pnl_acciones_est_ejemplarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_limpiar_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eliminar_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_editar_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_agregar_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        pnl_acciones_est_ejemplarLayout.setVerticalGroup(
            pnl_acciones_est_ejemplarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_acciones_est_ejemplarLayout.createSequentialGroup()
                .addComponent(btn_agregar_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_editar_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_eliminar_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_limpiar_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnl_buscar_est_ejemplar.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar"));

        btn_buscar_est_ejemplar.setText("Buscar");
        btn_buscar_est_ejemplar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscar_est_ejemplarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_buscar_est_ejemplarLayout = new javax.swing.GroupLayout(pnl_buscar_est_ejemplar);
        pnl_buscar_est_ejemplar.setLayout(pnl_buscar_est_ejemplarLayout);
        pnl_buscar_est_ejemplarLayout.setHorizontalGroup(
            pnl_buscar_est_ejemplarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_buscar_est_ejemplarLayout.createSequentialGroup()
                .addComponent(txt_buscar_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_buscar_est_ejemplar)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnl_buscar_est_ejemplarLayout.setVerticalGroup(
            pnl_buscar_est_ejemplarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txt_buscar_est_ejemplar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btn_buscar_est_ejemplar, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        tbl_estado_ejemplar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Descripción", "Habilitado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_estado_ejemplar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_estado_ejemplarMouseClicked(evt);
            }
        });
        scp_tbl_est_ejemplar.setViewportView(tbl_estado_ejemplar);
        if (tbl_estado_ejemplar.getColumnModel().getColumnCount() > 0) {
            tbl_estado_ejemplar.getColumnModel().getColumn(0).setMaxWidth(50);
            tbl_estado_ejemplar.getColumnModel().getColumn(1).setMinWidth(300);
            tbl_estado_ejemplar.getColumnModel().getColumn(1).setPreferredWidth(300);
            tbl_estado_ejemplar.getColumnModel().getColumn(1).setMaxWidth(300);
            tbl_estado_ejemplar.getColumnModel().getColumn(3).setMinWidth(75);
            tbl_estado_ejemplar.getColumnModel().getColumn(3).setPreferredWidth(75);
            tbl_estado_ejemplar.getColumnModel().getColumn(3).setMaxWidth(75);
        }

        javax.swing.GroupLayout pnl_menu_estado_ejemplarLayout = new javax.swing.GroupLayout(pnl_menu_estado_ejemplar);
        pnl_menu_estado_ejemplar.setLayout(pnl_menu_estado_ejemplarLayout);
        pnl_menu_estado_ejemplarLayout.setHorizontalGroup(
            pnl_menu_estado_ejemplarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_menu_estado_ejemplarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_menu_estado_ejemplarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_menu_estado_ejemplarLayout.createSequentialGroup()
                        .addComponent(pnl_datos_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addComponent(pnl_acciones_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnl_buscar_est_ejemplar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scp_tbl_est_ejemplar))
                .addContainerGap())
        );
        pnl_menu_estado_ejemplarLayout.setVerticalGroup(
            pnl_menu_estado_ejemplarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_menu_estado_ejemplarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_menu_estado_ejemplarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnl_acciones_est_ejemplar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnl_datos_est_ejemplar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnl_buscar_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scp_tbl_est_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(139, 139, 139))
        );

        tabConfig.addTab("Estado de ejemplares", pnl_menu_estado_ejemplar);

        pnl_datos_est_prestamo.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos"));

        lbl_nombre_est_prestamo.setText("Nombre:");

        lbl_descripcion_est_prestamo.setText("Descripción:");

        javax.swing.GroupLayout pnl_datos_est_prestamoLayout = new javax.swing.GroupLayout(pnl_datos_est_prestamo);
        pnl_datos_est_prestamo.setLayout(pnl_datos_est_prestamoLayout);
        pnl_datos_est_prestamoLayout.setHorizontalGroup(
            pnl_datos_est_prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_datos_est_prestamoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_id_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(pnl_datos_est_prestamoLayout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(pnl_datos_est_prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_nombre_est_prestamo)
                    .addComponent(lbl_descripcion_est_prestamo))
                .addGap(18, 18, 18)
                .addGroup(pnl_datos_est_prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_nombre_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_descripcion_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(326, Short.MAX_VALUE))
        );
        pnl_datos_est_prestamoLayout.setVerticalGroup(
            pnl_datos_est_prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_datos_est_prestamoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_id_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnl_datos_est_prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_nombre_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_nombre_est_prestamo))
                .addGap(18, 18, 18)
                .addGroup(pnl_datos_est_prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_descripcion_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_descripcion_est_prestamo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl_acciones_est_prestamo.setBorder(javax.swing.BorderFactory.createTitledBorder("Acciones"));

        btn_agregar_est_prestamo.setText("Agregar");
        btn_agregar_est_prestamo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_agregar_est_prestamoMouseClicked(evt);
            }
        });
        btn_agregar_est_prestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_agregar_est_prestamoActionPerformed(evt);
            }
        });

        btn_editar_est_prestamo.setText("Editar");
        btn_editar_est_prestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editar_est_prestamoActionPerformed(evt);
            }
        });

        btn_eliminar_est_prestamo.setText("Eliminar");
        btn_eliminar_est_prestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminar_est_prestamoActionPerformed(evt);
            }
        });

        btn_limpiar_est_prestamo.setText("Limpiar");
        btn_limpiar_est_prestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_limpiar_est_prestamoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_acciones_est_prestamoLayout = new javax.swing.GroupLayout(pnl_acciones_est_prestamo);
        pnl_acciones_est_prestamo.setLayout(pnl_acciones_est_prestamoLayout);
        pnl_acciones_est_prestamoLayout.setHorizontalGroup(
            pnl_acciones_est_prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_acciones_est_prestamoLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(pnl_acciones_est_prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_limpiar_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_eliminar_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_editar_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_agregar_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        pnl_acciones_est_prestamoLayout.setVerticalGroup(
            pnl_acciones_est_prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_acciones_est_prestamoLayout.createSequentialGroup()
                .addComponent(btn_agregar_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_editar_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_eliminar_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_limpiar_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnl_buscar_est_prestamo.setBorder(javax.swing.BorderFactory.createTitledBorder("Buscar"));

        btn_buscar_est_prestamo.setText("Buscar");
        btn_buscar_est_prestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_buscar_est_prestamoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl_buscar_est_prestamoLayout = new javax.swing.GroupLayout(pnl_buscar_est_prestamo);
        pnl_buscar_est_prestamo.setLayout(pnl_buscar_est_prestamoLayout);
        pnl_buscar_est_prestamoLayout.setHorizontalGroup(
            pnl_buscar_est_prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_buscar_est_prestamoLayout.createSequentialGroup()
                .addComponent(txt_buscar_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_buscar_est_prestamo)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnl_buscar_est_prestamoLayout.setVerticalGroup(
            pnl_buscar_est_prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txt_buscar_est_prestamo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btn_buscar_est_prestamo, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        tbl_estado_prestamo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Descripción", "Habilitado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_estado_prestamo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_estado_prestamoMouseClicked(evt);
            }
        });
        scp_tbl_est_prestamo.setViewportView(tbl_estado_prestamo);
        if (tbl_estado_prestamo.getColumnModel().getColumnCount() > 0) {
            tbl_estado_prestamo.getColumnModel().getColumn(0).setMaxWidth(50);
            tbl_estado_prestamo.getColumnModel().getColumn(1).setMinWidth(300);
            tbl_estado_prestamo.getColumnModel().getColumn(1).setPreferredWidth(300);
            tbl_estado_prestamo.getColumnModel().getColumn(1).setMaxWidth(300);
            tbl_estado_prestamo.getColumnModel().getColumn(3).setMinWidth(75);
            tbl_estado_prestamo.getColumnModel().getColumn(3).setPreferredWidth(75);
            tbl_estado_prestamo.getColumnModel().getColumn(3).setMaxWidth(75);
        }

        javax.swing.GroupLayout pnl_menu_estado_prestamoLayout = new javax.swing.GroupLayout(pnl_menu_estado_prestamo);
        pnl_menu_estado_prestamo.setLayout(pnl_menu_estado_prestamoLayout);
        pnl_menu_estado_prestamoLayout.setHorizontalGroup(
            pnl_menu_estado_prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_menu_estado_prestamoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_menu_estado_prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl_menu_estado_prestamoLayout.createSequentialGroup()
                        .addComponent(pnl_datos_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addComponent(pnl_acciones_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnl_buscar_est_prestamo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scp_tbl_est_prestamo))
                .addContainerGap())
        );
        pnl_menu_estado_prestamoLayout.setVerticalGroup(
            pnl_menu_estado_prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_menu_estado_prestamoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl_menu_estado_prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnl_acciones_est_prestamo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnl_datos_est_prestamo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_buscar_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scp_tbl_est_prestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabConfig.addTab("Estado de prestamo", pnl_menu_estado_prestamo);

        tbl_auditoria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Usuario ID", "Nombre Usuario", "Fecha", "Acción", "Detalle"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scp_auditoria.setViewportView(tbl_auditoria);
        if (tbl_auditoria.getColumnModel().getColumnCount() > 0) {
            tbl_auditoria.getColumnModel().getColumn(0).setPreferredWidth(25);
            tbl_auditoria.getColumnModel().getColumn(0).setMaxWidth(25);
            tbl_auditoria.getColumnModel().getColumn(1).setPreferredWidth(75);
            tbl_auditoria.getColumnModel().getColumn(1).setMaxWidth(75);
            tbl_auditoria.getColumnModel().getColumn(2).setPreferredWidth(150);
            tbl_auditoria.getColumnModel().getColumn(2).setMaxWidth(150);
            tbl_auditoria.getColumnModel().getColumn(3).setPreferredWidth(150);
            tbl_auditoria.getColumnModel().getColumn(3).setMaxWidth(150);
            tbl_auditoria.getColumnModel().getColumn(4).setMinWidth(200);
            tbl_auditoria.getColumnModel().getColumn(4).setPreferredWidth(200);
            tbl_auditoria.getColumnModel().getColumn(4).setMaxWidth(200);
            tbl_auditoria.getColumnModel().getColumn(5).setMinWidth(50);
            tbl_auditoria.getColumnModel().getColumn(5).setPreferredWidth(50);
        }

        javax.swing.GroupLayout pnl_menu_auditoriaLayout = new javax.swing.GroupLayout(pnl_menu_auditoria);
        pnl_menu_auditoria.setLayout(pnl_menu_auditoriaLayout);
        pnl_menu_auditoriaLayout.setHorizontalGroup(
            pnl_menu_auditoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_menu_auditoriaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scp_auditoria, javax.swing.GroupLayout.DEFAULT_SIZE, 1075, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnl_menu_auditoriaLayout.setVerticalGroup(
            pnl_menu_auditoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl_menu_auditoriaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scp_auditoria, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabConfig.addTab("Auditoría", pnl_menu_auditoria);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl_header_config, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabConfig)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl_header_config, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pnl_config_volverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnl_config_volverMouseClicked
        dashboardFrm.setVisible(true);
        dispose();
    }//GEN-LAST:event_pnl_config_volverMouseClicked

    private void btn_agregar_configActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agregar_configActionPerformed
       try {
           if (Validaciones.validarCamposRequeridos(new JTextField[]{txt_parametro_config,txt_valor_config}, new JComboBox[]{})) {
                String parametro = txt_parametro_config.getText();
                String valor = txt_valor_config.getText();
                String descripcion = txt_descripcion_config.getText();

                Configuracion add = new Configuracion();

                add.setParametro(parametro);
                add.setValor(valor);
                add.setDescripcion(descripcion);
                add.setHabilitado(true);

                // Enviar al controlador
                boolean exito = configController.insertar(add);

                if (exito) {
                    limpiarConfig();
                    cargarDatosConfig(configController.listar());
                    cargarAuditoria(auditController.listar());
                    JOptionPane.showMessageDialog(null, "Configuración agregada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al agregar la configuración.");
                }
           }

        } catch (HeadlessException e) {
            logger.error("Error al procesar la configuración: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_agregar_configActionPerformed

    private void btn_editar_configActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editar_configActionPerformed
        try {
            if (Validaciones.validarCamposRequeridos(new JTextField[]{txt_parametro_config,txt_valor_config}, new JComboBox[]{})) {
                Integer idConfig = Integer.parseInt(txt_id_config.getText());
                String parametro = txt_parametro_config.getText();
                String valor = txt_valor_config.getText();
                String descripcion = txt_descripcion_config.getText();

                Configuracion edit = new Configuracion();

                edit.setIdConfig(idConfig);
                edit.setParametro(parametro);
                edit.setValor(valor);
                edit.setDescripcion(descripcion);
                edit.setHabilitado(true);

                // Enviar al controlador
                boolean exito = configController.actualizar(edit);

                if (exito) {
                    limpiarConfig();
                    cargarDatosConfig(configController.listar());
                    cargarAuditoria(auditController.listar());
                    JOptionPane.showMessageDialog(null, "Configuración actualizada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el configuración.");
                }
            }

        } catch (HeadlessException e) {
            logger.error("Error al procesar la configuración: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_editar_configActionPerformed

    private void btn_eliminar_configActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminar_configActionPerformed
        if(txt_id_config.getText().length() > 0){
            if (JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar la configuración con ID: " +  txt_id_config.getText() , "WARNING",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                boolean exito = configController.eliminar(Integer.parseInt(txt_id_config.getText()));
                if (exito) {
                    limpiarConfig();
                    cargarDatosConfig(configController.listar());
                    cargarAuditoria(auditController.listar());
                    JOptionPane.showMessageDialog(null, "Configuración eliminada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar configuración.");
                }
            }
        }else {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una configuración antes de eliminarlo.");
        }
    }//GEN-LAST:event_btn_eliminar_configActionPerformed

    private void btn_limpiar_configActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_limpiar_configActionPerformed
        limpiarConfig();
    }//GEN-LAST:event_btn_limpiar_configActionPerformed

    private void tbl_configuracionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_configuracionMouseClicked
        limpiarConfig();
        int index = tbl_configuracion.getSelectedRow();
        TableModel modeloConfig = tbl_configuracion.getModel();
        txt_id_config.setText(modeloConfig.getValueAt(index, 0).toString());
        txt_parametro_config.setText(modeloConfig.getValueAt(index, 1).toString());
        txt_valor_config.setText(modeloConfig.getValueAt(index, 2).toString());
        txt_descripcion_config.setText(Validaciones.safeGetValue(modeloConfig, index, 3)); // posible valor vacío
        txt_parametro_config.setEditable(false);
    }//GEN-LAST:event_tbl_configuracionMouseClicked

    private void btn_agregar_tip_docActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agregar_tip_docActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_agregar_tip_docActionPerformed

    private void btn_editar_tip_docActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editar_tip_docActionPerformed
        try {
            if (Validaciones.validarCamposRequeridos(new JTextField[]{txt_nombre_tip_doc,txt_descripcion_tip_doc}, new JComboBox[]{})) {
                Integer idTipoDoc = Integer.parseInt(txt_id_tip_doc.getText());
                String nombreTipDoc = txt_nombre_tip_doc.getText();
                String descripcionTipDoc = txt_descripcion_tip_doc.getText();

                TipoDocumento tipDoc = new TipoDocumento();
                tipDoc.setTipoDocumentoID(idTipoDoc);
                tipDoc.setNombre(nombreTipDoc);
                tipDoc.setDescripcion(descripcionTipDoc);
                tipDoc.setHabilitado(true);

                Boolean exito = tipoDocController.actualizar(tipDoc);

                if (exito) {
                    limpiarTipoDocumento();
                    cargarTipoDocumento(tipoDocController.listar());
                    cargarAuditoria(auditController.listar());
                    JOptionPane.showMessageDialog(null, "Tipo de documento actualizado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el tipo de documento.");
                }
            }
            
         } catch (HeadlessException e) {
            logger.error("Error al procesar tipo de documento: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_editar_tip_docActionPerformed

    private void btn_eliminar_tip_docActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminar_tip_docActionPerformed
        if(txt_id_tip_doc.getText().length() > 0){
            if (JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el tipo de documento con ID: " +  txt_id_tip_doc.getText() , "WARNING",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                boolean exito = tipoDocController.eliminar(Integer.parseInt(txt_id_tip_doc.getText()));
                if (exito) {
                    limpiarTipoDocumento();
                    cargarTipoDocumento(tipoDocController.listar());
                    cargarAuditoria(auditController.listar());
                    JOptionPane.showMessageDialog(null, "Tipo de documento eliminado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el tipo de documento.");
                }
            }
        }else {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un tipo de documento antes de eliminarlo.");
        }
    }//GEN-LAST:event_btn_eliminar_tip_docActionPerformed

    private void btn_limpiar_tip_docActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_limpiar_tip_docActionPerformed
         limpiarTipoDocumento();
    }//GEN-LAST:event_btn_limpiar_tip_docActionPerformed

    private void tbl_tipo_documentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_tipo_documentoMouseClicked
        limpiarTipoDocumento();
        int index = tbl_tipo_documento.getSelectedRow();
        TableModel modeloTipDoc = tbl_tipo_documento.getModel();
        txt_id_tip_doc.setText(modeloTipDoc.getValueAt(index, 0).toString());
        txt_nombre_tip_doc.setText(modeloTipDoc.getValueAt(index, 1).toString());
        txt_descripcion_tip_doc.setText(Validaciones.safeGetValue(modeloTipDoc, index, 2)); // posible campo vacío
    }//GEN-LAST:event_tbl_tipo_documentoMouseClicked

    private void btn_agregar_tip_docMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_agregar_tip_docMouseClicked
        try {
            if (Validaciones.validarCamposRequeridos(new JTextField[]{txt_nombre_tip_doc,txt_descripcion_tip_doc}, new JComboBox[]{})) {
                String nombreTipDoc = txt_nombre_tip_doc.getText();
                String descripcionTipDoc = txt_descripcion_tip_doc.getText();

                TipoDocumento tipDoc = new TipoDocumento();
                tipDoc.setNombre(nombreTipDoc);
                tipDoc.setDescripcion(descripcionTipDoc);
                tipDoc.setHabilitado(true);

                Boolean exito = tipoDocController.insertar(tipDoc);

                if (exito) {
                    limpiarTipoDocumento();
                    cargarTipoDocumento(tipoDocController.listar());
                    cargarAuditoria(auditController.listar());
                    JOptionPane.showMessageDialog(null, "Tipo de documento agregado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al agregar el tipo de documento.");
                }
            }
         } catch (HeadlessException e) {
            logger.error("Error al procesar tipo de documento: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_agregar_tip_docMouseClicked

    private void btn_agregar_editorialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_agregar_editorialMouseClicked
        try {
            if (Validaciones.validarCamposRequeridos(new JTextField[]{txt_nombre_editorial,txt_correo_editorial, txt_telefono_editorial}, new JComboBox[]{cmb_pais_editorial, cmb_ciudad_editorial})) {
                String nombreEditorial = txt_nombre_editorial.getText();
                String sitio = txt_sitio_web_editorial.getText();
                String observaciones = txt_observaciones_editorial.getText();
                String correoEdi = txt_correo_editorial.getText();
                String telefonoEdi = txt_telefono_editorial.getText();
                Pais paisEdi = (Pais) cmb_pais_editorial.getSelectedItem();
                Ciudad ciudadEdi = (Ciudad) cmb_ciudad_editorial.getSelectedItem();
                
                Editorial model = new Editorial();
                model.setNombreEditorial(nombreEditorial);
                model.setSitioWeb(sitio);
                model.setObservaciones(observaciones);
                model.setContactoEmail(correoEdi);
                model.setContactoPhone(telefonoEdi);
                model.setPaisEditorial(paisEdi);
                model.setCiudadEditorial(ciudadEdi);
                model.setHabilitado(true);
                
                Boolean exito = editorialController.insertar(model);

                if (exito) {
                    limpiarEditorial();
                    cargarDatosEditoriales(editorialController.listar());
                    cargarAuditoria(auditController.listar());
                    JOptionPane.showMessageDialog(null, "Editorial agregado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al agregar la editorial.");
                }
            }
        } catch (HeadlessException e) {
            logger.error(e.getMessage() + "Error al procesar editorial: ");
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_agregar_editorialMouseClicked

    private void btn_agregar_editorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agregar_editorialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_agregar_editorialActionPerformed

    private void btn_editar_editorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editar_editorialActionPerformed
        try {
            if (Validaciones.validarCamposRequeridos(new JTextField[]{txt_nombre_editorial,txt_correo_editorial, txt_telefono_editorial}, new JComboBox[]{cmb_pais_editorial, cmb_ciudad_editorial})) {
                Integer editorialID = Integer.parseInt(txt_id_editorial.getText());
                String nombreEditorial = txt_nombre_editorial.getText();
                String sitio = txt_sitio_web_editorial.getText();
                String observaciones = txt_observaciones_editorial.getText();
                String correoEdi = txt_correo_editorial.getText();
                String telefonoEdi = txt_telefono_editorial.getText();
                Pais paisEdi = (Pais) cmb_pais_editorial.getSelectedItem();
                Ciudad ciudadEdi = (Ciudad) cmb_ciudad_editorial.getSelectedItem();
                
                Editorial model = new Editorial();
                model.setEditorialID(editorialID);
                model.setNombreEditorial(nombreEditorial);
                model.setSitioWeb(sitio);
                model.setObservaciones(observaciones);
                model.setContactoEmail(correoEdi);
                model.setContactoPhone(telefonoEdi);
                model.setPaisEditorial(paisEdi);
                model.setCiudadEditorial(ciudadEdi);
                model.setHabilitado(true);
                
                Boolean exito = editorialController.actualizar(model);

                if (exito) {
                    limpiarEditorial();
                    cargarDatosEditoriales(editorialController.listar());
                    cargarAuditoria(auditController.listar());
                    JOptionPane.showMessageDialog(null, "Editorial actualizada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al editar la editorial.");
                }
            }
        } catch (HeadlessException e) {
            logger.error(e.getMessage() + "Error al procesar editorial: ");
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_editar_editorialActionPerformed

    private void btn_eliminar_editorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminar_editorialActionPerformed
        if(txt_id_editorial.getText().length() > 0){
            if (JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar la editorial con ID: " +  txt_id_editorial.getText() , "WARNING",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                boolean exito = editorialController.eliminar(Integer.parseInt(txt_id_editorial.getText()));
                if (exito) {
                    limpiarEditorial();
                    cargarDatosEditoriales(editorialController.listar());
                    cargarAuditoria(auditController.listar());
                    JOptionPane.showMessageDialog(null, "Editorial eliminada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar la editorial.");
                }
            }
        }else {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una configuración antes de eliminarlo.");
        }
    }//GEN-LAST:event_btn_eliminar_editorialActionPerformed

    private void btn_limpiar_editorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_limpiar_editorialActionPerformed
        limpiarEditorial();
    }//GEN-LAST:event_btn_limpiar_editorialActionPerformed

    private void cmb_pais_editorialItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_pais_editorialItemStateChanged
       if (evt.getStateChange() == ItemEvent.SELECTED) {
        Pais seleccionado = (Pais) cmb_pais_editorial.getSelectedItem();
        if (seleccionado != null) {
            List<Ciudad> ciudades =  new ArrayList<>();
            ciudades.addAll(editorialController.obtenerCiudades(seleccionado.getPaisID()));
            cmb_ciudad_editorial.setModel(new DefaultComboBoxModel<>(ciudades.toArray(new Ciudad[0])));
        }
    }

    }//GEN-LAST:event_cmb_pais_editorialItemStateChanged

    private void tbl_editorialesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_editorialesMouseClicked
        limpiarEditorial();
        int index = tbl_editoriales.getSelectedRow();
        TableModel modeloEdi = tbl_editoriales.getModel();
        txt_id_editorial.setText(modeloEdi.getValueAt(index, 0).toString());
        txt_nombre_editorial.setText(Validaciones.safeGetValue(modeloEdi, index, 1));
        txt_sitio_web_editorial.setText(Validaciones.safeGetValue(modeloEdi, index, 2));
        txt_correo_editorial.setText(Validaciones.safeGetValue(modeloEdi, index, 5));
        txt_telefono_editorial.setText(Validaciones.safeGetValue(modeloEdi, index, 6));
        txt_observaciones_editorial.setText(Validaciones.safeGetValue(modeloEdi, index, 7));
        
        
        
        String nombrePais = modeloEdi.getValueAt(index, 3).toString();
        for (int i = 0; i < cmb_pais_editorial.getItemCount(); i++) {
            Pais p = (Pais) cmb_pais_editorial.getItemAt(i);
            if (p.getNombre().equalsIgnoreCase(nombrePais)) {
                cmb_pais_editorial.setSelectedIndex(i);
                break;
            }
        }

        String nombreCiudad = modeloEdi.getValueAt(index, 4).toString();
        for (int i = 0; i < cmb_ciudad_editorial.getItemCount(); i++) {
            Ciudad c = (Ciudad) cmb_ciudad_editorial.getItemAt(i);
            if (c.getNombre().equalsIgnoreCase(nombreCiudad)) {
                cmb_ciudad_editorial.setSelectedIndex(i);
                break;
            }
        }

        
    }//GEN-LAST:event_tbl_editorialesMouseClicked

    private void btn_buscar_editorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar_editorialActionPerformed
        String filter = txt_buscar_editorial.getText();
        cargarDatosEditoriales(editorialController.buscar(filter));
        txt_buscar_editorial.setText("");
    }//GEN-LAST:event_btn_buscar_editorialActionPerformed

    private void btn_buscar_tip_docActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar_tip_docActionPerformed
        String filter = txt_buscar_tip_doc.getText();
        cargarTipoDocumento(tipoDocController.buscar(filter));
        txt_buscar_tip_doc.setText("");
    }//GEN-LAST:event_btn_buscar_tip_docActionPerformed

    private void btn_agregar_est_ejemplarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_agregar_est_ejemplarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_agregar_est_ejemplarMouseClicked

    private void btn_agregar_est_ejemplarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agregar_est_ejemplarActionPerformed
        try {
            if (Validaciones.validarCamposRequeridos(new JTextField[]{txt_nombre_est_ejemplar,txt_descripcion_est_ejemplar}, new JComboBox[]{})) {
                String nombreEstEjemp = txt_nombre_est_ejemplar.getText();
                String descripcionEstEjemp = txt_descripcion_est_ejemplar.getText();

                EstadoEjemplar model = new EstadoEjemplar();
                model.setNombre(nombreEstEjemp);
                model.setDescripcion(descripcionEstEjemp);
                model.setHabilitado(true);

                Boolean exito = estadoEjempController.insertar(model);

                if (exito) {
                    limpiarEstEjemplar();
                    cargarEstadoEjemplar(estadoEjempController.listar());
                    cargarAuditoria(auditController.listar());
                    JOptionPane.showMessageDialog(null, "Estado de ejemplar agregado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al agregar el estado de ejemplar.");
                }
            }
            
         } catch (HeadlessException e) {
            logger.error("Error al procesar estado de ejemplar: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_agregar_est_ejemplarActionPerformed

    private void btn_editar_est_ejemplarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editar_est_ejemplarActionPerformed
        try {
            if (Validaciones.validarCamposRequeridos(new JTextField[]{txt_nombre_est_ejemplar,txt_descripcion_est_ejemplar}, new JComboBox[]{})) {
                Integer idEstEjemp = Integer.parseInt(txt_id_est_ejemplar.getText());
                String nombreEstEjemp = txt_nombre_est_ejemplar.getText();
                String descripcionEstEjemp = txt_descripcion_est_ejemplar.getText();

                EstadoEjemplar model = new EstadoEjemplar();
                model.setEstadoEjemplarID(idEstEjemp);
                model.setNombre(nombreEstEjemp);
                model.setDescripcion(descripcionEstEjemp);
                model.setHabilitado(true);

                Boolean exito = estadoEjempController.actualizar(model);

                if (exito) {
                    limpiarEstEjemplar();
                    cargarEstadoEjemplar(estadoEjempController.listar());
                    cargarAuditoria(auditController.listar());
                    JOptionPane.showMessageDialog(null, "Estado de ejemplar actualizado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el estado de ejemplar.");
                }
            }
            
         } catch (HeadlessException e) {
            logger.error("Error al procesar estado de ejemplar: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_editar_est_ejemplarActionPerformed

    private void btn_eliminar_est_ejemplarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminar_est_ejemplarActionPerformed
        if(txt_id_est_ejemplar.getText().length() > 0){
            if (JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el estado de ejempar con ID: " +  txt_id_est_ejemplar.getText() , "WARNING",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                boolean exito = estadoEjempController.eliminar(Integer.parseInt(txt_id_est_ejemplar.getText()));
                if (exito) {
                    limpiarEstEjemplar();
                    cargarEstadoEjemplar(estadoEjempController.listar());
                    cargarAuditoria(auditController.listar());
                    JOptionPane.showMessageDialog(null, "Estado de ejemplar eliminado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el estado de ejemplar.");
                }
            }
        }else {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un estado de ejemplar antes de eliminarlo.");
        }
    }//GEN-LAST:event_btn_eliminar_est_ejemplarActionPerformed

    private void btn_limpiar_est_ejemplarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_limpiar_est_ejemplarActionPerformed
        limpiarEstEjemplar();
    }//GEN-LAST:event_btn_limpiar_est_ejemplarActionPerformed

    private void btn_buscar_est_ejemplarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar_est_ejemplarActionPerformed
        String filter = txt_buscar_est_ejemplar.getText();
        cargarEstadoEjemplar(estadoEjempController.buscar(filter));
        txt_buscar_est_ejemplar.setText("");
    }//GEN-LAST:event_btn_buscar_est_ejemplarActionPerformed

    private void btn_agregar_est_prestamoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_agregar_est_prestamoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_agregar_est_prestamoMouseClicked

    private void btn_agregar_est_prestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_agregar_est_prestamoActionPerformed
        try {
            if (Validaciones.validarCamposRequeridos(new JTextField[]{txt_nombre_est_prestamo,txt_descripcion_est_prestamo}, new JComboBox[]{})) {
                String nombreEstPres = txt_nombre_est_prestamo.getText();
                String descripcionEstPres = txt_descripcion_est_prestamo.getText();

                EstadoPrestamo model = new EstadoPrestamo();
                model.setNombre(nombreEstPres);
                model.setDescripcion(descripcionEstPres);
                model.setHabilitado(true);

                Boolean exito = estadoPrestController.insertar(model);

                if (exito) {
                    limpiarEstPrestamo();
                    cargarEstadoPrestamo(estadoPrestController.listar());
                    cargarAuditoria(auditController.listar());
                    JOptionPane.showMessageDialog(null, "Estado préstamo agregado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al agregar el estado de préstamo.");
                }
            }
         } catch (HeadlessException e) {
            logger.error("Error al procesar estado de préstamo: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_agregar_est_prestamoActionPerformed

    private void btn_editar_est_prestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editar_est_prestamoActionPerformed
        try {
            if (Validaciones.validarCamposRequeridos(new JTextField[]{txt_nombre_est_prestamo,txt_descripcion_est_prestamo}, new JComboBox[]{})) {
                Integer idEstPrest = Integer.parseInt(txt_id_est_prestamo.getText());
                String nombreEstPres = txt_nombre_est_prestamo.getText();
                String descripcionEstPres = txt_descripcion_est_prestamo.getText();

                EstadoPrestamo model = new EstadoPrestamo();
                model.setEstadoPrestamoID(idEstPrest);
                model.setNombre(nombreEstPres);
                model.setDescripcion(descripcionEstPres);
                model.setHabilitado(true);

                Boolean exito = estadoPrestController.actualizar(model);

                if (exito) {
                    limpiarEstPrestamo();
                    cargarEstadoPrestamo(estadoPrestController.listar());
                    cargarAuditoria(auditController.listar());
                    JOptionPane.showMessageDialog(null, "Estado préstamo modificado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al modificar el estado de préstamo.");
                }
            }
         } catch (HeadlessException e) {
            logger.error("Error al procesar estado de préstamo: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
        }
    }//GEN-LAST:event_btn_editar_est_prestamoActionPerformed

    private void btn_eliminar_est_prestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminar_est_prestamoActionPerformed
         if(txt_id_est_prestamo.getText().length() > 0){
            if (JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el estado de préstamo con ID: " +  txt_id_est_prestamo.getText() , "WARNING",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                boolean exito = estadoPrestController.eliminar(Integer.parseInt(txt_id_est_prestamo.getText()));
                if (exito) {
                    limpiarEstPrestamo();
                    cargarEstadoPrestamo(estadoPrestController.listar());
                    cargarAuditoria(auditController.listar());
                    JOptionPane.showMessageDialog(null, "Estado de préstamo eliminado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el estado de préstamo.");
                }
            }
        }else {
            JOptionPane.showMessageDialog(null, "Debes seleccionar un estado de préstamo antes de eliminarlo.");
        }
    }//GEN-LAST:event_btn_eliminar_est_prestamoActionPerformed

    private void btn_limpiar_est_prestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_limpiar_est_prestamoActionPerformed
        limpiarEstPrestamo();
    }//GEN-LAST:event_btn_limpiar_est_prestamoActionPerformed

    private void btn_buscar_est_prestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_buscar_est_prestamoActionPerformed
        String filter = txt_buscar_est_prestamo.getText();
        cargarEstadoPrestamo(estadoPrestController.buscar(filter));
        txt_buscar_est_prestamo.setText("");
    }//GEN-LAST:event_btn_buscar_est_prestamoActionPerformed

    private void tbl_estado_ejemplarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_estado_ejemplarMouseClicked
        limpiarEstEjemplar();
        int index = tbl_estado_ejemplar.getSelectedRow();
        TableModel modelo = tbl_estado_ejemplar.getModel();
        txt_id_est_ejemplar.setText(modelo.getValueAt(index, 0).toString());
        txt_nombre_est_ejemplar.setText(modelo.getValueAt(index, 1).toString());
        txt_descripcion_est_ejemplar.setText(Validaciones.safeGetValue(modelo, index, 2)); // posible campo vacío
    }//GEN-LAST:event_tbl_estado_ejemplarMouseClicked

    private void tbl_estado_prestamoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_estado_prestamoMouseClicked
        limpiarEstPrestamo();
        int index = tbl_estado_prestamo.getSelectedRow();
        TableModel modelo = tbl_estado_prestamo.getModel();
        txt_id_est_prestamo.setText(modelo.getValueAt(index, 0).toString());
        txt_nombre_est_prestamo.setText(modelo.getValueAt(index, 1).toString());
        txt_descripcion_est_prestamo.setText(Validaciones.safeGetValue(modelo, index, 2)); // posible campo vacío
    }//GEN-LAST:event_tbl_estado_prestamoMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ConfiguracionFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConfiguracionFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConfiguracionFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConfiguracionFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
       
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_agregar_config;
    private javax.swing.JButton btn_agregar_editorial;
    private javax.swing.JButton btn_agregar_est_ejemplar;
    private javax.swing.JButton btn_agregar_est_prestamo;
    private javax.swing.JButton btn_agregar_tip_doc;
    private javax.swing.JButton btn_buscar_editorial;
    private javax.swing.JButton btn_buscar_est_ejemplar;
    private javax.swing.JButton btn_buscar_est_prestamo;
    private javax.swing.JButton btn_buscar_tip_doc;
    private javax.swing.JButton btn_editar_config;
    private javax.swing.JButton btn_editar_editorial;
    private javax.swing.JButton btn_editar_est_ejemplar;
    private javax.swing.JButton btn_editar_est_prestamo;
    private javax.swing.JButton btn_editar_tip_doc;
    private javax.swing.JButton btn_eliminar_config;
    private javax.swing.JButton btn_eliminar_editorial;
    private javax.swing.JButton btn_eliminar_est_ejemplar;
    private javax.swing.JButton btn_eliminar_est_prestamo;
    private javax.swing.JButton btn_eliminar_tip_doc;
    private javax.swing.JButton btn_limpiar_config;
    private javax.swing.JButton btn_limpiar_editorial;
    private javax.swing.JButton btn_limpiar_est_ejemplar;
    private javax.swing.JButton btn_limpiar_est_prestamo;
    private javax.swing.JButton btn_limpiar_tip_doc;
    private javax.swing.JComboBox<Ciudad> cmb_ciudad_editorial;
    private javax.swing.JComboBox<Pais> cmb_pais_editorial;
    private javax.swing.JLabel lbl_btn_volver;
    private javax.swing.JLabel lbl_ciudad_editorial;
    private javax.swing.JLabel lbl_correo_editorial;
    private javax.swing.JLabel lbl_descripcion_config;
    private javax.swing.JLabel lbl_descripcion_est_ejemplar;
    private javax.swing.JLabel lbl_descripcion_est_prestamo;
    private javax.swing.JLabel lbl_descripcion_tip_doc;
    private javax.swing.JLabel lbl_nombre_editorial;
    private javax.swing.JLabel lbl_nombre_est_ejemplar;
    private javax.swing.JLabel lbl_nombre_est_prestamo;
    private javax.swing.JLabel lbl_nombre_tip_doc;
    private javax.swing.JLabel lbl_observaciones_editorial;
    private javax.swing.JLabel lbl_pais_editorial;
    private javax.swing.JLabel lbl_parametro_config;
    private javax.swing.JLabel lbl_sitio_web_editorial;
    private javax.swing.JLabel lbl_telefono_editorial;
    private javax.swing.JLabel lbl_title_configuracion;
    private javax.swing.JLabel lbl_valor_config;
    private javax.swing.JPanel pnl_acciones_config;
    private javax.swing.JPanel pnl_acciones_editorial;
    private javax.swing.JPanel pnl_acciones_est_ejemplar;
    private javax.swing.JPanel pnl_acciones_est_prestamo;
    private javax.swing.JPanel pnl_acciones_tip_doc;
    private javax.swing.JPanel pnl_buscar_editorial;
    private javax.swing.JPanel pnl_buscar_est_ejemplar;
    private javax.swing.JPanel pnl_buscar_est_prestamo;
    private javax.swing.JPanel pnl_buscar_tip_doc;
    private javax.swing.JPanel pnl_config_volver;
    private javax.swing.JPanel pnl_datos_config;
    private javax.swing.JPanel pnl_datos_editorial;
    private javax.swing.JPanel pnl_datos_est_ejemplar;
    private javax.swing.JPanel pnl_datos_est_prestamo;
    private javax.swing.JPanel pnl_datos_tip_doc;
    private javax.swing.JPanel pnl_header_config;
    private javax.swing.JPanel pnl_menu_auditoria;
    private javax.swing.JPanel pnl_menu_config;
    private javax.swing.JPanel pnl_menu_editorial;
    private javax.swing.JPanel pnl_menu_estado_ejemplar;
    private javax.swing.JPanel pnl_menu_estado_prestamo;
    private javax.swing.JPanel pnl_menu_tipo_documentos;
    private javax.swing.JScrollPane scp_auditoria;
    private javax.swing.JScrollPane scp_tbl_config;
    private javax.swing.JScrollPane scp_tbl_editoriales;
    private javax.swing.JScrollPane scp_tbl_est_ejemplar;
    private javax.swing.JScrollPane scp_tbl_est_prestamo;
    private javax.swing.JScrollPane scp_tbl_tip_doc;
    private javax.swing.JScrollPane scp_txt_obser_editorial;
    private javax.swing.JTabbedPane tabConfig;
    private javax.swing.JTable tbl_auditoria;
    private javax.swing.JTable tbl_configuracion;
    private javax.swing.JTable tbl_editoriales;
    private javax.swing.JTable tbl_estado_ejemplar;
    private javax.swing.JTable tbl_estado_prestamo;
    private javax.swing.JTable tbl_tipo_documento;
    private javax.swing.JTextField txt_buscar_editorial;
    private javax.swing.JTextField txt_buscar_est_ejemplar;
    private javax.swing.JTextField txt_buscar_est_prestamo;
    private javax.swing.JTextField txt_buscar_tip_doc;
    private javax.swing.JTextField txt_correo_editorial;
    private javax.swing.JTextField txt_descripcion_config;
    private javax.swing.JTextField txt_descripcion_est_ejemplar;
    private javax.swing.JTextField txt_descripcion_est_prestamo;
    private javax.swing.JTextField txt_descripcion_tip_doc;
    private javax.swing.JTextField txt_id_config;
    private javax.swing.JTextField txt_id_editorial;
    private javax.swing.JTextField txt_id_est_ejemplar;
    private javax.swing.JTextField txt_id_est_prestamo;
    private javax.swing.JTextField txt_id_tip_doc;
    private javax.swing.JTextField txt_nombre_editorial;
    private javax.swing.JTextField txt_nombre_est_ejemplar;
    private javax.swing.JTextField txt_nombre_est_prestamo;
    private javax.swing.JTextField txt_nombre_tip_doc;
    private javax.swing.JTextArea txt_observaciones_editorial;
    private javax.swing.JTextField txt_parametro_config;
    private javax.swing.JTextField txt_sitio_web_editorial;
    private javax.swing.JTextField txt_telefono_editorial;
    private javax.swing.JTextField txt_valor_config;
    // End of variables declaration//GEN-END:variables
}

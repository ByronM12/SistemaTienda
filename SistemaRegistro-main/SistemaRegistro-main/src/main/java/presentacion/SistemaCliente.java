package presentacion;

import modelo.Cliente;
import modelo.Cliente.TipoCliente;
import negocio.ClienteServicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.*;

public class SistemaCliente extends JFrame {
    
    private static final Logger LOGGER = Logger.getLogger(SistemaCliente.class.getName());
    private final ClienteServicio clienteServicio;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtApellido, txtCedula, txtCorreo, txtFechaNac;
    private JTextField txtDireccion, txtTelefono, txtRuc;
    private JCheckBox chkEsEmpresa;
    private JComboBox<TipoCliente> cmbTipoCliente;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;

    public SistemaCliente() {
        super("COMPUTEC - Gestión de Clientes");
        clienteServicio = new ClienteServicio();
        configurarInterfaz();
        cargarClientes();
        setLocationRelativeTo(null);
    }

    private void configurarInterfaz() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setMinimumSize(new Dimension(900, 550));
        
        // Configuración de colores y fuentes
        Color colorFondo = new Color(240, 240, 240);
        Color colorTitulo = new Color(0, 102, 204);
        Font fontTitulo = new Font("Segoe UI", Font.BOLD, 16);
        Font fontNormal = new Font("Segoe UI", Font.PLAIN, 12);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(colorFondo);

        // Panel de título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(colorTitulo);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JLabel lblTitulo = new JLabel("COMPUTEC - Gestión de Clientes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);

        // Panel de contenido
        JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
        panelContenido.setBackground(colorFondo);

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(colorFondo);
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        txtNombre = crearCampoTexto(20, fontNormal);
        txtApellido = crearCampoTexto(20, fontNormal);
        txtCedula = crearCampoNumerico(10, fontNormal);
        txtCorreo = crearCampoTexto(20, fontNormal);
        txtFechaNac = crearCampoTexto(10, fontNormal);
        txtDireccion = crearCampoTexto(25, fontNormal);
        txtTelefono = crearCampoNumerico(10, fontNormal);
        txtRuc = crearCampoRUC(fontNormal);
        txtRuc.setEnabled(false);
        
        chkEsEmpresa = new JCheckBox("Es empresa");
        chkEsEmpresa.setFont(fontNormal);
        chkEsEmpresa.addActionListener(e -> {
            txtRuc.setEnabled(chkEsEmpresa.isSelected());
            if (!chkEsEmpresa.isSelected()) {
                txtRuc.setText("");
            }
        });
        
        cmbTipoCliente = new JComboBox<>(TipoCliente.values());
        cmbTipoCliente.setFont(fontNormal);
        
        txtCedula.setToolTipText("Ingrese 10 dígitos para cédula");
        txtRuc.setToolTipText("Ingrese 13 dígitos para RUC");
        txtTelefono.setToolTipText("Ingrese 10 dígitos para teléfono");
        txtFechaNac.setToolTipText("Formato: AAAA-MM-DD");
        txtCorreo.setToolTipText("Ejemplo: usuario@dominio.com");

        agregarComponentesFormulario(panelFormulario, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(colorFondo);
        
        btnAgregar = crearBoton("Agregar", new Color(46, 204, 113), fontNormal);
        btnAgregar.addActionListener(this::agregarCliente);
        
        btnActualizar = crearBoton("Actualizar", new Color(52, 152, 219), fontNormal);
        btnActualizar.addActionListener(this::actualizarCliente);
        
        btnEliminar = crearBoton("Eliminar", new Color(231, 76, 60), fontNormal);
        btnEliminar.addActionListener(this::eliminarCliente);
        
        btnLimpiar = crearBoton("Limpiar", new Color(149, 165, 166), fontNormal);
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // Configurar tabla
        configurarTabla(fontTitulo, fontNormal);

        // Agregar componentes al panel de contenido
        JPanel panelFormularioCompleto = new JPanel(new BorderLayout());
        panelFormularioCompleto.add(panelFormulario, BorderLayout.CENTER);
        panelFormularioCompleto.add(panelBotones, BorderLayout.SOUTH);
        panelFormularioCompleto.setBackground(colorFondo);

        panelContenido.add(panelFormularioCompleto, BorderLayout.NORTH);
        panelContenido.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);

        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
    }


    private void agregarComponentesFormulario(JPanel panel, GridBagConstraints gbc) {
        agregarComponente(panel, gbc, new JLabel("Nombre:"), 0, 0);
        agregarComponente(panel, gbc, txtNombre, 1, 0);
        
        agregarComponente(panel, gbc, new JLabel("Apellido:"), 0, 1);
        agregarComponente(panel, gbc, txtApellido, 1, 1);
        
        agregarComponente(panel, gbc, new JLabel("Cédula/RUC:"), 0, 2);
        agregarComponente(panel, gbc, txtCedula, 1, 2);
        
        agregarComponente(panel, gbc, new JLabel("Correo:"), 0, 3);
        agregarComponente(panel, gbc, txtCorreo, 1, 3);
        
        agregarComponente(panel, gbc, new JLabel("Fecha Nac. (YYYY-MM-DD):"), 0, 4);
        agregarComponente(panel, gbc, txtFechaNac, 1, 4);
        
        agregarComponente(panel, gbc, new JLabel("Dirección:"), 0, 5);
        agregarComponente(panel, gbc, txtDireccion, 1, 5);
        
        agregarComponente(panel, gbc, new JLabel("Teléfono:"), 0, 6);
        agregarComponente(panel, gbc, txtTelefono, 1, 6);
        
        agregarComponente(panel, gbc, new JLabel("Es empresa:"), 0, 7);
        agregarComponente(panel, gbc, chkEsEmpresa, 1, 7);
        
        agregarComponente(panel, gbc, new JLabel("RUC (si es empresa):"), 0, 8);
        agregarComponente(panel, gbc, txtRuc, 1, 8);
        
        agregarComponente(panel, gbc, new JLabel("Tipo de cliente:"), 0, 9);
        agregarComponente(panel, gbc, cmbTipoCliente, 1, 9);
    }

    private void configurarTabla(Font fontTitulo, Font fontNormal) {
        modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Apellido", "Cédula/RUC", "Correo", 
                         "Tipo", "Empresa", "Teléfono"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setFont(fontNormal);
        tablaClientes.setRowHeight(25);
        tablaClientes.setShowGrid(true);
        tablaClientes.setGridColor(new Color(220, 220, 220));
        tablaClientes.getTableHeader().setFont(fontTitulo);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarClienteSeleccionado();
            }
        });
    }

    private JTextField crearCampoTexto(int columnas, Font font) {
        JTextField campo = new JTextField(columnas);
        campo.setFont(font);
        return campo;
    }
    
    private JTextField crearCampoNumerico(int maxLength, Font font) {
        JTextField campo = new JTextField(maxLength);
        campo.setFont(font);
        campo.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) return;
                
                String newStr = str.replaceAll("[^0-9]", "");
                if (getLength() + newStr.length() <= maxLength) {
                    super.insertString(offs, newStr, a);
                }
            }
        });
        return campo;
    }
    
    private JTextField crearCampoRUC(Font font) {
        JTextField campo = new JTextField(13);
        campo.setFont(font);
        campo.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) return;
                
                String newStr = str.replaceAll("[^0-9]", "");
                if (getLength() + newStr.length() <= 13) {
                    super.insertString(offs, newStr, a);
                }
            }
        });
        
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (chkEsEmpresa.isSelected()) {
                    String ruc = campo.getText().trim();
                    if (ruc.length() != 13) {
                        mostrarAdvertencia("El RUC debe tener exactamente 13 dígitos");
                        campo.requestFocus();
                    }
                }
            }
        });
        
        return campo;
    }
    
    private JButton crearBoton(String texto, Color colorFondo, Font font) {
        JButton boton = new JButton(texto);
        boton.setFont(font);
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorFondo.darker()),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        return boton;
    }

    
    private void agregarComponente(JPanel panel, GridBagConstraints gbc, Component comp, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(comp, gbc);
    }

    private void cargarClientes() {
        try {
            modeloTabla.setRowCount(0);
            List<Cliente> clientes = clienteServicio.listarClientes();
            
            for (Cliente cliente : clientes) {
                modeloTabla.addRow(new Object[]{
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getNumIdentificacion(),
                    cliente.getCorreo(),
                    cliente.getTipoCliente().toString(),
                    cliente.isEsEmpresa() ? "Sí" : "No",
                    cliente.getTelefono()
                });
            }
            limpiarFormulario();
        } catch (Exception ex) {
            mostrarError("Error al cargar clientes: " + ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al cargar clientes", ex);
        }
    }

    private void cargarClienteSeleccionado() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada != -1) {
            txtNombre.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtApellido.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtCedula.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
            txtCorreo.setText(modeloTabla.getValueAt(filaSeleccionada, 4).toString());
            
            try {
                cmbTipoCliente.setSelectedItem(TipoCliente.valueOf(modeloTabla.getValueAt(filaSeleccionada, 5).toString()));
            } catch (IllegalArgumentException e) {
                cmbTipoCliente.setSelectedItem(TipoCliente.NORMAL);
            }
            
            boolean esEmpresa = modeloTabla.getValueAt(filaSeleccionada, 6).toString().equals("Sí");
            chkEsEmpresa.setSelected(esEmpresa);
            txtRuc.setEnabled(esEmpresa);
            
            txtTelefono.setText(modeloTabla.getValueAt(filaSeleccionada, 7).toString());
        }
    }

    private void agregarCliente(ActionEvent e) {
    try {
        if (!validarCamposObligatorios()) {
            return;
        }
        
        // Validación modificada: para empresas validamos txtRuc, para personas txtCedula
        if (chkEsEmpresa.isSelected()) {
            String ruc = txtRuc.getText().trim();
            if (ruc.length() != 13 || !ruc.matches("^[0-9]{13}$")) {
                mostrarAdvertencia("El RUC debe tener exactamente 13 dígitos numéricos");
                txtRuc.requestFocus();
                return;
            }
        } else {
            String cedula = txtCedula.getText().trim();
            if (cedula.length() != 10 || !cedula.matches("^[0-9]{10}$")) {
                mostrarAdvertencia("La cédula debe tener exactamente 10 dígitos numéricos");
                txtCedula.requestFocus();
                return;
            }
        }
        
        if (!txtCorreo.getText().trim().isEmpty() && !validarCorreo(txtCorreo.getText().trim())) {
            mostrarAdvertencia("Ingrese un correo electrónico válido");
            return;
        }
        
        LocalDate fechaNacimiento = LocalDate.parse(txtFechaNac.getText().trim());
        
        TipoCliente tipoCliente;
        try {
            tipoCliente = (TipoCliente) cmbTipoCliente.getSelectedItem();
        } catch (Exception ex) {
            tipoCliente = TipoCliente.NORMAL;
        }
        
        Cliente nuevoCliente = new Cliente(
            txtNombre.getText().trim(),
            txtApellido.getText().trim(),
            chkEsEmpresa.isSelected() ? txtRuc.getText().trim() : txtCedula.getText().trim(),
            txtCorreo.getText().trim(),
            fechaNacimiento,
            txtDireccion.getText().trim(),
            txtTelefono.getText().trim(),
            chkEsEmpresa.isSelected(),
            chkEsEmpresa.isSelected() ? txtRuc.getText().trim() : null,
            tipoCliente
        );
        
        int resultado = clienteServicio.registrarCliente(nuevoCliente);
        
        manejarResultadoOperacion(resultado);
    } catch (Exception ex) {
        mostrarError("Error en los datos ingresados: " + ex.getMessage());
        LOGGER.log(Level.SEVERE, "Error al agregar cliente", ex);
    }
}
    
    private void manejarResultadoOperacion(int resultado) {
        switch(resultado) {
            case 0:
                mostrarError("Ya existe un cliente con esta cédula/RUC");
                break;
            case 1:
                mostrarExito("Cliente agregado exitosamente");
                cargarClientes();
                break;
            case 2:
                mostrarError("Error al agregar cliente");
                break;
            case 3:
                mostrarAdvertencia("El cliente debe ser mayor de edad");
                break;
            case 4:
                mostrarAdvertencia("El RUC es obligatorio para empresas");
                break;
            case 5:
                mostrarAdvertencia("La cédula/RUC no tiene el formato correcto");
                break;
        }
    }

    private void actualizarCliente(ActionEvent e) {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarAdvertencia("Seleccione un cliente para actualizar");
            return;
        }
        
        try {
            if (!validarCamposObligatorios()) {
                return;
            }
            
            if (chkEsEmpresa.isSelected()) {
                String ruc = txtRuc.getText().trim();
                if (ruc.length() != 13 || !ruc.matches("^[0-9]{13}$")) {
                    mostrarAdvertencia("El RUC debe tener exactamente 13 dígitos numéricos");
                    txtRuc.requestFocus();
                    return;
                }
            }
            
            int id = Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            LocalDate fechaNacimiento = LocalDate.parse(txtFechaNac.getText().trim());
            
            Cliente clienteActualizado = new Cliente(
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                txtCedula.getText().trim(),
                txtCorreo.getText().trim(),
                fechaNacimiento,
                txtDireccion.getText().trim(),
                txtTelefono.getText().trim(),
                chkEsEmpresa.isSelected(),
                chkEsEmpresa.isSelected() ? txtRuc.getText().trim() : null,
                (TipoCliente) cmbTipoCliente.getSelectedItem()
            );
            clienteActualizado.setId(id);
            
            if (clienteServicio.actualizarCliente(clienteActualizado)) {
                mostrarExito("Cliente actualizado exitosamente");
                cargarClientes();
            } else {
                mostrarError("Error al actualizar cliente");
            }
        } catch (Exception ex) {
            mostrarError("Error en los datos ingresados: " + ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al actualizar cliente", ex);
        }
    }

    private void eliminarCliente(ActionEvent e) {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarAdvertencia("Seleccione un cliente para eliminar");
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de eliminar este cliente?", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
                if (clienteServicio.eliminarCliente(id)) {
                    mostrarExito("Cliente eliminado exitosamente");
                    cargarClientes();
                } else {
                    mostrarError("Error al eliminar cliente");
                }
            } catch (Exception ex) {
                mostrarError("Error al eliminar cliente: " + ex.getMessage());
                LOGGER.log(Level.SEVERE, "Error al eliminar cliente", ex);
            }
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtCedula.setText("");
        txtCorreo.setText("");
        txtFechaNac.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtRuc.setText("");
        chkEsEmpresa.setSelected(false);
        cmbTipoCliente.setSelectedIndex(0);
        tablaClientes.clearSelection();
        txtRuc.setEnabled(false);
    }

    private boolean validarCamposObligatorios() {
        if (txtNombre.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty() ||
            txtCedula.getText().trim().isEmpty() || txtFechaNac.getText().trim().isEmpty()) {
            mostrarAdvertencia("Nombre, Apellido, Cédula/RUC y Fecha Nac. son campos obligatorios");
            return false;
        }
        
        if (chkEsEmpresa.isSelected() && txtRuc.getText().trim().isEmpty()) {
            mostrarAdvertencia("El RUC es obligatorio para empresas");
            return false;
        }
        
        return true;
    }
    
    private boolean validarIdentificacion(String identificacion, boolean esEmpresa) {
        String valorLimpio = identificacion.trim().replaceAll("\\s+", "");
        
        if (esEmpresa) {
            return valorLimpio.matches("^[0-9]{13}$");
        } else {
            return valorLimpio.matches("^[0-9]{10}$");
        }
    }
    
    private boolean validarCorreo(String correo) {
        return correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
    
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            SistemaCliente sistema = new SistemaCliente();
            sistema.setVisible(true);
        });
    }
}   
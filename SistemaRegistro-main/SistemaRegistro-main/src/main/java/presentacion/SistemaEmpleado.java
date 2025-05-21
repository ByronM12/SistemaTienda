package presentacion;

import modelo.Empleado;
import modelo.Empleado.RolEmpleado;
import negocio.EmpleadoServicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class SistemaEmpleado extends JFrame {
    
    private final EmpleadoServicio empleadoServicio;
    private JTable tablaEmpleados;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtApellido, txtCorreo, txtFechaNac;
    private JTextField txtFechaIngreso, txtDireccion, txtSalario;
    private JTextField txtUsuario, txtContrasena;
    private JFormattedTextField txtCedula, txtTelefono;
    private JComboBox<RolEmpleado> cmbRol;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;

    public SistemaEmpleado() {
        super("COMPUTEC - Gestión de Empleados");
        empleadoServicio = new EmpleadoServicio();
        configurarInterfaz();
        cargarEmpleados();
        setLocationRelativeTo(null);
    }

    private void configurarInterfaz() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 650);
        setMinimumSize(new Dimension(1000, 550));
        
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
        JLabel lblTitulo = new JLabel("COMPUTEC - Gestión de Empleados");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);

        // Panel de contenido
        JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
        panelContenido.setBackground(colorFondo);

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridLayout(12, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Empleado"));
        panelFormulario.setBackground(colorFondo);
        
        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtCedula = crearCampoNumerico(10); // Cédula con máximo 10 dígitos
        txtCorreo = new JTextField();
        txtFechaNac = new JTextField();
        txtFechaIngreso = new JTextField();
        txtDireccion = new JTextField();
        txtTelefono = crearCampoNumerico(10); // Teléfono con máximo 10 dígitos
        txtSalario = new JTextField();
        txtUsuario = new JTextField();
        txtContrasena = new JTextField();
        cmbRol = new JComboBox<>(RolEmpleado.values());
        cmbRol.setFont(fontNormal);

        // Configurar etiquetas
        Font fontEtiqueta = new Font("Segoe UI", Font.BOLD, 12);
        JLabel[] etiquetas = {
            new JLabel("Nombre:"), new JLabel("Apellido:"), new JLabel("Cédula:"),
            new JLabel("Correo:"), new JLabel("Fecha Nac. (YYYY-MM-DD):"), 
            new JLabel("Rol:"), new JLabel("Fecha Ingreso (YYYY-MM-DD):"),
            new JLabel("Dirección:"), new JLabel("Teléfono:"), 
            new JLabel("Salario:"), new JLabel("Usuario:"), new JLabel("Contraseña:")
        };
        
        for (JLabel etiqueta : etiquetas) {
            etiqueta.setFont(fontEtiqueta);
        }

        panelFormulario.add(etiquetas[0]);
        panelFormulario.add(txtNombre);
        panelFormulario.add(etiquetas[1]);
        panelFormulario.add(txtApellido);
        panelFormulario.add(etiquetas[2]);
        panelFormulario.add(txtCedula);
        panelFormulario.add(etiquetas[3]);
        panelFormulario.add(txtCorreo);
        panelFormulario.add(etiquetas[4]);
        panelFormulario.add(txtFechaNac);
        panelFormulario.add(etiquetas[5]);
        panelFormulario.add(cmbRol);
        panelFormulario.add(etiquetas[6]);
        panelFormulario.add(txtFechaIngreso);
        panelFormulario.add(etiquetas[7]);
        panelFormulario.add(txtDireccion);
        panelFormulario.add(etiquetas[8]);
        panelFormulario.add(txtTelefono);
        panelFormulario.add(etiquetas[9]);
        panelFormulario.add(txtSalario);
        panelFormulario.add(etiquetas[10]);
        panelFormulario.add(txtUsuario);
        panelFormulario.add(etiquetas[11]);
        panelFormulario.add(txtContrasena);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(colorFondo);
        
        btnAgregar = crearBoton("Agregar", new Color(46, 204, 113), fontNormal);
        btnAgregar.addActionListener(this::agregarEmpleado);
        
        btnActualizar = crearBoton("Actualizar", new Color(52, 152, 219), fontNormal);
        btnActualizar.addActionListener(this::actualizarEmpleado);
        
        btnEliminar = crearBoton("Eliminar", new Color(231, 76, 60), fontNormal);
        btnEliminar.addActionListener(this::eliminarEmpleado);
        
        btnLimpiar = crearBoton("Limpiar", new Color(149, 165, 166), fontNormal);
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // Configurar tabla
        modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Apellido", "Cédula", "Rol", 
                         "F. Ingreso", "Salario", "Usuario"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEmpleados = new JTable(modeloTabla);
        tablaEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEmpleados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarEmpleadoSeleccionado();
            }
        });

        // Agregar componentes al panel de contenido
        JPanel panelFormularioCompleto = new JPanel(new BorderLayout());
        panelFormularioCompleto.add(panelFormulario, BorderLayout.CENTER);
        panelFormularioCompleto.add(panelBotones, BorderLayout.SOUTH);
        panelFormularioCompleto.setBackground(colorFondo);

        panelContenido.add(panelFormularioCompleto, BorderLayout.NORTH);
        panelContenido.add(new JScrollPane(tablaEmpleados), BorderLayout.CENTER);

        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
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
    // Método para crear campos que solo aceptan números
    private JFormattedTextField crearCampoNumerico(int maxLength) {
        JFormattedTextField campo = new JFormattedTextField();
        campo.setColumns(10);
        
        // Crear un documento que solo acepte números
        PlainDocument doc = new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) return;
                
                // Solo permitir dígitos
                String newStr = str.replaceAll("[^0-9]", "");
                
                // Verificar que no exceda la longitud máxima
                if (getLength() + newStr.length() <= maxLength) {
                    super.insertString(offs, newStr, a);
                }
            }
        };
        
        campo.setDocument(doc);
        return campo;
    }

    private void cargarEmpleados() {
        modeloTabla.setRowCount(0);
        List<Empleado> empleados = empleadoServicio.listarEmpleados();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (Empleado empleado : empleados) {
            modeloTabla.addRow(new Object[]{
                empleado.getId(),
                empleado.getNombre(),
                empleado.getApellido(),
                empleado.getNumIdentificacion(),
                empleado.getRol().toString(),
                empleado.getFechaIngreso().format(formatter),
                String.format("$%.2f", empleado.getSalario()),
                empleado.getUsuario()
            });
        }
        limpiarFormulario();
    }

    private void agregarEmpleado(ActionEvent e) {
        try {
            // Validar que cédula y teléfono no estén vacíos
            if (txtCedula.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La cédula es obligatoria", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (txtTelefono.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El teléfono es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            LocalDate fechaNacimiento = LocalDate.parse(txtFechaNac.getText().trim());
            LocalDate fechaIngreso = LocalDate.parse(txtFechaIngreso.getText().trim());
            double salario = Double.parseDouble(txtSalario.getText().trim());
            
            Empleado nuevoEmpleado = new Empleado(
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                txtCedula.getText().trim(),
                txtCorreo.getText().trim(),
                fechaNacimiento,
                (RolEmpleado) cmbRol.getSelectedItem(),
                fechaIngreso,
                txtDireccion.getText().trim(),
                txtTelefono.getText().trim(),
                salario,
                txtUsuario.getText().trim(),
                txtContrasena.getText().trim()
            );
            
            int resultado = empleadoServicio.registrarEmpleado(nuevoEmpleado);
            
            switch(resultado) {
                case 0:
                    JOptionPane.showMessageDialog(this, "Ya existe un empleado con esta cédula", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case 1:
                    JOptionPane.showMessageDialog(this, "Empleado agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarEmpleados();
                    break;
                case 2:
                    JOptionPane.showMessageDialog(this, "Error al agregar empleado", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case 3:
                    JOptionPane.showMessageDialog(this, "Ya existe un empleado con este usuario", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case 4:
                    JOptionPane.showMessageDialog(this, "El empleado debe ser mayor de edad", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case 5:
                    JOptionPane.showMessageDialog(this, "Usuario y contraseña son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en los datos ingresados: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarEmpleado(ActionEvent e) {
        int filaSeleccionada = tablaEmpleados.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado para actualizar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Validar que cédula y teléfono no estén vacíos
            if (txtCedula.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La cédula es obligatoria", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (txtTelefono.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El teléfono es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int id = Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            LocalDate fechaNacimiento = LocalDate.parse(txtFechaNac.getText().trim());
            LocalDate fechaIngreso = LocalDate.parse(txtFechaIngreso.getText().trim());
            double salario = Double.parseDouble(txtSalario.getText().trim());
            
            Empleado empleadoActualizado = new Empleado(
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                txtCedula.getText().trim(),
                txtCorreo.getText().trim(),
                fechaNacimiento,
                (RolEmpleado) cmbRol.getSelectedItem(),
                fechaIngreso,
                txtDireccion.getText().trim(),
                txtTelefono.getText().trim(),
                salario,
                txtUsuario.getText().trim(),
                txtContrasena.getText().trim()
            );
            empleadoActualizado.setId(id);
            
            if (empleadoServicio.actualizarEmpleado(empleadoActualizado)) {
                JOptionPane.showMessageDialog(this, "Empleado actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarEmpleados();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar empleado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en los datos ingresados: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEmpleado(ActionEvent e) {
        int filaSeleccionada = tablaEmpleados.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de eliminar este empleado?", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            if (empleadoServicio.eliminarEmpleado(id)) {
                JOptionPane.showMessageDialog(this, "Empleado eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarEmpleados();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar empleado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarEmpleadoSeleccionado() {
        int filaSeleccionada = tablaEmpleados.getSelectedRow();
        if (filaSeleccionada != -1) {
            txtNombre.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtApellido.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtCedula.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
            txtCorreo.setText("");
            cmbRol.setSelectedItem(RolEmpleado.valueOf(modeloTabla.getValueAt(filaSeleccionada, 4).toString()));
            txtFechaIngreso.setText(modeloTabla.getValueAt(filaSeleccionada, 5).toString());
            txtSalario.setText(modeloTabla.getValueAt(filaSeleccionada, 6).toString().replace("$", ""));
            txtTelefono.setText("");
            txtUsuario.setText(modeloTabla.getValueAt(filaSeleccionada, 7).toString());
            txtContrasena.setText("");
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtCedula.setValue(null);
        txtCorreo.setText("");
        txtFechaNac.setText("");
        txtFechaIngreso.setText("");
        txtDireccion.setText("");
        txtTelefono.setValue(null);
        txtSalario.setText("");
        txtUsuario.setText("");
        txtContrasena.setText("");
        cmbRol.setSelectedIndex(0);
        tablaEmpleados.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaEmpleado sistema = new SistemaEmpleado();
            sistema.setVisible(true);
        });
    }
}
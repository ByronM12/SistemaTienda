package presentacion;

import modelo.Producto;
import modelo.Producto.Categoria;
import negocio.ProductoServicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;

public class SistemaProducto extends JFrame {
    
    private final ProductoServicio productoServicio;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtId, txtCodigo, txtNombre, txtMarca, txtPrecio, txtDescripcion, txtStock, txtGarantia;
    private JComboBox<Categoria> cmbCategoria;
    private JButton btnAgregar, btnActualizar, btnEliminar, btnLimpiar;

    public SistemaProducto() {
        super("COMPUTEC - Gestión de Productos");
        productoServicio = new ProductoServicio();
        configurarInterfaz();
        cargarProductos();
        setLocationRelativeTo(null);
    }

    private void configurarInterfaz() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setMinimumSize(new Dimension(1000, 500));
        
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
        JLabel lblTitulo = new JLabel("COMPUTEC - Gestión de Productos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);

        // Panel de contenido
        JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
        panelContenido.setBackground(colorFondo);

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridLayout(9, 2, 10, 10));
        panelFormulario.setBackground(colorFondo);
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));
        
        txtId = new JTextField();
        txtId.setEditable(false);
        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        txtMarca = new JTextField();
        txtPrecio = new JTextField();
        txtDescripcion = new JTextField();
        txtStock = new JTextField();
        txtGarantia = new JTextField();
        cmbCategoria = new JComboBox<>(Categoria.values());
        cmbCategoria.setFont(fontNormal);

        // Configurar componentes
        Font fontEtiqueta = new Font("Segoe UI", Font.BOLD, 12);
        JLabel[] etiquetas = {
            new JLabel("ID:"), new JLabel("Código:"), new JLabel("Nombre:"),
            new JLabel("Marca:"), new JLabel("Precio:"), new JLabel("Descripción:"),
            new JLabel("Stock:"), new JLabel("Garantía (meses):"), new JLabel("Categoría:")
        };
        
        for (JLabel etiqueta : etiquetas) {
            etiqueta.setFont(fontEtiqueta);
        }

        panelFormulario.add(etiquetas[0]);
        panelFormulario.add(txtId);
        panelFormulario.add(etiquetas[1]);
        panelFormulario.add(txtCodigo);
        panelFormulario.add(etiquetas[2]);
        panelFormulario.add(txtNombre);
        panelFormulario.add(etiquetas[3]);
        panelFormulario.add(txtMarca);
        panelFormulario.add(etiquetas[4]);
        panelFormulario.add(txtPrecio);
        panelFormulario.add(etiquetas[5]);
        panelFormulario.add(txtDescripcion);
        panelFormulario.add(etiquetas[6]);
        panelFormulario.add(txtStock);
        panelFormulario.add(etiquetas[7]);
        panelFormulario.add(txtGarantia);
        panelFormulario.add(etiquetas[8]);
        panelFormulario.add(cmbCategoria);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBackground(colorFondo);
        
        btnAgregar = crearBoton("Agregar", new Color(46, 204, 113), fontNormal);
        btnAgregar.addActionListener(this::agregarProducto);
        
        btnActualizar = crearBoton("Actualizar", new Color(52, 152, 219), fontNormal);
        btnActualizar.addActionListener(this::actualizarProducto);
        
        btnEliminar = crearBoton("Eliminar", new Color(231, 76, 60), fontNormal);
        btnEliminar.addActionListener(this::eliminarProducto);
        
        btnLimpiar = crearBoton("Limpiar", new Color(149, 165, 166), fontNormal);
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // Configurar tabla
        modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Código", "Nombre", "Marca", "Precio", 
                         "Categoría", "Stock", "Garantía"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarProductoSeleccionado();
            }
        });

        // Agregar componentes al panel de contenido
        JPanel panelFormularioCompleto = new JPanel(new BorderLayout());
        panelFormularioCompleto.add(panelFormulario, BorderLayout.CENTER);
        panelFormularioCompleto.add(panelBotones, BorderLayout.SOUTH);
        panelFormularioCompleto.setBackground(colorFondo);

        panelContenido.add(panelFormularioCompleto, BorderLayout.NORTH);
        panelContenido.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

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
    private void cargarProductos() {
        modeloTabla.setRowCount(0);
        List<Producto> productos = productoServicio.obtenerTodosProductos();
        
        for (Producto producto : productos) {
            modeloTabla.addRow(new Object[]{
                producto.getId(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getMarca(),
                String.format("%.2f", producto.getPrecio()),
                producto.getCategoria().toString(),
                producto.getStock(),
                producto.getGarantiaMeses() + " meses"
            });
        }
        limpiarFormulario();
    }

    private void agregarProducto(ActionEvent e) {
        try {
            Producto nuevoProducto = new Producto(
                txtCodigo.getText().trim(),
                txtNombre.getText().trim(),
                txtMarca.getText().trim(),
                Float.parseFloat(txtPrecio.getText().trim()),
                txtDescripcion.getText().trim(),
                Integer.parseInt(txtStock.getText().trim()),
                (Categoria) cmbCategoria.getSelectedItem(),
                Integer.parseInt(txtGarantia.getText().trim()),
                LocalDate.now()
            );
            
            int resultado = productoServicio.registrarProducto(nuevoProducto);
            
            switch(resultado) {
                case 0:
                    JOptionPane.showMessageDialog(this, "Ya existe un producto con este código", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case 1:
                    JOptionPane.showMessageDialog(this, "Producto agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarProductos();
                    break;
                case 2:
                    JOptionPane.showMessageDialog(this, "Error al agregar producto", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio, Stock y Garantía deben ser números válidos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarProducto(ActionEvent e) {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para actualizar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            
            Producto productoActualizado = new Producto(
                txtCodigo.getText().trim(),
                txtNombre.getText().trim(),
                txtMarca.getText().trim(),
                Float.parseFloat(txtPrecio.getText().trim()),
                txtDescripcion.getText().trim(),
                Integer.parseInt(txtStock.getText().trim()),
                (Categoria) cmbCategoria.getSelectedItem(),
                Integer.parseInt(txtGarantia.getText().trim()),
                LocalDate.now()
            );
            productoActualizado.setId(id);
            
            if (productoServicio.actualizarProducto(productoActualizado)) {
                JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarProductos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio, Stock y Garantía deben ser números válidos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto(ActionEvent e) {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de eliminar este producto?", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText().trim());
            if (productoServicio.eliminarProducto(id)) {
                JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarProductos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarProductoSeleccionado() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada != -1) {
            txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtCodigo.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtNombre.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtMarca.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
            txtPrecio.setText(modeloTabla.getValueAt(filaSeleccionada, 4).toString());
            cmbCategoria.setSelectedItem(Categoria.valueOf(modeloTabla.getValueAt(filaSeleccionada, 5).toString()));
            txtStock.setText(modeloTabla.getValueAt(filaSeleccionada, 6).toString());
            txtGarantia.setText(modeloTabla.getValueAt(filaSeleccionada, 7).toString().replace(" meses", ""));
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtCodigo.setText("");
        txtNombre.setText("");
        txtMarca.setText("");
        txtPrecio.setText("");
        txtDescripcion.setText("");
        txtStock.setText("");
        txtGarantia.setText("");
        cmbCategoria.setSelectedIndex(0);
        tablaProductos.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaProducto sistema = new SistemaProducto();
            sistema.setVisible(true);
        });
    }
}
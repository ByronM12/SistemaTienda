package presentacion;

import modelo.Cliente;
import modelo.DetalleFactura;
import modelo.Factura;
import modelo.Producto;
import negocio.FacturaServicio;
import negocio.ClienteServicio;
import negocio.ProductoServicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.*;

public class SistemaFactura extends JFrame {
    
    private List<DetalleFactura> detallesFactura = new ArrayList<>();
    private Cliente clienteEncontrado;
    private Producto productoEncontrado;
    private final FacturaServicio facturaServicio;
    private final ClienteServicio clienteServicio;
    private final ProductoServicio productoServicio;
    private DefaultTableModel modeloTabla;
    
    // Componentes de la interfaz
    private JTextField txtCedula, txtCodigo, txtCantidad, txtPrecio;
    private JLabel lblNombreCliente, lblApellidoCliente, lblCedulaCliente, 
                   lblCorreoCliente, lblTelefonoCliente, lblTipoCliente;
    private JLabel lblNombreProducto;
    private JButton btnBuscarCliente, btnBuscarProducto, btnAgregar, btnCrearFactura;
    private JTable tblProductos;
    private JLabel lblSubtotal, lblIVA, lblTotal, lblDescuento;

    public SistemaFactura() {
        super("COMPUTEC - Sistema de Facturación");
        facturaServicio = new FacturaServicio();
        clienteServicio = new ClienteServicio();
        productoServicio = new ProductoServicio();
        configurarInterfaz();
        setLocationRelativeTo(null);
    }

    private void configurarInterfaz() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setMinimumSize(new Dimension(900, 600));
        
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
        JLabel lblTitulo = new JLabel("COMPUTEC - Sistema de Facturación");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);

        // Panel de contenido
        JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
        panelContenido.setBackground(colorFondo);

        // Panel de cliente
        JPanel panelCliente = new JPanel(new BorderLayout());
        panelCliente.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        panelCliente.setBackground(colorFondo);
        
        JPanel panelBusquedaCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusquedaCliente.setBackground(colorFondo);
        panelBusquedaCliente.add(new JLabel("Cédula/RUC:"));
        
        txtCedula = new JTextField(15);
        txtCedula.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) return;
                if (str.matches("\\d+")) {
                    super.insertString(offs, str, a);
                }
            }
        });
        
        panelBusquedaCliente.add(txtCedula);
        btnBuscarCliente = crearBoton("Buscar Cliente", new Color(52, 152, 219), fontNormal);
        btnBuscarCliente.addActionListener(this::buscarCliente);
        panelBusquedaCliente.add(btnBuscarCliente);
        
        JPanel panelInfoCliente = new JPanel(new GridLayout(6, 1, 5, 5));
        panelInfoCliente.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelInfoCliente.setBackground(colorFondo);
        
        lblCedulaCliente = new JLabel("Cédula/RUC: ");
        lblNombreCliente = new JLabel("Nombre: ");
        lblApellidoCliente = new JLabel("Apellido: ");
        lblCorreoCliente = new JLabel("Correo: ");
        lblTelefonoCliente = new JLabel("Teléfono: ");
        lblTipoCliente = new JLabel("Tipo: ");
        
        Font fontEtiqueta = new Font("Segoe UI", Font.BOLD, 12);
        lblCedulaCliente.setFont(fontEtiqueta);
        lblNombreCliente.setFont(fontEtiqueta);
        lblApellidoCliente.setFont(fontEtiqueta);
        lblCorreoCliente.setFont(fontEtiqueta);
        lblTelefonoCliente.setFont(fontEtiqueta);
        lblTipoCliente.setFont(fontEtiqueta);
        
        panelInfoCliente.add(lblCedulaCliente);
        panelInfoCliente.add(lblNombreCliente);
        panelInfoCliente.add(lblApellidoCliente);
        panelInfoCliente.add(lblCorreoCliente);
        panelInfoCliente.add(lblTelefonoCliente);
        panelInfoCliente.add(lblTipoCliente);
        
        panelCliente.add(panelBusquedaCliente, BorderLayout.NORTH);
        panelCliente.add(panelInfoCliente, BorderLayout.CENTER);

        // Panel de producto
        JPanel panelProducto = new JPanel(new BorderLayout());
        panelProducto.setBorder(BorderFactory.createTitledBorder("Agregar Productos"));
        panelProducto.setBackground(colorFondo);
        
        JPanel panelBusquedaProducto = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusquedaProducto.setBackground(colorFondo);
        panelBusquedaProducto.add(new JLabel("Código:"));
        txtCodigo = new JTextField(10);
        panelBusquedaProducto.add(txtCodigo);
        btnBuscarProducto = crearBoton("Buscar Producto", new Color(52, 152, 219), fontNormal);
        btnBuscarProducto.addActionListener(this::buscarProducto);
        panelBusquedaProducto.add(btnBuscarProducto);
        
        JPanel panelDatosProducto = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelDatosProducto.setBackground(colorFondo);
        JLabel lblProducto = new JLabel("Producto: ");
        lblProducto.setFont(fontEtiqueta);
        lblNombreProducto = new JLabel("");
        panelDatosProducto.add(lblProducto);
        panelDatosProducto.add(lblNombreProducto);
        panelDatosProducto.add(new JLabel("Precio:"));
        txtPrecio = new JTextField(10);
        txtPrecio.setEditable(false);
        panelDatosProducto.add(txtPrecio);
        panelDatosProducto.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField(5);
        panelDatosProducto.add(txtCantidad);
        btnAgregar = crearBoton("Agregar", new Color(46, 204, 113), fontNormal);
        btnAgregar.addActionListener(this::agregarProducto);
        panelDatosProducto.add(btnAgregar);
        
        panelProducto.add(panelBusquedaProducto, BorderLayout.NORTH);
        panelProducto.add(panelDatosProducto, BorderLayout.CENTER);

        // Panel de detalles de factura
        JPanel panelDetalles = new JPanel(new BorderLayout());
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Detalles de Factura"));
        panelDetalles.setBackground(colorFondo);
        
        modeloTabla = new DefaultTableModel(
            new Object[]{"Producto", "Código", "Precio", "Cantidad", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblProductos = new JTable(modeloTabla);
        panelDetalles.add(new JScrollPane(tblProductos), BorderLayout.CENTER);

        // Panel de totales
        JPanel panelTotales = new JPanel(new GridLayout(4, 1, 5, 5));
        panelTotales.setBorder(BorderFactory.createTitledBorder("Totales"));
        panelTotales.setBackground(colorFondo);
        
        lblSubtotal = new JLabel("Subtotal: $0.00");
        lblDescuento = new JLabel("Descuento: $0.00");
        lblIVA = new JLabel("IVA (15%): $0.00");
        lblTotal = new JLabel("Total: $0.00");
        
        Font fontTotal = new Font("Segoe UI", Font.BOLD, 14);
        lblSubtotal.setFont(fontTotal);
        lblDescuento.setFont(fontTotal);
        lblIVA.setFont(fontTotal);
        lblTotal.setFont(fontTotal);
        
        panelTotales.add(lblSubtotal);
        panelTotales.add(lblDescuento);
        panelTotales.add(lblIVA);
        panelTotales.add(lblTotal);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(colorFondo);
        btnCrearFactura = crearBoton("Generar Factura", new Color(155, 89, 182), fontNormal);
        btnCrearFactura.addActionListener(this::crearFactura);
        panelBotones.add(btnCrearFactura);

        // Organizar paneles
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1, 10, 10));
        panelSuperior.add(panelCliente);
        panelSuperior.add(panelProducto);
        panelSuperior.setBackground(colorFondo);
        
        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.add(panelDetalles, BorderLayout.CENTER);
        panelCentro.setBackground(colorFondo);
        
        JPanel panelDerecha = new JPanel(new BorderLayout());
        panelDerecha.add(panelTotales, BorderLayout.NORTH);
        panelDerecha.add(panelBotones, BorderLayout.SOUTH);
        panelDerecha.setBackground(colorFondo);
        
        panelCentro.add(panelDerecha, BorderLayout.EAST);
        
        panelContenido.add(panelSuperior, BorderLayout.NORTH);
        panelContenido.add(panelCentro, BorderLayout.CENTER);
        
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
    

    private void buscarCliente(ActionEvent e) {
        String cedula = txtCedula.getText().trim();
        if(cedula.isEmpty()) {
            mostrarMensaje("Ingrese un número de cédula o RUC", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        clienteEncontrado = clienteServicio.buscarPorCedula(cedula);
        if(clienteEncontrado != null) {
            lblCedulaCliente.setText("Cédula/RUC: " + clienteEncontrado.getNumIdentificacion());
            lblNombreCliente.setText("Nombre: " + clienteEncontrado.getNombre());
            lblApellidoCliente.setText("Apellido: " + clienteEncontrado.getApellido());
            lblCorreoCliente.setText("Correo: " + (clienteEncontrado.getCorreo() != null ? clienteEncontrado.getCorreo() : ""));
            lblTelefonoCliente.setText("Teléfono: " + (clienteEncontrado.getTelefono() != null ? clienteEncontrado.getTelefono() : ""));
            lblTipoCliente.setText("Tipo: " + clienteEncontrado.getTipoCliente().toString());
            
            mostrarMensaje("Cliente encontrado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            limpiarCamposCliente();
            mostrarMensaje("No se encontró un cliente con esa cédula/RUC", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarProducto(ActionEvent e) {
        String codigo = txtCodigo.getText().trim();
        if(codigo.isEmpty()) {
            mostrarMensaje("Ingrese un código de producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        productoEncontrado = productoServicio.buscarProductoPorCodigo(codigo);
        if(productoEncontrado != null) {
            lblNombreProducto.setText(productoEncontrado.getNombre() + " (" + productoEncontrado.getMarca() + ")");
            txtPrecio.setText(String.format("%.2f", productoEncontrado.getPrecio()));
            mostrarMensaje("Producto encontrado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            limpiarCamposProducto();
            mostrarMensaje("No se encontró un producto con ese código", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarProducto(ActionEvent e) {
        if(productoEncontrado == null) {
            mostrarMensaje("Primero busque un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if(cantidad <= 0) {
                mostrarMensaje("La cantidad debe ser mayor a cero", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if(cantidad > productoEncontrado.getStock()) {
                mostrarMensaje("No hay suficiente stock. Stock disponible: " + productoEncontrado.getStock(), 
                              "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Verificar si el producto ya está en la lista
            boolean encontrado = false;
            for(DetalleFactura detalle : detallesFactura) {
                if(detalle.getProducto().getId() == productoEncontrado.getId()) {
                    detalle.setCantidad(detalle.getCantidad() + cantidad);
                    encontrado = true;
                    break;
                }
            }
            
            if(!encontrado) {
                DetalleFactura nuevoDetalle = new DetalleFactura(
                    cantidad, productoEncontrado.getPrecio(), productoEncontrado);
                detallesFactura.add(nuevoDetalle);
            }
            
            actualizarTablaDetalles();
            limpiarCamposProducto();
            
        } catch(NumberFormatException ex) {
            mostrarMensaje("La cantidad debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTablaDetalles() {
        modeloTabla.setRowCount(0);
        
        double subtotal = 0;
        
        for(DetalleFactura detalle : detallesFactura) {
            double subTotalDetalle = detalle.getPrecioUnitario() * detalle.getCantidad();
            modeloTabla.addRow(new Object[]{
                detalle.getProducto().getNombre(),
                detalle.getProducto().getCodigo(),
                String.format("%.2f", detalle.getPrecioUnitario()),
                detalle.getCantidad(),
                String.format("%.2f", subTotalDetalle)
            });
            
            subtotal += subTotalDetalle;
        }
        
        // Calcular descuento según tipo de cliente
        double descuento = 0;
        if(clienteEncontrado != null) {
            switch(clienteEncontrado.getTipoCliente()) {
                case FRECUENTE:
                    descuento = subtotal * 0.05; // 5% de descuento
                    break;
                case MAYORISTA:
                    descuento = subtotal * 0.10; // 10% de descuento
                    break;
                default:
                    descuento = 0;
            }
        }
        
        double iva = (subtotal - descuento) * 0.15; // 15% de IVA
        double total = subtotal - descuento + iva;
        
        lblSubtotal.setText(String.format("Subtotal: $%.2f", subtotal));
        lblDescuento.setText(String.format("Descuento: $%.2f", descuento));
        lblIVA.setText(String.format("IVA (15%%): $%.2f", iva));
        lblTotal.setText(String.format("Total: $%.2f", total));
    }

    private void limpiarCamposProducto() {
        txtCodigo.setText("");
        txtCantidad.setText("");
        txtPrecio.setText("");
        lblNombreProducto.setText("");
        productoEncontrado = null;
    }
    
    private void limpiarCamposCliente() {
        lblCedulaCliente.setText("Cédula/RUC: ");
        lblNombreCliente.setText("Nombre: ");
        lblApellidoCliente.setText("Apellido: ");
        lblCorreoCliente.setText("Correo: ");
        lblTelefonoCliente.setText("Teléfono: ");
        lblTipoCliente.setText("Tipo: ");
    }

    private void crearFactura(ActionEvent e) {
        if(clienteEncontrado == null) {
            mostrarMensaje("Primero busque un cliente", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if(detallesFactura.isEmpty()) {
            mostrarMensaje("Agregue al menos un producto a la factura", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de generar esta factura?", 
            "Confirmar Factura", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if(confirmacion == JOptionPane.YES_OPTION) {
            Factura nuevaFactura = new Factura();
            nuevaFactura.setCliente(clienteEncontrado);
            nuevaFactura.setDetalles(detallesFactura);
            
            // Calcular totales
            double subtotal = detallesFactura.stream()
                .mapToDouble(d -> d.getPrecioUnitario() * d.getCantidad())
                .sum();
            
            double descuento = 0;
            switch(clienteEncontrado.getTipoCliente()) {
                case FRECUENTE: descuento = subtotal * 0.05; break;
                case MAYORISTA: descuento = subtotal * 0.10; break;
                default: descuento = 0;
            }
            
            double iva = (subtotal - descuento) * 0.15; // 15% de IVA
            double total = subtotal - descuento + iva;
            
            nuevaFactura.setSubtotal(subtotal);
            nuevaFactura.setDescuento(descuento);
            nuevaFactura.setIva(iva);
            nuevaFactura.setTotal(total);
            
            facturaServicio.registrarFactura(nuevaFactura);
            mostrarMensaje("Factura generada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        }
    }

    private void limpiarFormulario() {
        detallesFactura.clear();
        clienteEncontrado = null;
        productoEncontrado = null;
        modeloTabla.setRowCount(0);
        txtCedula.setText("");
        limpiarCamposCliente();
        limpiarCamposProducto();
        lblSubtotal.setText("Subtotal: $0.00");
        lblDescuento.setText("Descuento: $0.00");
        lblIVA.setText("IVA (15%): $0.00");
        lblTotal.setText("Total: $0.00");
    }

    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaFactura sistema = new SistemaFactura();
            sistema.setVisible(true);
        });
    }
}
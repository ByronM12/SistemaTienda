package presentacion;

import java.awt.*;
import javax.swing.*;
import modelo.Empleado;

public class Main extends JFrame {
    private Empleado empleadoActual;
    private String[] opcionesDisponibles;
    
    public Main(Empleado empleado) {
        this(empleado, new String[]{"Clientes", "Productos", "Facturación", "Empleados", "Salir"});
    }
    
    public Main(Empleado empleado, String[] opciones) {
        this.empleadoActual = empleado;
        this.opcionesDisponibles = opciones;
        configurarInterfaz();
    }
    
    private void configurarInterfaz() {
        setTitle("COMPUTEC - Sistema de Facturación");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal con estilo
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(240, 240, 240));

        // Panel de título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(0, 102, 204));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JLabel lblTitulo = new JLabel("COMPUTEC");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);

        // Panel de bienvenida
        JPanel panelBienvenida = new JPanel();
        panelBienvenida.setBackground(new Color(240, 240, 240));
        String mensajeBienvenida = empleadoActual != null ? 
            "Bienvenido, " + empleadoActual.getNombre() + " " + empleadoActual.getApellido() + 
            " (" + empleadoActual.getRol() + ")" : "Bienvenido al Sistema";
        JLabel lblBienvenida = new JLabel(mensajeBienvenida);
        lblBienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelBienvenida.add(lblBienvenida);

        // Panel de opciones con botones con estilo
        JPanel panelOpciones = new JPanel(new GridLayout(0, 1, 10, 10));
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panelOpciones.setBackground(new Color(240, 240, 240));

        for (String opcion : opcionesDisponibles) {
            JButton btn = crearBotonOpcion(opcion);
            btn.addActionListener(e -> abrirModulo(opcion));
            panelOpciones.add(btn);
        }

        // Agregar componentes
        panelPrincipal.add(panelBienvenida, BorderLayout.CENTER);
        panelPrincipal.add(panelOpciones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }
    
    private JButton crearBotonOpcion(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Asignar colores más claros según la opción
        switch(texto) {
            case "Clientes":
                boton.setBackground(new Color(100, 181, 246)); // Azul más claro
                break;
            case "Productos":
                boton.setBackground(new Color(129, 199, 132)); // Verde más claro
                break;
            case "Facturación":
                boton.setBackground(new Color(186, 104, 200)); // Morado más claro
                break;
            case "Empleados":
                boton.setBackground(new Color(255, 213, 79)); // Amarillo más claro
                break;
            case "Salir":
                boton.setBackground(new Color(239, 154, 154)); // Rojo más claro
                break;
        }
        
        boton.setForeground(Color.WHITE);
        return boton;
    }
    
    private void abrirModulo(String modulo) {
        switch(modulo) {
            case "Clientes":
                new SistemaCliente().setVisible(true);
                break;
            case "Productos":
                new SistemaProducto().setVisible(true);
                break;
            case "Facturación":
                new SistemaFactura().setVisible(true);
                break;
            case "Empleados":
                new SistemaEmpleado().setVisible(true);
                break;
            case "Salir":
                System.exit(0);
                break;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
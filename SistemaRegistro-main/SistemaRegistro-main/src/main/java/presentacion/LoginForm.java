package presentacion;

import modelo.Empleado;
import negocio.EmpleadoServicio;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginForm extends JFrame {
    
    private final EmpleadoServicio empleadoServicio;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    
    public LoginForm() {
        empleadoServicio = new EmpleadoServicio();
        configurarInterfaz();
    }
    
    private void configurarInterfaz() {
        setTitle("COMPUTEC - Inicio de Sesión");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principal con degradado
        JPanel panelPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Color color1 = new Color(0, 102, 204);
                Color color2 = new Color(0, 153, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setOpaque(false);
        JLabel lblTitulo = new JLabel("COMPUTEC");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        
        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridLayout(4, 1, 10, 10));
        panelFormulario.setOpaque(false);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Panel de usuario
        JPanel panelUsuario = new JPanel(new BorderLayout(5, 5));
        panelUsuario.setOpaque(false);
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(200, 30));
        panelUsuario.add(lblUsuario, BorderLayout.NORTH);
        panelUsuario.add(txtUsuario, BorderLayout.CENTER);
        
        // Panel de contraseña
        JPanel panelContrasena = new JPanel(new BorderLayout(5, 5));
        panelContrasena.setOpaque(false);
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setForeground(Color.WHITE);
        lblContrasena.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtContrasena = new JPasswordField();
        txtContrasena.setPreferredSize(new Dimension(200, 30));
        panelContrasena.add(lblContrasena, BorderLayout.NORTH);
        panelContrasena.add(txtContrasena, BorderLayout.CENTER);
        
        // Botón de login con estilo mejorado
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(new Color(46, 125, 50)); // Verde oscuro
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(30, 90, 30)), // Borde exterior
            BorderFactory.createEmptyBorder(10, 0, 10, 0) // Margen interior
        ));
        
        // Efecto hover para el botón
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(56, 142, 60)); // Verde más claro al pasar el mouse
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(46, 125, 50)); // Volver al color original
            }
        });
        
        btnLogin.addActionListener(this::iniciarSesion);
        
        // Agregar componentes al panel de formulario
        panelFormulario.add(panelUsuario);
        panelFormulario.add(panelContrasena);
        panelFormulario.add(btnLogin);
        
        // Agregar componentes al panel principal
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }
    
    private void iniciarSesion(ActionEvent e) {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword()).trim();
        
        if(usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y contraseña son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Empleado empleado = empleadoServicio.validarCredenciales(usuario, contrasena);
        
        if(empleado != null) {
            // Redirigir según el rol del empleado
            switch(empleado.getRol()) {
                case ADMINISTRATIVO:
                    new Main(empleado, new String[]{"Clientes", "Productos", "Facturación", "Empleados", "Salir"}).setVisible(true);
                    break;
                case GERENTE:
                    new Main(empleado, new String[]{"Clientes", "Productos", "Facturación", "Salir"}).setVisible(true);
                    break;
                case VENDEDOR:
                    new Main(empleado, new String[]{"Facturación", "Salir"}).setVisible(true);
                    break;
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            txtContrasena.setText("");
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
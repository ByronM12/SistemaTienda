package negocio;

import java.util.List;
import modelo.Empleado;
import datos.EmpleadoDAO;
import java.time.LocalDate;

public class EmpleadoServicio {
    
    private final EmpleadoDAO empleadoDAO;
    
    public EmpleadoServicio() {
        this.empleadoDAO = new EmpleadoDAO();
    }
    
    public int registrarEmpleado(Empleado empleado) {
        // Validar que el empleado sea mayor de edad
        LocalDate ahora = LocalDate.now();
        int edad = ahora.getYear() - empleado.getFechaNacimiento().getYear();
        if(ahora.getMonthValue() < empleado.getFechaNacimiento().getMonthValue() || 
           (ahora.getMonthValue() == empleado.getFechaNacimiento().getMonthValue() && 
            ahora.getDayOfMonth() < empleado.getFechaNacimiento().getDayOfMonth())) {
            edad--;
        }
        
        if(edad < 18) {
            return 4; // Menor de edad
        }
        
        // Validar campos obligatorios
        if(empleado.getUsuario() == null || empleado.getUsuario().trim().isEmpty() ||
           empleado.getContrasena() == null || empleado.getContrasena().trim().isEmpty()) {
            return 5; // Campos usuario/contrasena vacíos
        }
        
        // Normalizar datos
        empleado.setNombre(empleado.getNombre().toUpperCase());
        empleado.setApellido(empleado.getApellido().toUpperCase());
        
        return empleadoDAO.registrarEmpleado(empleado);
    }
    
    public List<Empleado> listarEmpleados() {
        return empleadoDAO.listarEmpleados();
    }
    
    public boolean eliminarEmpleado(int id) {
        return empleadoDAO.eliminarEmpleado(id);
    }
    
    public boolean actualizarEmpleado(Empleado empleado) {
        // Validaciones
        if(empleado.getNombre() == null || empleado.getNombre().trim().isEmpty() ||
           empleado.getUsuario() == null || empleado.getUsuario().trim().isEmpty() ||
           empleado.getContrasena() == null || empleado.getContrasena().trim().isEmpty()) {
            return false;
        }
        
        // Normalizar datos
        empleado.setNombre(empleado.getNombre().toUpperCase());
        empleado.setApellido(empleado.getApellido().toUpperCase());
        
        return empleadoDAO.actualizarEmpleado(empleado);
    }
    
    public Empleado buscarPorCedula(String cedula) {
        return empleadoDAO.buscarPorCedula(cedula);
    }
    
    public Empleado validarCredenciales(String usuario, String contrasena) {
        return empleadoDAO.validarCredenciales(usuario, contrasena);
    }
}
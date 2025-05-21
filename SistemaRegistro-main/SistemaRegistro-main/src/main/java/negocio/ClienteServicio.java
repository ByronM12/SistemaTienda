package negocio;

import modelo.Cliente;
import datos.ClienteDAO;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class ClienteServicio {
    
    private final ClienteDAO clienteDAO;
    
    public ClienteServicio() {
        this.clienteDAO = new ClienteDAO();
    }
    
    public int registrarCliente(Cliente cliente) {
        try {
            cliente.calcularEdad();
            
            // Validar que el cliente sea mayor de edad si es persona natural
            if(!cliente.isEsEmpresa() && cliente.getEdad() < 18) {
                return 3; // Menor de edad
            }
            
            // Validar RUC si es empresa
            if(cliente.isEsEmpresa() && (cliente.getRuc() == null || cliente.getRuc().trim().isEmpty())) {
                return 4; // RUC faltante
            }
            
            // Validar formato de cédula/RUC
            if(!validarIdentificacion(cliente.getNumIdentificacion(), cliente.isEsEmpresa())) {
                return 5; // Identificación inválida
            }
            
            // Normalizar datos
            cliente.setNombre(cliente.getNombre().toUpperCase().trim());
            cliente.setApellido(cliente.getApellido().toUpperCase().trim());
            
            return clienteDAO.registrarCliente(cliente);
        } catch(Exception ex) {
            ex.printStackTrace();
            return 2; // Error
        }
    }
    
    public List<Cliente> listarClientes() {
        return clienteDAO.listarClientes();
    }
    
    public boolean eliminarCliente(int id) {
        return clienteDAO.eliminarCliente(id);
    }
    
    public boolean actualizarCliente(Cliente cliente) {
        try {
            // Validaciones
            if(cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
                return false;
            }
            
            // Validar RUC si es empresa
            if(cliente.isEsEmpresa() && (cliente.getRuc() == null || cliente.getRuc().trim().isEmpty())) {
                return false;
            }
            
            // Normalizar datos
            cliente.setNombre(cliente.getNombre().toUpperCase().trim());
            cliente.setApellido(cliente.getApellido().toUpperCase().trim());
            
            return clienteDAO.actualizarCliente(cliente);
        } catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public Cliente buscarPorCedula(String cedula) {
        return clienteDAO.buscarPorCedula(cedula);
    }
    
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteDAO.buscarPorNombre(nombre);
    }
    
    private boolean validarIdentificacion(String identificacion, boolean esEmpresa) {
        if(esEmpresa) {
            // Validar RUC (13 dígitos en Ecuador)
            return identificacion != null && identificacion.matches("^[0-9]{13}$");
        } else {
            // Validar cédula (10 dígitos en Ecuador)
            return identificacion != null && identificacion.matches("^[0-9]{10}$");
        }
    }
}
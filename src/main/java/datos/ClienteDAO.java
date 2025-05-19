package datos;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import modelo.Cliente;
import util.PersistenceUtil;

public class ClienteDAO {
    
    public int registrarCliente(Cliente cliente) {
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            // Verificar si ya existe un cliente con la misma cédula/RUC
            Long count = em.createQuery(
                "SELECT COUNT(c) FROM Cliente c WHERE c.numIdentificacion = :cedula", Long.class)
                .setParameter("cedula", cliente.getNumIdentificacion())
                .getSingleResult();
            
            if(count > 0) {
                return 0; // Ya existe
            }
            
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            return 1; // Éxito
        } catch(Exception ex) {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al registrar cliente: " + ex.getMessage());
            return 2; // Error
        } finally {
            em.close();
        }
    }
    
    public List<Cliente> listarClientes() {
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                "SELECT c FROM Cliente c ORDER BY c.apellido, c.nombre", Cliente.class)
                .getResultList();
        } finally {
            em.close();
        }
    }
    
    public boolean eliminarCliente(int id) {
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            Cliente cliente = em.find(Cliente.class, id);
            if(cliente == null) {
                return false;
            }
            
            em.getTransaction().begin();
            em.remove(cliente);
            em.getTransaction().commit();
            return true;
        } catch(Exception ex) {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al eliminar cliente: " + ex.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
    
    public boolean actualizarCliente(Cliente cliente) {
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            Cliente existente = em.find(Cliente.class, cliente.getId());
            if(existente == null) {
                return false;
            }
            
            em.getTransaction().begin();
            existente.setNombre(cliente.getNombre());
            existente.setApellido(cliente.getApellido());
            existente.setNumIdentificacion(cliente.getNumIdentificacion());
            existente.setCorreo(cliente.getCorreo());
            existente.setFechaNacimiento(cliente.getFechaNacimiento());
            existente.setEdad(cliente.getEdad());
            existente.setDireccion(cliente.getDireccion());
            existente.setTelefono(cliente.getTelefono());
            existente.setEsEmpresa(cliente.isEsEmpresa());
            existente.setRuc(cliente.getRuc());
            existente.setTipoCliente(cliente.getTipoCliente());
            em.getTransaction().commit();
            return true;
        } catch(Exception ex) {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al actualizar cliente: " + ex.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
    
    public Cliente buscarPorCedula(String cedula) {
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                "SELECT c FROM Cliente c WHERE c.numIdentificacion = :cedula", Cliente.class)
                .setParameter("cedula", cedula)
                .getSingleResult();
        } catch(NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<Cliente> buscarPorNombre(String nombre) {
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                "SELECT c FROM Cliente c WHERE LOWER(c.nombre) LIKE LOWER(:nombre) " +
                "OR LOWER(c.apellido) LIKE LOWER(:nombre) ORDER BY c.apellido, c.nombre", Cliente.class)
                .setParameter("nombre", "%" + nombre + "%")
                .getResultList();
        } finally {
            em.close();
        }
    }
}
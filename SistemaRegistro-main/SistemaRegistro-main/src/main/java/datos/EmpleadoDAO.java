package datos;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import modelo.Empleado;
import util.PersistenceUtil;

public class EmpleadoDAO {
    
    public int registrarEmpleado(Empleado empleado) {
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            // Verificar si ya existe un empleado con la misma cédula o usuario
            Long countCedula = em.createQuery(
                "SELECT COUNT(e) FROM Empleado e WHERE e.numIdentificacion = :cedula", Long.class)
                .setParameter("cedula", empleado.getNumIdentificacion())
                .getSingleResult();
            
            Long countUsuario = em.createQuery(
                "SELECT COUNT(e) FROM Empleado e WHERE e.usuario = :usuario", Long.class)
                .setParameter("usuario", empleado.getUsuario())
                .getSingleResult();
            
            if(countCedula > 0) {
                return 0; // Ya existe cédula
            }
            
            if(countUsuario > 0) {
                return 3; // Ya existe usuario
            }
            
            em.getTransaction().begin();
            em.persist(empleado);
            em.getTransaction().commit();
            return 1; // Éxito
        } catch(Exception ex) {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al registrar empleado: " + ex.getMessage());
            return 2; // Error
        } finally {
            em.close();
        }
    }
    
    public List<Empleado> listarEmpleados() {
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                "SELECT e FROM Empleado e WHERE e.activo = true ORDER BY e.apellido, e.nombre", Empleado.class)
                .getResultList();
        } finally {
            em.close();
        }
    }
    
    public boolean eliminarEmpleado(int id) {
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            Empleado empleado = em.find(Empleado.class, id);
            if(empleado == null) {
                return false;
            }
            
            em.getTransaction().begin();
            empleado.setActivo(false);
            em.getTransaction().commit();
            return true;
        } catch(Exception ex) {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al eliminar empleado: " + ex.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
    
    public boolean actualizarEmpleado(Empleado empleado) {
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            Empleado existente = em.find(Empleado.class, empleado.getId());
            if(existente == null) {
                return false;
            }
            
            em.getTransaction().begin();
            existente.setNombre(empleado.getNombre());
            existente.setApellido(empleado.getApellido());
            existente.setNumIdentificacion(empleado.getNumIdentificacion());
            existente.setCorreo(empleado.getCorreo());
            existente.setFechaNacimiento(empleado.getFechaNacimiento());
            existente.setRol(empleado.getRol());
            existente.setFechaIngreso(empleado.getFechaIngreso());
            existente.setDireccion(empleado.getDireccion());
            existente.setTelefono(empleado.getTelefono());
            existente.setSalario(empleado.getSalario());
            existente.setUsuario(empleado.getUsuario());
            existente.setContrasena(empleado.getContrasena());
            em.getTransaction().commit();
            return true;
        } catch(Exception ex) {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al actualizar empleado: " + ex.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
    
    public Empleado buscarPorCedula(String cedula) {
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                "SELECT e FROM Empleado e WHERE e.numIdentificacion = :cedula AND e.activo = true", Empleado.class)
                .setParameter("cedula", cedula)
                .getSingleResult();
        } catch(NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public Empleado validarCredenciales(String usuario, String contrasena) {
        EntityManager em = PersistenceUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                "SELECT e FROM Empleado e WHERE e.usuario = :usuario AND e.contrasena = :contrasena AND e.activo = true", 
                Empleado.class)
                .setParameter("usuario", usuario)
                .setParameter("contrasena", contrasena)
                .getSingleResult();
        } catch(NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }
}
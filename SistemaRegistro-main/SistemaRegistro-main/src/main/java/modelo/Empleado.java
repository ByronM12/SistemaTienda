package modelo;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "empleado")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String apellido;
    
    @Column(name = "num_identificacion", nullable = false, unique = true)
    private String numIdentificacion;
    
    @Column(unique = true)
    private String correo;
    
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @Enumerated(EnumType.STRING)
    private RolEmpleado rol;
    
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;
    
    private String direccion;
    
    private String telefono;
    
    @Column(precision = 10, scale = 2)
    private double salario;
    
    private boolean activo;
    
    @Column(nullable = false)
    private String usuario;
    
    @Column(nullable = false)
    private String contrasena;

    public enum RolEmpleado {
        ADMINISTRATIVO, VENDEDOR, GERENTE
    }

    public Empleado() {
    }

    public Empleado(String nombre, String apellido, String numIdentificacion, 
                   String correo, LocalDate fechaNacimiento, RolEmpleado rol, 
                   LocalDate fechaIngreso, String direccion, String telefono, 
                   double salario, String usuario, String contrasena) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.numIdentificacion = numIdentificacion;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.rol = rol;
        this.fechaIngreso = fechaIngreso;
        this.direccion = direccion;
        this.telefono = telefono;
        this.salario = salario;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.activo = true;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNumIdentificacion() {
        return numIdentificacion;
    }

    public void setNumIdentificacion(String numIdentificacion) {
        this.numIdentificacion = numIdentificacion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public RolEmpleado getRol() {
        return rol;
    }

    public void setRol(RolEmpleado rol) {
        this.rol = rol;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
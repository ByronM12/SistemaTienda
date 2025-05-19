package modelo;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cliente")
public class Cliente {
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
    
    private int edad;
    
    private String direccion;
    
    private String telefono;
    
    @Column(name = "es_empresa")
    private boolean esEmpresa;
    
    private String ruc;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente")
    private TipoCliente tipoCliente;
    
    public enum TipoCliente {
        NORMAL, FREQUENTE, MAYORISTA
    }

    public Cliente() {
    }

    public Cliente(String nombre, String apellido, String numIdentificacion, String correo, 
                  LocalDate fechaNacimiento, String direccion, String telefono, 
                  boolean esEmpresa, String ruc, TipoCliente tipoCliente) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.numIdentificacion = numIdentificacion;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.esEmpresa = esEmpresa;
        this.ruc = ruc;
        this.tipoCliente = tipoCliente;
        calcularEdad();
    }

    public void calcularEdad() {
        if (fechaNacimiento != null) {
            LocalDate ahora = LocalDate.now();
            this.edad = ahora.getYear() - fechaNacimiento.getYear();
            if (ahora.getMonthValue() < fechaNacimiento.getMonthValue() || 
                (ahora.getMonthValue() == fechaNacimiento.getMonthValue() && 
                 ahora.getDayOfMonth() < fechaNacimiento.getDayOfMonth())) {
                this.edad--;
            }
        }
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

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
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

    public boolean isEsEmpresa() {
        return esEmpresa;
    }

    public void setEsEmpresa(boolean esEmpresa) {
        this.esEmpresa = esEmpresa;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
}
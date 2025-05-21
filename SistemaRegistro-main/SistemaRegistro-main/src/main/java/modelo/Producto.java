package modelo;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false, unique = true)
    private String codigo;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String marca;
    
    @Column(nullable = false)
    private float precio;
    
    private String descripcion;
    
    @Column(nullable = false)
    private int stock;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;
    
    @Column(name = "garantia_meses")
    private int garantiaMeses;
    
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;
    
    private boolean activo;

    public enum Categoria {
        PC, LAPTOP, COMPONENTE, PERIFERICO, SOFTWARE, OTRO
    }

    public Producto() {
    }

    public Producto(String codigo, String nombre, String marca, float precio, 
                   String descripcion, int stock, Categoria categoria, 
                   int garantiaMeses, LocalDate fechaIngreso) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.descripcion = descripcion;
        this.stock = stock;
        this.categoria = categoria;
        this.garantiaMeses = garantiaMeses;
        this.fechaIngreso = fechaIngreso;
        this.activo = true;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getGarantiaMeses() {
        return garantiaMeses;
    }

    public void setGarantiaMeses(int garantiaMeses) {
        this.garantiaMeses = garantiaMeses;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
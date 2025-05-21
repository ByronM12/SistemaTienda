package negocio;

import java.util.List;
import modelo.Producto;
import datos.ProductoDAO;

public class ProductoServicio {
    
    private final ProductoDAO productoDAO;
    
    public ProductoServicio() {
        this.productoDAO = new ProductoDAO();
    }
    
    public int registrarProducto(Producto producto) {
        // Validaciones
        if(producto.getPrecio() <= 0) {
            return 2; // Precio inválido
        }
        if(producto.getStock() < 0) {
            return 2; // Stock inválido
        }
        
        // Normalizar datos
        producto.setNombre(producto.getNombre().trim());
        producto.setCodigo(producto.getCodigo().trim());
        producto.setMarca(producto.getMarca().trim());
        
        if(producto.getDescripcion() != null) {
            producto.setDescripcion(producto.getDescripcion().trim());
        }
        
        return productoDAO.registrarProducto(producto);
    }
    
    public boolean actualizarProducto(Producto producto) {
        // Validaciones
        if(producto.getPrecio() <= 0 || producto.getStock() < 0) {
            return false;
        }
        
        // Normalizar datos
        producto.setNombre(producto.getNombre().trim());
        producto.setCodigo(producto.getCodigo().trim());
        producto.setMarca(producto.getMarca().trim());
        
        if(producto.getDescripcion() != null) {
            producto.setDescripcion(producto.getDescripcion().trim());
        }
        
        return productoDAO.actualizarProducto(producto);
    }
    
    public boolean eliminarProducto(int id) {
        return productoDAO.eliminarProducto(id);
    }
    
    public List<Producto> obtenerTodosProductos() {
        return productoDAO.obtenerTodosLosProductos();
    }
    
    public List<Producto> buscarProductosPorNombre(String nombre) {
        return productoDAO.buscarPorNombre(nombre.trim());
    }
    
    public Producto buscarProductoPorCodigo(String codigo) {
        return productoDAO.buscarPorCodigo(codigo.trim());
    }
    
    public List<Producto> productosPorCategoria(Producto.Categoria categoria) {
        return productoDAO.productosPorCategoria(categoria);
    }
}
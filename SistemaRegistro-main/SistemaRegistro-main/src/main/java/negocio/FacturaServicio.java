package negocio;

import modelo.Factura;
import datos.FacturaDAO;

public class FacturaServicio {
    private final FacturaDAO facturaDAO;
    
    public FacturaServicio() {
        this.facturaDAO = new FacturaDAO();
    }
    
    public void registrarFactura(Factura factura) {
        facturaDAO.registrarFactura(factura);
    }
}
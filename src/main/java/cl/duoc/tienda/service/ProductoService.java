package cl.duoc.tienda.service;

import cl.duoc.tienda.dto.*;
import cl.duoc.tienda.model.Producto;
import cl.duoc.tienda.repository.ProductoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/** Lógica de negocio, separada del controlador HTTP y del repositorio JPA. */
@Service
public class ProductoService {
    private final ProductoRepository repositorio;
    public ProductoService(ProductoRepository repositorio) { this.repositorio = repositorio; }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        return repositorio.findAll(Sort.by("nombre")).stream().map(ProductoResponse::desde).toList();
    }
    @Transactional(readOnly = true)
    public ProductoResponse buscar(Long id) {
        return ProductoResponse.desde(repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + id)));
    }
    @Transactional
    public ProductoResponse crear(ProductoRequest entrada) {
        if (repositorio.existsBySkuIgnoreCase(entrada.sku())) {
            throw new ConflictoException("SKU ya registrado: " + entrada.sku());
        }
        Producto nuevo = new Producto(entrada.sku(), entrada.nombre(), entrada.categoria(),
                entrada.precio(), entrada.stock());
        return ProductoResponse.desde(repositorio.save(nuevo));
    }
    @Transactional
    public ProductoResponse ajustarStock(Long id, int nuevoStock) {
        Producto producto = repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + id));
        producto.actualizarStock(nuevoStock);
        return ProductoResponse.desde(producto); // JPA guarda el cambio al confirmar la transacción.
    }
}

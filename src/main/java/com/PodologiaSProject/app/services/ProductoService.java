package com.PodologiaSProject.app.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PodologiaSProject.app.Exceptions.ResourceNotFoundException;
import com.PodologiaSProject.app.dao.MovimientoInventarioRepository;
import com.PodologiaSProject.app.dao.ProductoRepository;
import com.PodologiaSProject.app.models.MovimientoInventario;
import com.PodologiaSProject.app.models.Producto;
import com.PodologiaSProject.app.models.TipoMovimientoEnum;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoInventarioRepository  movimientoInventarioRepository;

    public Producto crearProducto(Producto producto) {

        if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (producto.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de producto es obligatorio.");
        }
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        if (producto.getPrecio() != null && producto.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }

        return productoRepository.save(producto);
    }

    public Producto buscarProductoPorId(Integer id) {
        Optional<Producto> productoOptional = productoRepository.findById(id);
        return productoOptional.orElseThrow(()
 -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto actualizarProducto(Producto producto) {

        if (buscarProductoPorId(producto.getId()) == null) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + producto.getId());
        }

        return productoRepository.save(producto);
    }

    public void eliminarProducto(Integer id) {
        if (buscarProductoPorId(id) == null) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
        }

        productoRepository.deleteById(id);
    }

    public void registrarEntradaInventario(Producto producto, int cantidad, String descripcion) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de entrada debe ser positiva.");
        }

        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(TipoMovimientoEnum.ENTRADA);
        movimiento.setCantidad(cantidad);
        movimiento.setDescripcion(descripcion);
        movimientoInventarioRepository.save(movimiento);
    }

    public void registrarSalidaInventario(Producto producto, int cantidad, String descripcion) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de salida debe ser positiva.");
        }
        if (producto.getStock() < cantidad) {
            throw new IllegalArgumentException("No hay suficiente stock para realizar esta salida.");
        }

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(TipoMovimientoEnum.SALIDA);
        movimiento.setCantidad(cantidad);
        movimiento.setDescripcion(descripcion);
        movimientoInventarioRepository.save(movimiento);
    }
}
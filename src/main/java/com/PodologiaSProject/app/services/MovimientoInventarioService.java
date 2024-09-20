package com.PodologiaSProject.app.services;

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
public class MovimientoInventarioService {

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public MovimientoInventario registrarMovimiento(MovimientoInventario movimiento) {
        validarMovimiento(movimiento);

        Producto producto = productoRepository.findById(movimiento.getProducto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + movimiento.getProducto().getId()));

        if (movimiento.getTipoMovimiento() == TipoMovimientoEnum.ENTRADA) {
            producto.setStock(producto.getStock() + movimiento.getCantidad());
        } else if (movimiento.getTipoMovimiento() == TipoMovimientoEnum.SALIDA) {
            if (producto.getStock() < movimiento.getCantidad()) {
                throw new IllegalArgumentException("No hay suficiente stock para realizar esta salida.");
            }
            producto.setStock(producto.getStock() - movimiento.getCantidad());
        }

        productoRepository.save(producto);

        return movimientoInventarioRepository.save(movimiento);
    }

    public MovimientoInventario buscarMovimientoPorId(Integer id) {
        Optional<MovimientoInventario> movimientoOptional = movimientoInventarioRepository.findById(id);
        return movimientoOptional.orElseThrow(() -> new ResourceNotFoundException("Movimiento de inventario no encontrado con ID: " + id));
    }

    public List<MovimientoInventario> listarMovimientos() {
        return movimientoInventarioRepository.findAll();
    }

    public List<MovimientoInventario> buscarMovimientosPorProducto(Producto producto) {
        return movimientoInventarioRepository.findByProducto(producto);
    }


    private void validarMovimiento(MovimientoInventario movimiento) {
        if (movimiento.getProducto() == null) {
            throw new IllegalArgumentException("El producto es obligatorio.");
        }
        if (movimiento.getTipoMovimiento() == null) {
            throw new IllegalArgumentException("El tipo de movimiento es obligatorio.");
        }
        if (movimiento.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva.");
        }
    }
}
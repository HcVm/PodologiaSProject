package com.PodologiaSProject.app.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PodologiaSProject.app.Exceptions.ResourceNotFoundException;
import com.PodologiaSProject.app.dao.PacienteRepository;
import com.PodologiaSProject.app.dao.ProductoRepository;
import com.PodologiaSProject.app.dao.VentaRepository;
import com.PodologiaSProject.app.models.MovimientoInventario;
import com.PodologiaSProject.app.models.Paciente;
import com.PodologiaSProject.app.models.Producto;
import com.PodologiaSProject.app.models.TipoMovimientoEnum;
import com.PodologiaSProject.app.models.Venta;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

	    public Venta crearVenta(Venta venta) {
	        validarVenta(venta);
	
	        Paciente paciente = pacienteRepository.findById(venta.getPaciente().getId())
	                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con ID: " + venta.getPaciente().getId()));
	
	        Producto producto = productoRepository.findById(venta.getProducto().getId())
	                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + venta.getProducto().getId()));
	
	        if (producto.getStock() < venta.getCantidad()) {
	            throw new IllegalArgumentException("No hay suficiente stock del producto para realizar esta venta.");
	        }
	
	        BigDecimal precioTotal = producto.getPrecio().multiply(BigDecimal.valueOf(venta.getCantidad()));
	        venta.setPrecioTotal(precioTotal);
	
	        MovimientoInventario movimiento = new MovimientoInventario();
	        movimiento.setProducto(producto);
	        movimiento.setTipoMovimiento(TipoMovimientoEnum.SALIDA);
	        movimiento.setCantidad(venta.getCantidad());
	        movimiento.setDescripcion("Venta al paciente " + paciente.getNombre() + " " + paciente.getApellido());
	        movimientoInventarioService.registrarMovimiento(movimiento);
	
	        return ventaRepository.save(venta);
	    }

    public Venta buscarVentaPorId(Integer id) {
        Optional<Venta> ventaOptional = ventaRepository.findById(id);
        return ventaOptional.orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con ID: " + id));
    }
    
    public BigDecimal calcularTotalIngresosPorVentas(LocalDate fechaInicio, LocalDate fechaFin) {

        if (fechaInicio != null && fechaFin != null) {
            return ventaRepository.calcularTotalIngresosPorVentasEntreFechas(fechaInicio.atStartOfDay(), fechaFin.atTime(LocalTime.MAX));
        } else {
            return ventaRepository.calcularTotalIngresosPorVentas();
        }
    }

    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    public List<Venta> buscarVentasPorPaciente(Paciente paciente) {
        return ventaRepository.findByIdPaciente(paciente.getId());
    }

    public List<Venta> buscarVentasPorProducto(Producto producto) {
        return ventaRepository.findByIdProducto(producto.getId());
    }

    private void validarVenta(Venta venta) {
        if (venta.getPaciente() == null) {
            throw new IllegalArgumentException("El paciente es obligatorio.");
        }
        if (venta.getProducto() == null) {
            throw new IllegalArgumentException("El producto es obligatorio.");
        }
        if (venta.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva.");
        }
    }
}
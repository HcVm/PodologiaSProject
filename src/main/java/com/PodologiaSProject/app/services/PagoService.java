package com.PodologiaSProject.app.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PodologiaSProject.app.Exceptions.ResourceNotFoundException;
import com.PodologiaSProject.app.dao.CitaRepository;
import com.PodologiaSProject.app.dao.PagoRepository;
import com.PodologiaSProject.app.models.Cita;
import com.PodologiaSProject.app.models.Pago;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private CitaRepository citaRepository;

    public Pago crearPago(Pago pago) {
        validarPago(pago);
        citaRepository.findById(pago.getCita().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + pago.getCita().getId()));

        return pagoRepository.save(pago);
    }

    public Pago buscarPagoPorId(Integer id) {
        Optional<Pago> pagoOptional = pagoRepository.findById(id);
        return pagoOptional.orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con ID: " + id));
    }

    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }
    
    public BigDecimal calcularTotalIngresosPorCitas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return pagoRepository.calcularTotalIngresosPorCitasEntreFechas(fechaInicio.atStartOfDay(), fechaFin.atTime(LocalTime.MAX));
        } else {
            return pagoRepository.calcularTotalIngresosPorCitas();
        }
    }

    public Pago actualizarPago(Pago pago) {
        validarPago(pago);

        if (buscarPagoPorId(pago.getId()) == null) {
            throw new ResourceNotFoundException("Pago no encontrado con ID: " + pago.getId());
        }

        return pagoRepository.save(pago);
    }

    public void eliminarPago(Integer id) {
        if (buscarPagoPorId(id) == null) {
            throw new ResourceNotFoundException("Pago no encontrado con ID: " + id);
        }

        pagoRepository.deleteById(id);
    }

    public List<Pago> buscarPagosPorCita(Cita cita) {
        return pagoRepository.findByIdCita(cita.getId());
    }


    private void validarPago(Pago pago) {
        if (pago.getCita() == null) {
            throw new IllegalArgumentException("La cita es obligatoria.");
        }
        if (pago.getMonto() == null || pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser positivo.");
        }

    }
}
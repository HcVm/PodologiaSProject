package com.PodologiaSProject.app.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PodologiaSProject.app.Exceptions.ResourceNotFoundException;
import com.PodologiaSProject.app.dao.TratamientoRepository;
import com.PodologiaSProject.app.models.Tratamiento;

@Service
public class TratamientoService {

    @Autowired
    private TratamientoRepository tratamientoRepository;

    public Tratamiento crearTratamiento(Tratamiento tratamiento) {
        validarTratamiento(tratamiento);
        return tratamientoRepository.save(tratamiento);
    }

    public Tratamiento buscarTratamientoPorId(Integer id) {
        Optional<Tratamiento> tratamientoOptional = tratamientoRepository.findById(id);
        return tratamientoOptional.orElseThrow(() -> new ResourceNotFoundException("Tratamiento no encontrado con ID: " + id));
    }

    public List<Tratamiento> listarTratamientos() {
        return tratamientoRepository.findAll();
    }

    public Tratamiento actualizarTratamiento(Tratamiento tratamiento) {
        validarTratamiento(tratamiento);

        if (buscarTratamientoPorId(tratamiento.getId()) == null) {
            throw new ResourceNotFoundException("Tratamiento no encontrado con ID: " + tratamiento.getId());
        }

        return tratamientoRepository.save(tratamiento);
    }

    public void eliminarTratamiento(Integer id) {
        if (buscarTratamientoPorId(id) == null) {
            throw new ResourceNotFoundException("Tratamiento no encontrado con ID: " + id);
        }

        tratamientoRepository.deleteById(id);
    }

    public List<Tratamiento> buscarTratamientosPorNombre(String nombre) {
        return tratamientoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    private void validarTratamiento(Tratamiento tratamiento) {
        if (tratamiento.getNombre() == null || tratamiento.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del tratamiento es obligatorio.");
        }
        if (tratamiento.getCosto() == null || tratamiento.getCosto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El costo del tratamiento debe ser positivo.");
        }
    }
}
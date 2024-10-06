package com.PodologiaSProject.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PodologiaSProject.app.Exceptions.ResourceNotFoundException;
import com.PodologiaSProject.app.dao.PacienteRepository;
import com.PodologiaSProject.app.models.Paciente;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository  pacienteRepository;

    public Paciente crearPaciente(Paciente paciente){
        if (paciente.getNombre() == null || paciente.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del paciente es obligatorio.");
        }
        if (paciente.getApellido() == null || paciente.getApellido().isEmpty()) {
            throw new IllegalArgumentException("El apellido del paciente es obligatorio.");
        }
        if (paciente.getDni() == null || paciente.getDni().isEmpty()) {
            throw new IllegalArgumentException("El DNI del paciente es obligatorio.");
        }

        if (pacienteRepository.findByDni(paciente.getDni()) != null) {
            throw new IllegalArgumentException("Ya existe un paciente con ese DNI.");
        }

        return pacienteRepository.save(paciente);
    }

    public Paciente buscarPacientePorId(Integer id) { 
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);
        return pacienteOptional.orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con ID: " + id));
    }

    public List<Paciente> buscarPacientesPorNombre(String nombre) {
        return pacienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }
    
    public Paciente buscarPacientePorDni(String dni) {
        return pacienteRepository.findByDni(dni);
    }

    public Paciente actualizarPaciente(Paciente paciente) {

        if (buscarPacientePorId(paciente.getId()) == null) {
            throw new ResourceNotFoundException("Paciente no encontrado con ID: " + paciente.getId());
        }

        Paciente pacienteExistenteConDni = pacienteRepository.findByDni(paciente.getDni());
        if (pacienteExistenteConDni != null && !pacienteExistenteConDni.getId().equals(paciente.getId())) {
            throw new IllegalArgumentException("Ya existe un paciente con ese DNI.");
        }

        return pacienteRepository.save(paciente);
    }

    public void eliminarPaciente(Integer id) { 
        if (buscarPacientePorId(id) == null) {
            throw new ResourceNotFoundException("Paciente no encontrado con ID: " + id);
        }

        pacienteRepository.deleteById(id);
    }
}
package com.PodologiaSProject.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PodologiaSProject.app.Exceptions.ResourceNotFoundException;
import com.PodologiaSProject.app.dao.HistorialMedicoRepository;
import com.PodologiaSProject.app.models.HistorialMedico;

@Service
public class HistorialMedicoService {

    @Autowired
    private HistorialMedicoRepository historialMedicoRepository;
    

    public HistorialMedico crearHistorialMedico(HistorialMedico historialMedico) {  //La creación de un método tiene la siguiente estructura ( public o private - Entidad - Nombre del método - (parámetros))
        validarHistorialMedico(historialMedico);  //Consumo de un método dentro de otro método del mismo servicio
        return historialMedicoRepository.save(historialMedico);
    }
    
    private void validarHistorialMedico(HistorialMedico historialMedico) {
        if (historialMedico.getPaciente() == null) {
            throw new IllegalArgumentException("El paciente es obligatorio.");
        }
    }

    public HistorialMedico buscarHistorialMedicoPorId(Integer id) {
        Optional<HistorialMedico> historialMedicoOptional = historialMedicoRepository.findById(id);
        return historialMedicoOptional.orElseThrow(() -> new ResourceNotFoundException("Historial médico no encontrado con ID: " + id));
    }

    public List<HistorialMedico> listarHistorialesMedicos() {
        return historialMedicoRepository.findAll();
    }

    public HistorialMedico actualizarHistorialMedico(HistorialMedico historialMedico) {
        validarHistorialMedico(historialMedico);

        if (buscarHistorialMedicoPorId(historialMedico.getId()) == null) {
            throw new ResourceNotFoundException("Historial médico no encontrado con ID: " + historialMedico.getId());
        }

        return historialMedicoRepository.save(historialMedico);
    }

    public void eliminarHistorialMedico(Integer id) {
        if (buscarHistorialMedicoPorId(id) == null) {
            throw new ResourceNotFoundException("Historial médico no encontrado con ID: " + id);
        }
        historialMedicoRepository.deleteById(id);
    }


    public List<HistorialMedico> buscarHistorialesMedicosPorPaciente(Integer pacienteId) {
        return historialMedicoRepository.findByIdPaciente(pacienteId);
    }

    public HistorialMedico buscarHistorialMedicoPorAtencion(Integer atencionId) {
        return historialMedicoRepository.findByIdAtencion(atencionId);
    }
}
package com.PodologiaSProject.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PodologiaSProject.app.Exceptions.ResourceNotFoundException;
import com.PodologiaSProject.app.dao.AtencionRepository;
import com.PodologiaSProject.app.dao.HistorialMedicoRepository;
import com.PodologiaSProject.app.models.Atencion;
import com.PodologiaSProject.app.models.Cita;
import com.PodologiaSProject.app.models.EstadoCitaEnum;
import com.PodologiaSProject.app.models.HistorialMedico;

@Service
public class AtencionService  {

    @Autowired
    private AtencionRepository atencionRepository;

    @Autowired
    private HistorialMedicoRepository historialMedicoRepository;

    public Atencion crearAtencion(Atencion atencion) {
        validarAtencion(atencion);

        if (atencion.getCita().getEstado() == EstadoCitaEnum.ATENDIDA) {
            throw new IllegalArgumentException("La cita ya ha sido atendida.");
        }
        Atencion atencionGuardada = atencionRepository.save(atencion);
        Cita cita = atencion.getCita();
        cita.setEstado(EstadoCitaEnum.ATENDIDA);

        HistorialMedico historialMedico = new HistorialMedico();
        historialMedico.setPaciente(cita.getPaciente());
        historialMedico.setAtencion(atencionGuardada);
        historialMedicoRepository.save(historialMedico);

        return atencionGuardada;
    }

    public Atencion buscarAtencionPorId(Integer id) {
        Optional<Atencion> atencionOptional = atencionRepository.findById(id);
        return atencionOptional.orElseThrow(() -> new ResourceNotFoundException("Atención no encontrada con ID: " + id));
    }

    public List<Atencion> listarAtenciones() {
        return atencionRepository.findAll();
    }

    public Atencion actualizarAtencion(Atencion atencion) {
        validarAtencion(atencion);

        if (buscarAtencionPorId(atencion.getId()) == null) {
            throw new ResourceNotFoundException("Atención no encontrada con ID: " + atencion.getId());
        }

        HistorialMedico historialMedico = historialMedicoRepository.findByIdAtencion(atencion.getId());
        if (historialMedico != null) {
            historialMedicoRepository.save(historialMedico);
        }

        return atencionRepository.save(atencion);
    }

    public void eliminarAtencion(Integer id) {
        if (buscarAtencionPorId(id) == null) {
            throw new ResourceNotFoundException("Atención no encontrada con ID: " + id);
        }

        HistorialMedico historialMedico = historialMedicoRepository.findByIdAtencion(id);
        if (historialMedico != null) {
            historialMedicoRepository.delete(historialMedico);
        }

        atencionRepository.deleteById(id);
    }


    private void validarAtencion(Atencion atencion) {
        if (atencion.getCita() == null) {
            throw new IllegalArgumentException("La cita es obligatoria.");
        }
        if (atencion.getTratamiento() == null) {
            throw new IllegalArgumentException("El tratamiento es obligatorio.");
        }
    }
}
package com.PodologiaSProject.app.services;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PodologiaSProject.app.Exceptions.ResourceNotFoundException;
import com.PodologiaSProject.app.dao.DisponibilidadRepository;
import com.PodologiaSProject.app.models.DiaSemanaEnum;
import com.PodologiaSProject.app.models.Disponibilidad;
import com.PodologiaSProject.app.models.Empleado;

@Service
public class DisponibilidadService {

    @Autowired
    private DisponibilidadRepository disponibilidadRepository;

    public Disponibilidad crearDisponibilidad(Disponibilidad disponibilidad) {
        validarDisponibilidad(disponibilidad);

        if (disponibilidadRepository.existsByIdPodologoAndDiaSemanaAndHoraInicioAndHoraFin(
                disponibilidad.getPodologo().getId(),
                disponibilidad.getDiaSemana(),
                disponibilidad.getHoraInicio(),
                disponibilidad.getHoraFin())) {
            throw new IllegalArgumentException("Ya existe una disponibilidad registrada para el podólogo en ese horario.");
        }

        return disponibilidadRepository.save(disponibilidad);
    }

    public Disponibilidad buscarDisponibilidadPorId(Integer id) {
        Optional<Disponibilidad> disponibilidadOptional = disponibilidadRepository.findById(id);
        return disponibilidadOptional.orElseThrow(() -> new ResourceNotFoundException("Disponibilidad no encontrada con ID: " + id));
    }

    public List<Disponibilidad> listarDisponibilidades() {
        return disponibilidadRepository.findAll();
    }

    public Disponibilidad actualizarDisponibilidad(Disponibilidad disponibilidad) {
        validarDisponibilidad(disponibilidad);

        if (buscarDisponibilidadPorId(disponibilidad.getId()) == null) {
            throw new ResourceNotFoundException("Disponibilidad no encontrada con ID: " + disponibilidad.getId());
        }

        if (disponibilidadRepository.existsByIdPodologoAndDiaSemanaAndHoraInicioAndHoraFinAndIdNot(
                disponibilidad.getPodologo().getId(),
                disponibilidad.getDiaSemana(),
                disponibilidad.getHoraInicio(),
                disponibilidad.getHoraFin(),
                disponibilidad.getId())) {
            throw new IllegalArgumentException("Ya existe una disponibilidad registrada para el podólogo en ese horario.");
        }

        return disponibilidadRepository.save(disponibilidad);
    }

    public void eliminarDisponibilidad(Integer id) {
        if (buscarDisponibilidadPorId(id) == null) {
            throw new ResourceNotFoundException("Disponibilidad no encontrada con ID: " + id);
        }

        disponibilidadRepository.deleteById(id);
    }
    
    public List<Disponibilidad> buscarDisponibilidadesPorPodologo(Empleado podologo) {
        return disponibilidadRepository.findByIdPodologo(podologo.getId());
    }

    public List<Disponibilidad> buscarDisponibilidadesPorDiaSemana(DayOfWeek diaSemana) {
        DiaSemanaEnum diaSemanaEnum = convertirDayOfWeekADiaSemanaEnum(diaSemana);
        return disponibilidadRepository.findByDiaSemana(diaSemanaEnum);
    }

    public List<Disponibilidad> buscarDisponibilidadesPorPodologoYDiaSemana(Empleado podologo, DayOfWeek diaSemana) {
        DiaSemanaEnum diaSemanaEnum = convertirDayOfWeekADiaSemanaEnum(diaSemana);
        return disponibilidadRepository.findByIdPodologoAndDiaSemana(podologo.getId(), diaSemanaEnum);
    }

    private DiaSemanaEnum convertirDayOfWeekADiaSemanaEnum(DayOfWeek diaOfWeek) {
        switch (diaOfWeek) {
            case MONDAY:
                return DiaSemanaEnum.LUNES;
            case TUESDAY:
                return DiaSemanaEnum.MARTES;
            case WEDNESDAY:
                return DiaSemanaEnum.MIERCOLES;
            case THURSDAY:
                return DiaSemanaEnum.JUEVES;
            case FRIDAY:
                return DiaSemanaEnum.VIERNES;
            case SATURDAY:
                return DiaSemanaEnum.SABADO;
            case SUNDAY:
                return DiaSemanaEnum.DOMINGO;
            default:
                throw new IllegalArgumentException("Día de la semana no válido: " + diaOfWeek);
        }
    }

    private void validarDisponibilidad(Disponibilidad disponibilidad) {
        if (disponibilidad.getPodologo() == null) {
            throw new IllegalArgumentException("El podólogo es obligatorio.");
        }
        if (disponibilidad.getDiaSemana() == null) {
            throw new IllegalArgumentException("El día de la semana es obligatorio.");
        }
        if (disponibilidad.getHoraInicio() == null) {
            throw new IllegalArgumentException("La hora de inicio es obligatoria.");
        }
        if (disponibilidad.getHoraFin() == null) {
            throw new IllegalArgumentException("La hora de fin es obligatoria.");
        }
        if (disponibilidad.getHoraInicio().isAfter(disponibilidad.getHoraFin()) ||
                disponibilidad.getHoraInicio().equals(disponibilidad.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin.");
        }
    }
}
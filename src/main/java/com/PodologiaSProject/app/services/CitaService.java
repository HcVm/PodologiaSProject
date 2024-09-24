package com.PodologiaSProject.app.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PodologiaSProject.app.Exceptions.ResourceNotFoundException;
import com.PodologiaSProject.app.dao.CitaRepository;
import com.PodologiaSProject.app.models.Cita;
import com.PodologiaSProject.app.models.Disponibilidad;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private DisponibilidadService disponibilidadService;

    @Autowired
    private NotificacionService notificacionService;

    public Cita crearCita(Cita cita) {
        validarCita(cita);
        LocalDate fechaCita = cita.getFechaHora().toLocalDate();
        DayOfWeek diaSemana = fechaCita.getDayOfWeek();
        LocalTime horaInicio = cita.getFechaHora().toLocalTime();
        LocalTime horaFin = horaInicio.plusMinutes(30);

        List<Disponibilidad> disponibilidades = disponibilidadService.buscarDisponibilidadesPorPodologoYDiaSemana(
                cita.getPodologo(), diaSemana);

        if (!isHorarioDisponible(horaInicio, horaFin, disponibilidades)) {
            throw new IllegalArgumentException("El podólogo no está disponible en ese horario.");
        }

        if (citaRepository.existsByIdPodologoAndFechaHora(cita.getPodologo().getId(), cita.getFechaHora())) {
            throw new IllegalArgumentException("Ya existe una cita programada para el podólogo en ese horario.");
        }

        Cita citaGuardada = citaRepository.save(cita);

        enviarNotificacionCitaProgramada(citaGuardada);

        return citaGuardada;
    }

    public Cita buscarCitaPorId(Integer id) {
        Optional<Cita> citaOptional = citaRepository.findById(id);
        return citaOptional.orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));
    }
    
    public List<Object[]> obtenerCitasPorPodologo(LocalDate fechaInicio, LocalDate fechaFin) {
        
        if (fechaInicio != null && fechaFin != null) {
            return citaRepository.findCitasPorPodologoEntreFechas(fechaInicio.atStartOfDay(), fechaFin.atTime(LocalTime.MAX));
        } else {
            return citaRepository.findCitasPorPodologo();
        }
    }

    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    public Cita actualizarCita(Cita cita) {
        validarCita(cita);

        Cita citaExistente = buscarCitaPorId(cita.getId());

        if (!cita.getFechaHora().equals(citaExistente.getFechaHora())) {
            LocalDate fechaCita = cita.getFechaHora().toLocalDate();
            DayOfWeek diaSemana = fechaCita.getDayOfWeek();
            LocalTime horaInicio = cita.getFechaHora().toLocalTime();
            LocalTime horaFin = horaInicio.plusMinutes(30);

            List<Disponibilidad> disponibilidades = disponibilidadService.buscarDisponibilidadesPorPodologoYDiaSemana(
                    cita.getPodologo(), diaSemana);

            if (!isHorarioDisponible(horaInicio, horaFin, disponibilidades)) {
                throw new IllegalArgumentException("El podólogo no está disponible en el nuevo horario.");
            }

            if (citaRepository.existsByIdPodologoAndFechaHora(cita.getPodologo().getId(), cita.getFechaHora())) {
                throw new IllegalArgumentException("Ya existe una cita programada para el podólogo en ese horario.");
            }

            enviarNotificacionCitaActualizada(cita);
        }

        return citaRepository.save(cita);
    }

    public void eliminarCita(Integer id) {
        Cita cita = buscarCitaPorId(id);

        enviarNotificacionCitaCancelada(cita);

        citaRepository.deleteById(id);
    }


    private void validarCita(Cita cita) {
        if (cita.getPaciente() == null) {
            throw new IllegalArgumentException("El paciente es obligatorio.");
        }
        if (cita.getPodologo() == null) {
            throw new IllegalArgumentException("El podólogo es obligatorio.");
        }
        if (cita.getFechaHora() == null) {
            throw new IllegalArgumentException("La fecha y hora de la cita son obligatorias.");
        }
        if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha y hora de la cita no pueden ser en el pasado.");
        }
    }

    private boolean isHorarioDisponible(LocalTime horaInicio, LocalTime horaFin, List<Disponibilidad> disponibilidades) {
        return disponibilidades.stream()
                .anyMatch(d -> horaInicio.isAfter(d.getHoraInicio()) && horaFin.isBefore(d.getHoraFin()));
    }

    private void enviarNotificacionCitaProgramada(Cita cita) {
        String mensaje = "Su cita ha sido programada para el " + cita.getFechaHora() + ".";
        enviarNotificacionPorEmail(cita.getPaciente().getEmail(), mensaje);
        enviarNotificacionPorSms(cita.getPaciente().getTelefono(), mensaje);
    }

    private void enviarNotificacionCitaActualizada(Cita cita) {
        String mensaje = "Su cita ha sido actualizada para el " + cita.getFechaHora() + ".";
        enviarNotificacionPorEmail(cita.getPaciente().getEmail(), mensaje);
        enviarNotificacionPorSms(cita.getPaciente().getTelefono(), mensaje);
    }

    private void enviarNotificacionCitaCancelada(Cita cita) {
        String mensaje = "Su cita para el " + cita.getFechaHora() + " ha sido cancelada.";
        enviarNotificacionPorEmail(cita.getPaciente().getEmail(), mensaje);
        enviarNotificacionPorSms(cita.getPaciente().getTelefono(), mensaje);
    }

    private void enviarNotificacionPorEmail(String email, String mensaje) {
        // Falta implementar la lógica para enviar un correo electrónico al paciente
        System.out.println("Enviando correo electrónico a " + email + ": " + mensaje); 
    }

    private void enviarNotificacionPorSms(String telefono, String mensaje) {
    	// Falta implementar la lógica para enviar un mensaje de texto al paciente
        System.out.println("Enviando SMS a " + telefono + ": " + mensaje); 
    }
}
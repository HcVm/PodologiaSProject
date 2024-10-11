package com.PodologiaSProject.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PodologiaSProject.app.Exceptions.ResourceNotFoundException;
import com.PodologiaSProject.app.dao.NotificacionRepository;
import com.PodologiaSProject.app.models.EstadoNotificacion;
import com.PodologiaSProject.app.models.Notificacion;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private EmailServiceImpl emailService;
    
    public void enviarNotificacion(Notificacion notificacion) {
        notificacionRepository.save(notificacion);


        if (notificacion.getUsuario() != null && notificacion.getUsuario().getEmpleado() != null 
                && notificacion.getUsuario().getEmpleado().getEmail() != null) {
            emailService.enviarEmail(notificacion.getUsuario().getEmpleado().getEmail(), "Notificación de la clínica", notificacion.getMensaje());
        }

        // Enviar notificación por SMS si el paciente tiene un número de teléfono
        if (notificacion.getUsuario() != null && notificacion.getUsuario().getEmpleado() != null 
                && notificacion.getUsuario().getEmpleado().getTelefono() != null) {
            // smsService.enviarSms(notificacion.getUsuario().getEmpleado().getTelefono(), notificacion.getMensaje());
        }
    }

    public List<Notificacion> obtenerNotificacionesPorUsuario(Integer usuarioId) {
        return notificacionRepository.findByIdUsuarioOrderByFechaHoraDesc(usuarioId);
    }

    public void marcarNotificacionComoLeida(Integer notificacionId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con ID: " + notificacionId));

        notificacion.setEstado(EstadoNotificacion.LEIDA);
        notificacionRepository.save(notificacion);
    }

    public List<Notificacion> obtenerNotificacionesNoLeidasPorUsuario(Integer usuarioId) {
        return notificacionRepository.findByIdUsuarioAndEstadoOrderByFechaHoraDesc(usuarioId, EstadoNotificacion.NO_LEIDA);
    }

    public void eliminarNotificacion(Integer notificacionId) {
        notificacionRepository.deleteById(notificacionId);
    }
}
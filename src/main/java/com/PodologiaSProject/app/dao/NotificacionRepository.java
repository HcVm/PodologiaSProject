package com.PodologiaSProject.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.EstadoNotificacion;
import com.PodologiaSProject.app.models.Notificacion;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer>{
	
	List<Notificacion> findByIdUsuarioOrderByFechaHoraDesc(Integer idUsuario);

    List<Notificacion> findByIdUsuarioAndEstadoOrderByFechaHoraDesc(Integer idUsuario, EstadoNotificacion estado);
}


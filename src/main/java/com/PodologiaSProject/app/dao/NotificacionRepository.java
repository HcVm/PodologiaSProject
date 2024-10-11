package com.PodologiaSProject.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.EstadoNotificacion;
import com.PodologiaSProject.app.models.Notificacion;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer>{

    @Query("SELECT n FROM Notificacion n WHERE n.usuario.id = :idUsuario ORDER BY n.fechaHora DESC")
    List<Notificacion> findByIdUsuarioOrderByFechaHoraDesc(@Param("idUsuario") Integer idUsuario);

    @Query("SELECT n FROM Notificacion n WHERE n.usuario.id = :idUsuario AND n.estado = :estado ORDER BY n.fechaHora DESC")
    List<Notificacion> findByIdUsuarioAndEstadoOrderByFechaHoraDesc(@Param("idUsuario") Integer idUsuario, @Param("estado") EstadoNotificacion estado);
}
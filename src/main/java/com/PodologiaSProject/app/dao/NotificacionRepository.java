package com.PodologiaSProject.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.Notificacion;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer>{
}


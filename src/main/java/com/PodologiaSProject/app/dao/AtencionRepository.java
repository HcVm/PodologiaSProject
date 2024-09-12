package com.PodologiaSProject.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.Atencion;

@Repository
public interface AtencionRepository extends JpaRepository<Atencion, Integer>{

}

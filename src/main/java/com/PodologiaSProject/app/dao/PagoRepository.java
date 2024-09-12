package com.PodologiaSProject.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer>{

}

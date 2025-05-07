package com.example.demo.Repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.Sorteo;

@Repository
public interface SorteoRepository extends JpaRepository<Sorteo, Long> {

    List<Sorteo> findAllByOrderByFechaDesc();

    List<Sorteo> findByNumero(Integer numero);

    List<Sorteo> findByFecha(LocalDate fecha);
}

package com.example.demo.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Sorteo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer numero;

    @Column(nullable = false)
    private Double montoAGanar;

    @Column(nullable = false)
    private LocalDate fecha;

    private Integer numeroGanador;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Double getMontoAGanar() {
        return this.montoAGanar;
    }

    public void setMontoAGanar(Double montoAGanar) {
        this.montoAGanar = montoAGanar;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getNumeroGanador() {
        return this.numeroGanador;
    }

    public void setNumeroGanador(Integer numeroGanador) {
        this.numeroGanador = numeroGanador;
    }
    
}

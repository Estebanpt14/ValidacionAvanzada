package com.example.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Model.Sorteo;
import com.example.demo.Repositories.SorteoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SorteoService {

    @Autowired
    private SorteoRepository repository;

    public List<Sorteo> listarTodos() {
        return repository.findAllByOrderByFechaDesc();
    }

    public List<Sorteo> buscarPorNumero(Integer numero) {
        return repository.findByNumero(numero);
    }

    public List<Sorteo> buscarPorFecha(LocalDate fecha) {
        return repository.findByFecha(fecha);
    }

    public Sorteo crear(Sorteo sorteo) {
        return repository.save(sorteo);
    }

    public Optional<Sorteo> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public Sorteo actualizar(Long id, Sorteo nuevoSorteo) {
        return repository.findById(id).map(sorteo -> {
            sorteo.setNumero(nuevoSorteo.getNumero());
            sorteo.setMontoAGanar(nuevoSorteo.getMontoAGanar());
            sorteo.setFecha(nuevoSorteo.getFecha());
            sorteo.setNumeroGanador(nuevoSorteo.getNumeroGanador());
            return repository.save(sorteo);
        }).orElseThrow(() -> new RuntimeException("Sorteo no encontrado"));
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
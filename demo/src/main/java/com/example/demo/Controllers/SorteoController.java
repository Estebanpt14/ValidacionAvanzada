package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Model.Sorteo;
import com.example.demo.Services.SorteoService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sorteos")
@CrossOrigin(origins = "*") // Habilita CORS para Angular
public class SorteoController {

    @Autowired
    private SorteoService service;

    @GetMapping
    public List<Sorteo> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/filtrar")
    public List<Sorteo> filtrar(
            @RequestParam(required = false) Integer numero,
            @RequestParam(required = false) String fecha) {
        
        if (numero != null) {
            return service.buscarPorNumero(numero);
        } else if (fecha != null) {
            return service.buscarPorFecha(LocalDate.parse(fecha));
        } else {
            return service.listarTodos();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sorteo> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sorteo> crear(@RequestBody Sorteo sorteo) {
        Sorteo nuevo = service.crear(sorteo);
        return ResponseEntity.ok(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sorteo> actualizar(@PathVariable Long id, @RequestBody Sorteo sorteo) {
        return ResponseEntity.ok(service.actualizar(id, sorteo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
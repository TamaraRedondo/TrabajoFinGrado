package com.tfg.project.tfg_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "objetivo")
public class Objetivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ejercicios_realizados")
    private int ejerciciosRealizados;

    @Column(name = "rutinas_realizadas")
    private int rutinasRealizadas;
    
    @Column(name = "horas_totales")
    private double horasTotales;

    @Column(name = "puntos_obtenidos")
    private int puntosObtenidos;

    @JoinColumn(name = "cliente_id")
    private Integer clienteId;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getEjerciciosRealizados() {
        return ejerciciosRealizados;
    }

    public void setEjerciciosRealizados(int ejerciciosRealizados) {
        this.ejerciciosRealizados = ejerciciosRealizados;
    }

    public int getRutinasRealizadas() {
        return rutinasRealizadas;
    }

    public void setRutinasRealizadas(int rutinasRealizadas) {
        this.rutinasRealizadas = rutinasRealizadas;
    }
    
    public double getHorasTotales() {
        return horasTotales;
    }

    public void setHorasTotales(double horasTotales) {
        this.horasTotales = horasTotales;
    }

    public int getPuntosObtenidos() {
        return puntosObtenidos;
    }

    public void setPuntosObtenidos(int puntosObtenidos) {
        this.puntosObtenidos = puntosObtenidos;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }
}
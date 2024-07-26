package com.tfg.project.tfg_backend.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ejercicio")
public class Ejercicio {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    @Column(length = 6000, columnDefinition = "JSON") 
    private String datos;
    
    private boolean realizado;
    
    @JoinColumn(name = "cliente_id")
	Integer clienteId;
    
    @ManyToOne
    @JoinColumn(name = "rutina_id")
    private Rutina rutina;
    
    private Integer orden;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRealizado; // Nueva columna para almacenar la fecha de realización del ejercicio
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return datos;
    }

    public void setDescripcion(String descripcion) {
        this.datos = descripcion;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Rutina getRutina() {
        return rutina;
    }

    public void setRutina(Rutina rutina) {
        this.rutina = rutina;
    }
    
    public void agregarLineaDescripcion(String linea) {
        if (datos == null) {
            datos = "";
        }
        datos += linea + "\n";
    }
    
    public boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(boolean realizado) {
        this.realizado = realizado;
    }
    
    public Integer getOrgen() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }


}

package com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
public class Autor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer nacimiento;
    private Integer fallecimiento;

    public Autor() {}
    public Autor(String nombre, Integer nacimiento, Integer fallecimiento) {
        this.nombre = nombre;
        this.nacimiento = nacimiento;
        this.fallecimiento = fallecimiento;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Integer getNacimiento() { return nacimiento; }
    public Integer getFallecimiento() { return fallecimiento; }
}

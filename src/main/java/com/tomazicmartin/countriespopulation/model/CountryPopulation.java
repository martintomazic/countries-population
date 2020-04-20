/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tomazicmartin.countriespopulation.model;

/**
 *
 * @author martin
 */
public class CountryPopulation {
    private String id;
    private String name;
    private Long population;

    public CountryPopulation() {
    }

    public CountryPopulation(String id, String name, Long population) {
        this.id = id;
        this.name = name;
        this.population = population;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }
    
    
}

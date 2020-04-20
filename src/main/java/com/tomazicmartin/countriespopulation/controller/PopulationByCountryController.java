/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tomazicmartin.countriespopulation.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomazicmartin.countriespopulation.model.CountryPopulation;
import com.tomazicmartin.countriespopulation.model.CountryPopulationRaw;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author martin
 */
@RestController
@RequestMapping("/country/all/population")
public class PopulationByCountryController {

    private static final String DATA_URL = "https://api.worldbank.org/v2/country/all/indicator/SP.POP.TOTL?date=2018&format=json&per_page=500";

    @GetMapping("/highest")
    public List<CountryPopulation> getThreeMostPopulatedCountries() {
        return getDataFromWorldBank().stream()
                .filter(x -> x.getPopulation() != null)
                .sorted(Comparator.comparing(CountryPopulation::getPopulation).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    @GetMapping("/lowest")
    public List<CountryPopulation> getThreeLeastPopulatedCountries() {
        return getDataFromWorldBank().stream()
                .filter(x -> x.getPopulation() != null)
                .sorted(Comparator.comparing(CountryPopulation::getPopulation))
                .limit(3)
                .collect(Collectors.toList());
    }

    private List<CountryPopulation> getDataFromWorldBank() {
        RestTemplate restTemplate = new RestTemplate();
        // JSON response is of type: [Object1, [NestedObject2]] -> not posible to deserialize directly into pojo class
        Object[] rawResponse = restTemplate.getForObject(DATA_URL, Object[].class);

        if (rawResponse == null) {
            throw new RuntimeException("Worldbank is currently not available.");
        }

        ObjectMapper mapper = new ObjectMapper();
        // map data only from the second object of rawResponse to List<CountryPopulationRaw>. The first object only contains general info about request.
        List<CountryPopulationRaw> countryPopulationRaw = mapper.convertValue(rawResponse[1], // rawResponse = [Object1, [NestedObject2]]
                new TypeReference<List<CountryPopulationRaw>>() {
        });

        // we get rid of all the uneccessary data
        return countryPopulationRaw.stream()
                .map(raw -> new CountryPopulation(raw.getCountry().getId(), raw.getCountry().getValue(), raw.getValue()))
                .collect(Collectors.toList());
    }
}

/* example input json
[{
        "page": 1,
        "pages": 6,
        "per_page": 50,
        "total": 264,
        "sourceid": "2",

        "lastupdated": "2020-04-09"
    }, [{
            "indicator": {
                "id": "SP.POP.TOTL",
                "value": "Population, total"
            },
            "country": {
                "id": "1A",
                "value": "Arab World"
            },
            "countryiso3code": "ARB",
            "date": "2018",
            "value": 419790588,
            "unit": "",
            "obs_status": "",
            "decimal": 0
        }
    ]]
 */

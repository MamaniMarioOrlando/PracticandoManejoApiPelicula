package com.aluracursos.screenmach.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosSerie(
        //la notacion @JsonAlias solo sirve para lectura los datos dela api
        //la notacion @Jsonproperyti sirve para lectura y escritura
        @JsonAlias("Title") String titulo,
        @JsonAlias("totalSeasons") Integer totalDeTemporadas,
        @JsonAlias("imdbRating") String evaluacion,
        @JsonAlias("Actors") String actores,
        @JsonAlias("Genre") String genero,
        @JsonAlias("Plot") String sinopsis,
        @JsonAlias("Poster") String poster

        ) {
}
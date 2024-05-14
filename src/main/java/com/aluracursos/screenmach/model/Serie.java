package com.aluracursos.screenmach.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;
import java.util.OptionalDouble;
@Entity
@Table(name ="series")

public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_serie", nullable = false)
    private Long id_serie;
    @Column(unique = true)
    private String titulo;
    private Integer totalDeTemporadas;
    private Double evaluacion;
    private String actores;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String sinopsis;
    private String poster;
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios;

    public Serie() {
    }

    public Long getId_serie() {
        return id_serie;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    public Serie(DatosSerie datoSerie){
        this.titulo = datoSerie.titulo();
        this.totalDeTemporadas = datoSerie.totalDeTemporadas();
        this.evaluacion = OptionalDouble.of(Double.valueOf(datoSerie.evaluacion())).orElse(0);
        this.actores = datoSerie.actores();
        this.genero = Categoria.fromString(datoSerie.genero().split(",")[0].trim());
        this.sinopsis = datoSerie.sinopsis();
        this.poster = datoSerie.poster();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalDeTemporadas() {
        return totalDeTemporadas;
    }

    public void setTotalDeTemporadas(Integer totalDeTemporadas) {
        this.totalDeTemporadas = totalDeTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public String toString() {
        return
                "Actores= '" + actores + '\'' +
                        " titulo= '" + titulo + '\'' +
                ", totalDeTemporadas= " + totalDeTemporadas +
                ", evaluacion= " + evaluacion +

                ", genero = " + genero +
                ", sinopsis= '" + sinopsis + '\'' +
                ", poster= '" + poster +
                 ", episodio= '" + this.episodios +'\'';
    }
}

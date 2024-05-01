package com.aluracursos.screenmach.Principal;

import com.aluracursos.screenmach.model.DatosEpisodio;
import com.aluracursos.screenmach.model.DatosSerie;
import com.aluracursos.screenmach.model.DatosTemporada;
import com.aluracursos.screenmach.model.Episodio;
import com.aluracursos.screenmach.service.ConsumoAPI;
import com.aluracursos.screenmach.service.ConvierteDato;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leer = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String KEY = "&apikey=2ca7e64c";

    private ConvierteDato conversor = new ConvierteDato();
    public void mostrarMenu(){
        System.out.println("Ingrese el nombre de la serie que desea buscar: ");
        //Busca los datos generales de la serie
        var nombreSerie = leer.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE+nombreSerie.replace(" ","+")+KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);

        System.out.println(datos);

        List<DatosTemporada> temporadas = new ArrayList<>();
        //Busca los datos de las tempo
        for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
            json = consumoApi.obtenerDatos(URL_BASE+nombreSerie.replace(" ","+")+"&Season="+i+KEY);
            var datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
            temporadas.add((datosTemporada));
        }
        //temporadas.forEach(System.out::println);
        /*for (int i = 0; i < datos.totalDeTemporadas(); i++) {
            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
            for (int j = 0; j < episodiosTemporada.size(); j++) {
                System.out.println(episodiosTemporada.get(j).titulo());
            }
        }*/
        //temporadas.forEach(t -> t.episodios().forEach( e -> System.out.println(e.titulo())) );

        //Convertir todas las informaciones en una lista del tipo de datoEpisodio
        /*List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());
        //mostrar los mejores 5 episodios
        System.out.println("TOP DE LOS MEJORES 5:");
        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .limit(5)
                .forEach(System.out::println);*/

        //Convirtiendo los datos a un tipo Episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream().map(d -> new Episodio(t.numeroTemporada(),d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        //Seleccionar series de acuerdo a un año determinado
        /*System.out.println("ingrese un año de busqueda: ");
        var anio = leer.nextInt();
        leer.nextLine();

        LocalDate fechaBusqueda = LocalDate.of(anio,1,1);
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
                .forEach(e -> System.out.println(
                        "Episodio: "+e.getNumeroEpisodio() +
                        "Temporada: "+e.getTemporada()+
                        "Fecha de lanzamiento: "+ e.getFechaDeLanzamiento().format(formatoFecha)
                ));*/

        /*System.out.println("Ingrese el titulo del episodio: ");
        var tituloIngresado = leer.nextLine();
        Optional<Episodio> episodioEncontrado = episodios.parallelStream()
                .filter(e -> e.getTitulo().toUpperCase().contains(tituloIngresado.toUpperCase()))
                .peek(System.out::println)
                .findFirst();
        if(episodioEncontrado.isPresent()){
            System.out.println("Los datos del episodio son:");
            System.out.println(episodioEncontrado.get());
        }else{
            System.out.println("El episodio no existe!!");
        }*/

        //obtener evaluacion por temporada
        /*Map<Integer,Double> evaluacionPorTemporada = episodios.stream()
                .filter(e -> e.getEvaluacion() >0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(evaluacionPorTemporada);*/

        //obtener las estadisticas de los episodios
        DoubleSummaryStatistics doubleSummaryStatics = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));

        System.out.println(doubleSummaryStatics);

    }
}

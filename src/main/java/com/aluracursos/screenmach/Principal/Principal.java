package com.aluracursos.screenmach.Principal;

import com.aluracursos.screenmach.model.*;
import com.aluracursos.screenmach.repository.ISerieRepository;
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
    private final String KEY = System.getenv("KEY_API");

    private ConvierteDato conversor = new ConvierteDato();

    private List<DatosSerie> datosSeries = new ArrayList<>();
    private ISerieRepository serieRepository;

    List<Serie> series = new ArrayList<>();
    public  Principal(ISerieRepository serieRepository){
        this.serieRepository = serieRepository;
    }
    public void mostrarMenuPrincipal(){
        int opcion = -1;
        System.out.println("********* Bienvenido a PelisMax ********");
        var menu = """
                    
                    
                    1- Buscar serie
                    2- Buscar episodios
                    3- Mostrar información
                    0- salir
                """;
        while(opcion != 0){
            System.out.println(menu);
            System.out.println("ingrese una opcion: ");
            opcion = leer.nextInt();
            leer.nextLine();
            switch (opcion){
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarEpisodios();
                    break;
            }
        }
    }

    private DatosSerie getDatosSerie(){
        System.out.println("Ingrese el nombre de la serie: ");
        String nombreSerie = leer.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE+nombreSerie.replace(" ","+")+KEY);
        System.out.println(json);
        DatosSerie datosSerie = conversor.obtenerDatos(json, DatosSerie.class);
        datosSeries.add(datosSerie);
        return datosSerie;
    }

    private void buscarEpisodioPorSerie(){
        /*DatosSerie datosSerie = getDatosSerie();
        List<DatosTemporada> temporadas = new ArrayList<>();

        for(int i=1; i < datosSerie.totalDeTemporadas(); i++){
            var json = consumoApi.obtenerDatos(URL_BASE+datosSerie.titulo().replace(" ","+")+"&season="+i+KEY);
            DatosTemporada datosTemporada = conversor.obtenerDatos(json,DatosTemporada.class);
            temporadas.add(datosTemporada);
        }
        temporadas.forEach(System.out::println);*/
        mostrarEpisodios();
        System.out.println("Ingrese el nombre de la serie: ");
        var nombreSerie = leer.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();
        if(serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DatosTemporada> temporadas = new ArrayList<>();

            for(int i=1; i < serieEncontrada.getTotalDeTemporadas(); i++){
                var json = consumoApi.obtenerDatos(URL_BASE+serieEncontrada.getTitulo().replace(" ","+")+"&season="+i+KEY);
                DatosTemporada datosTemporada = conversor.obtenerDatos(json,DatosTemporada.class);
                temporadas.add(datosTemporada);
            }
            //convertimos la lista de temporadas en lista de Episodios
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numeroTemporada(),e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            serieRepository.save(serieEncontrada);

        }

    }
    private void buscarSerieWeb(){
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        serieRepository.save(serie);
        System.out.println(datos);
    }
    private void mostrarEpisodios(){
        /*List<Serie> series = new ArrayList<>();
        series = datosSeries.stream()
                .map(s -> new Serie(s))
                .collect(Collectors.toList());*/
        //traemos los datos con JPArepository
        series = serieRepository.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

    }
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

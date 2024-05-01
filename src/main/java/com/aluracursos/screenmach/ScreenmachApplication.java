package com.aluracursos.screenmach;

import com.aluracursos.screenmach.Principal.EjemploStreams;
import com.aluracursos.screenmach.Principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmachApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmachApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/*var consumoApi = new ConsumoAPI();
		var json = consumoApi.obtenerDatos("http://www.omdbapi.com/?t=game+of+thrones&apikey=2ca7e64c");

        System.out.println(json);

        ConvierteDato conversor = new ConvierteDato();
        var datos = conversor.obtenerDatos(json, DatosSerie.class);

        System.out.println(datos);

        json = consumoApi.obtenerDatos("http://www.omdbapi.com/?t=game+of+thrones&Season=1&episode=1&apikey=2ca7e64c");
        DatosEpisodio episodio = conversor.obtenerDatos(json, DatosEpisodio.class);

        System.out.println(episodio);*/

        Principal menuPrincipal = new Principal();

        menuPrincipal.mostrarMenu();

		//EjemploStreams ejemplo = new EjemploStreams();
		//jemplo.mostrarEjemplo();



    }
}

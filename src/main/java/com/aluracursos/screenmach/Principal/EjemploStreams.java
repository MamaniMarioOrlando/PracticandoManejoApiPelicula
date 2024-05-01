package com.aluracursos.screenmach.Principal;

import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void mostrarEjemplo(){
        List<String> nombres = Arrays.asList("Wilson","Carlos","Pedro","Genesis");
        //Streams me permite hacer varias operaciones en cadenas
        //Por ejemplo ordenar los elementos del array con el metodo sorted()
        nombres.stream()
                .sorted()
                .limit(2)
                .filter(n -> n.startsWith("G"))
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }
}

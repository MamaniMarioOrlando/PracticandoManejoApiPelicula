package com.aluracursos.screenmach.model;

public enum Categoria {
    ACCION("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIMENE("Crime");

     private String categoriaOmdb;
    Categoria(String categoriaOmdb){
        this.categoriaOmdb=categoriaOmdb;
    }

    public static Categoria fromString(String text){
        for(Categoria categoria : Categoria.values()){
            if(categoria.categoriaOmdb.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("No se encontro una categoria: "+text);
    }
}

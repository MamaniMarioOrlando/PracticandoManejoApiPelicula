package com.aluracursos.screenmach.repository;

import com.aluracursos.screenmach.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISerieRepository extends JpaRepository<Serie,Long> {
}

package com.example.ex8.repository;

import com.example.ex8.entity.Note;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

  @EntityGraph(attributePaths = {"writer"}, type = EntityGraph.EntityGraphType.LOAD)
  @Query("select n from Note n where n.num=:num ")
  Optional<Note> readNote(Long num);

  @EntityGraph(attributePaths = {"writer"}, type = EntityGraph.EntityGraphType.LOAD)
  @Query("select n from Note n where n.writer.eamil=:email ")
  List<Note> getNoteWithWriter(String email);

}

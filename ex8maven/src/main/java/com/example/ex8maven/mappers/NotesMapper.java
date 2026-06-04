package com.example.ex8maven.mappers;

import com.example.ex8maven.entity.Note;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@Mapper
@MapperScan(basePackages = {"com.example.ex8maven.mappers"})
public interface NotesMapper {
  List<Note> getNote(String email);

  int insertNote(Note note);

  int updateNote(Note note);

  int deleteNote(String email);

  List<Note> getAllNotes();
}

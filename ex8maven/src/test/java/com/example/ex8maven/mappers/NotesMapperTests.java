package com.example.ex8maven.mappers;

import com.example.ex8maven.entity.ClubMember;
import com.example.ex8maven.entity.Note;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotesMapperTests {

  @Autowired
  NotesMapper notesMapper;

  @Test
  public void getNoteWithEmail() {
    List<Note> list = notesMapper.getNote("user10@example.com");
    list.forEach(System.out::println);
  }

  @Test
  public void insertTest() {
    Note note = Note.builder()
        .title("mybatis test")
        .content("content test")
        .writer(ClubMember.builder().email("user10@example.com").build())
        .build();
    notesMapper.insertNote(note);
  }
}
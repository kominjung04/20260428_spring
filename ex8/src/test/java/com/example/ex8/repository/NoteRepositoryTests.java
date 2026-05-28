package com.example.ex8.repository;

import com.example.ex8.entity.ClubMember;
import com.example.ex8.entity.Note;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NoteRepositoryTests {
  @Autowired
  NoteRepository noteRepository;

  @Test
  @Transactional
  @Commit
  void insertDummies() {
    IntStream.rangeClosed(1, 100).forEach(i -> {
      Note note = Note.builder()
          .title("title" + i)
          .content("content" + i)
          .writer(ClubMember.builder().email("user"+i+"@example.com").build())
          .build();
      noteRepository.save(note);
    });
  }

}
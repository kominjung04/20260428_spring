package com.example.ex8.service;

import com.example.ex8.dto.NoteDTO;
import com.example.ex8.entity.Note;
import com.example.ex8.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
  private final NoteRepository noteRepository;

  @Override
  public Long register(NoteDTO noteDTO) {
    return noteRepository.save(DTOtoEntity(noteDTO)).getNum();
  }

  @Override
  public NoteDTO getNote(Long num) {
    Optional<Note> result = noteRepository.getNoteWithNum(num);
    return result.isPresent()?EntityToDTO(result.get()):null;
  }

  @Override
  public void modify(NoteDTO noteDTO) {
    Optional<Note> result = noteRepository.getNoteWithNum(noteDTO.getNum());
    if(result.isPresent()) {
      Note note = result.get();
      note.changeTitle(noteDTO.getTitle());
      note.changeContent(noteDTO.getContent());
      noteRepository.save(note);
    }
  }

  @Override
  public void remove(NoteDTO noteDTO) {
    noteRepository.deleteById(noteDTO.getNum());
  }

  @Override
  public List<NoteDTO> getAllWithWriter(String writerEmail) {
    List<Note> list = noteRepository.getNoteWithWriter(writerEmail);
    return list.stream().map(this::EntityToDTO).toList();
  }

}

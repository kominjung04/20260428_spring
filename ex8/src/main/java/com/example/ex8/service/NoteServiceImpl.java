package com.example.ex8.service;

import com.example.ex8.dto.NoteDTO;
import com.example.ex8.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
  private final NoteRepository noteRepository;

  @Override
  public Long register(NoteDTO noteDTO) {
    return 0L;
  }

  @Override
  public NoteDTO getNote(Long num) {
    return null;
  }

  @Override
  public void modify(NoteDTO noteDTO) {

  }

  @Override
  public void remove(NoteDTO noteDTO) {

  }
}

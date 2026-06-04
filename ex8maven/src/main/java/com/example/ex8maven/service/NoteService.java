package com.example.ex8maven.service;

import com.example.ex8maven.dto.NoteDTO;
import com.example.ex8maven.entity.ClubMember;
import com.example.ex8maven.entity.Note;

import java.util.List;

public interface NoteService {

  Long register(NoteDTO noteDTO);

  NoteDTO get(Long num);

  void modify(NoteDTO noteDTO);

  void remove(NoteDTO noteDTO);

  List<NoteDTO> getAllWithWriter(String writerEmail);

  default NoteDTO entityToDto(Note note) {
    NoteDTO dto = NoteDTO.builder()
        .num(note.getNum())
        .title(note.getTitle())
        .content(note.getContent())
        .writerEmail(note.getWriter().getEmail())
        .regDate(note.getRegDate())
        .modDate(note.getModDate())
        .build();
    return dto;
  }

  default Note dtoToEntity(NoteDTO dto) {
    Note note = Note.builder()
        .num(dto.getNum())
        .title(dto.getTitle())
        .content(dto.getContent())
        .writer(ClubMember.builder().email(dto.getWriterEmail()).build())
        .build();
    return note;
  }

}

package com.example.ex8.service;

import com.example.ex8.dto.NoteDTO;
import com.example.ex8.entity.ClubMember;
import com.example.ex8.entity.Note;

import java.util.List;

public interface NoteService {

  Long register(NoteDTO noteDTO);

  NoteDTO getNote(Long num);

  void modify(NoteDTO noteDTO);

  void remove(NoteDTO noteDTO);

  List<NoteDTO> getAllWithWriter(String writerEmail);

  default NoteDTO EntityToDTO(Note note) {
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

  default Note DTOtoEntity(NoteDTO dto) {
    Note note = Note.builder()
        .num(dto.getNum())
        .title(dto.getTitle())
        .content(dto.getContent())
        .writer(ClubMember.builder().email(dto.getWriterEmail()).build())
        .build();
    return note;
  }

}

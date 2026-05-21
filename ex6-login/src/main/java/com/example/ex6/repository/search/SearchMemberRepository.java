package com.example.ex6.repository.search;

import com.example.ex6.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchMemberRepository {
  Page<Member> searchPage(String type, String keyword, Pageable pageable);
}

package com.example.ex7.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ClubMember extends BasicEntity {

  @Id
  private String email;

  private String password;
  private String name;
  private boolean fromSocial;

  @ElementCollection(fetch = FetchType.LAZY)
  @Builder.Default
  private Set<ClubMemberRole> roleSet = new HashSet<>();

  public void addMemberRole(ClubMemberRole role) {
    roleSet.add(role);
  }

  public void changePassword(String newPassword) {
    this.password = newPassword;
  }
}

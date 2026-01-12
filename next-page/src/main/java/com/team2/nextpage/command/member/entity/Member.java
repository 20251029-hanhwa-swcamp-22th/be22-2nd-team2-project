package com.team2.nextpage.command.member.entity;

import com.team2.nextpage.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

/**
 * 회원 엔티티 (Soft Delete 적용)
 *
 * @author 김태형
 */
@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@SQLDelete(sql = "UPDATE tbl_user SET status = 'DELETED', deleted_at = NOW() WHERE id = ?")
@SQLRestriction("status = 'ACTIVE'")
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password; // 암호화된 비밀번호 저장

  @Column(nullable = false)
  private String nickname;

  @Enumerated(EnumType.STRING)
  private UserRole role; // 권한 (USER, ADMIN)

  @Enumerated(EnumType.STRING)
  private UserStatus status; // 상태 (ACTIVE, DELETED)

  private LocalDateTime deletedAt; // 탈퇴 일시

  @Builder
  public Member(String username, String password, String nickname, UserRole role, UserStatus status) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.role = role;
    this.status = status;
  }

  // 회원 탈퇴 처리
  public void delete() {
    this.status = UserStatus.DELETED;
    this.deletedAt = LocalDateTime.now();
  }

  // 비밀번호 설정 (암호화)
  public void setEncodedPassword(String encodedPassword) {
    this.password = encodedPassword;
  }

  // 역할 변경
  public void modifyRole(String roleName) {
    this.role = UserRole.valueOf(roleName);
  }
}

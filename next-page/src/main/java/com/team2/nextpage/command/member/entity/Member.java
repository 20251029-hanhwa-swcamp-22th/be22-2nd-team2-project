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
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET user_status = 'DELETED', deleted_at = NOW() WHERE user_id = ?")
@SQLRestriction("user_status = 'ACTIVE'")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String nickname;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "varchar(20) default 'USER'")
  private UserRole userRole;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "varchar(20) default 'ACTIVE'")
  private UserStatus userStatus; // ACTIVE, DELETED

  private LocalDateTime deletedAt;

  @Builder
  public Member(String username, String password, String nickname, UserRole userRole, UserStatus userStatus) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.userRole = userRole;
    this.userStatus = userStatus;
  }

  // 회원 탈퇴 처리
  public void delete(){
    this.userStatus = UserStatus.DELETED;
    this.deletedAt = LocalDateTime.now();
  }

  // 비밀번호 설정(암호화)
  public void setEncodedPassword(String encodedPassword){
    this.password = encodedPassword;
  }

  // 역할 변경
  public void modifyRole(String roleName){
    this.userRole = UserRole.valueOf(roleName);
  }
}

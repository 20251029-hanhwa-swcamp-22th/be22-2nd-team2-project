package com.team2.nextpage.command.reaction.service;

import com.team2.nextpage.command.member.repository.MemberRepository;
import com.team2.nextpage.command.reaction.dto.request.CreateCommentRequest;
import com.team2.nextpage.command.reaction.dto.request.UpdateCommentRequest;
import com.team2.nextpage.command.reaction.entity.Comment;
import com.team2.nextpage.command.reaction.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 반응(댓글/투표) Command 서비스
 *
 * @author 정병진
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ReactionService {

  private final CommentRepository commentRepository;
  // MemberRepository는 userId만 쓸 거면 필요 없지만, 혹시 몰라 남겨둠 (안 쓰면 삭제 가능)
  private final MemberRepository memberRepository;

  /**
   * 댓글 작성
   */
  public Long addComment(CreateCommentRequest request) {

    // 1. 안전하게 userId 가져오기
    Long currentUserId = getCurrentUserId();

    Comment newComment = Comment.builder()
        .bookId(request.getBookId())
        .writerId(currentUserId)
        .content(request.getContent())
        .build();

    Comment saveComment = commentRepository.save(newComment);

    return saveComment.getCommentId();
  }

  /**
   * 댓글 수정
   */
  public void modifyComment(Long commentId, UpdateCommentRequest request) {

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

    Long currentUserId = getCurrentUserId();

    validateWriter(comment, currentUserId);

    comment.updateContent(request.getContent());
  }

  /**
   * 댓글 삭제
   */
  public void removeComment(Long commentId){
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

    Long currentUserId = getCurrentUserId();

    validateWriter(comment, currentUserId);

    commentRepository.delete(comment);
  }

  /**
   * 소설 좋아요 투표
   */
  public Boolean voteBook(/* VoteRequest request */) {
    // TODO: 정병진 구현 필요
    return null;
  }

  /**
   * 작성자 권한 검증
   */
  private void validateWriter(Comment comment, Long userId) {
    if (!comment.getWriterId().equals(userId)){
      throw new IllegalArgumentException("작성자만 수정/삭제할 수 있습니다.");
    }
  }

  /**
   * 현재 로그인한 사용자의 ID(PK)를 반환하는 메서드 (가장 안전한 방식)
   */
  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // 1. 인증 정보가 아예 없거나, 로그인하지 않은 경우(anonymousUser) 체크
    if (authentication == null || !authentication.isAuthenticated() ||
        authentication.getPrincipal().equals("anonymousUser")) {
      throw new IllegalArgumentException("로그인이 필요한 서비스입니다.");
    }

    Object principal = authentication.getPrincipal();

    // 2. Principal이 UserDetails 타입인지 확인 (가장 정석)
    if (principal instanceof UserDetails) {
      String username = ((UserDetails) principal).getUsername();
      try {
        return Long.parseLong(username); // username에 userId가 들어있다고 가정
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("유효하지 않은 사용자 ID 형식입니다.");
      }
    }

    // 3. 만약 UserDetails가 아니라 그냥 String 등으로 들어온 경우 (방어 코드)
    try {
      return Long.parseLong(principal.toString());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("사용자 정보를 식별할 수 없습니다.");
    }
  }
}

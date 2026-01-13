package com.team2.nextpage.command.reaction.dto.request;

import com.team2.nextpage.command.reaction.entity.VoteType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoteRequest {
  private Long bookId;
  private VoteType voteType;
}

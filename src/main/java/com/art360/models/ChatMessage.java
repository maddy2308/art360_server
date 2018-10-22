package com.art360.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {
  private MessageType type;

  private String content;

  private String sender;

  public enum MessageType {
    CHAT,
    JOIN,
    LEAVE
  }
}
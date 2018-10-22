package com.art360.security;

import com.art360.models.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class WebSocketEventListener {

  private SimpMessageSendingOperations messagingTemplate;

  public WebSocketEventListener(final SimpMessageSendingOperations messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    log.info("Received a new web socket connection");
  }

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

    String username = (String) headerAccessor.getSessionAttributes().get("username");
    if(username != null) {
      log.info("User Disconnected : " + username);

      ChatMessage chatMessage = new ChatMessage();
      chatMessage.setType(ChatMessage.MessageType.LEAVE);
      chatMessage.setSender(username);

      messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }
  }
}
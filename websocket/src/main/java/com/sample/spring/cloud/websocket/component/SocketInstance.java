package com.sample.spring.cloud.websocket.component;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/websocket")  //서버가 바인딩된 주소를 뜻함.
@Log
public class SocketInstance {

    private static ConcurrentHashMap<String, Session> sessionIdWithSession = new ConcurrentHashMap<>(); // clientId, session

    @OnOpen //클라이언트가 소켓에 연결되때 마다 호출
    public void onOpen(Session session) {
        sessionIdWithSession.put(session.getId(), session);
        log.info("onOpen called, size:" + sessionIdWithSession.size() + ", session:" + session.getId());
    }

    @OnClose //클라이언트와 소켓과의 연결이 닫힐때 (끊길떄) 마다 호
    public void onClose(Session session) {
        sessionIdWithSession.remove(session.getId());
        log.info("onClose called, size:" + sessionIdWithSession.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("onMessage called, message:" + message);
        broadcast(message);
    }

    @OnError //의도치 않은 에러 발생
    public void onError(Session session, Throwable throwable) {
        sessionIdWithSession.remove(session.getId());
        log.info("onError called, size:" + sessionIdWithSession.size() + ", error:" + throwable.getMessage());
    }

    public static void broadcast(String message) {
        for (Map.Entry<String, Session> stringSessionEntry : sessionIdWithSession.entrySet()) {
            try {
                Session session = stringSessionEntry.getValue();
                session.getBasicRemote().sendText(message);
                log.info("send message, sessionId:" + session.getId() + ", message:" + message);
            } catch (IOException e) {
                log.warning("Caught exception while sending message, error:" + e.getMessage());
            }
        }
    }
}

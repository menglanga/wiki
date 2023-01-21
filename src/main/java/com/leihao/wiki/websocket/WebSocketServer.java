package com.leihao.wiki.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint("/ws/{token}")
public class WebSocketServer {
    private static final Logger LOG= LoggerFactory.getLogger(WebSocketServer.class);

    private String token="";
    private static Map<String , Session> map=new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("token")String token){
        map.put(token,session);
        this.token=token;
        LOG.info("有新连接：token:{},sessionId :{},当前连接数：{}",token,session.getId(),map.size());
    }

    @OnClose
    public void onClose(Session session){
        map.remove(this.token);
        LOG.info("连接关闭：token:{},sessionId :{},当前连接数：{}",token,session.getId(),map.size());
    }

    @OnMessage
    public void onMessage(String message,Session session){
        LOG.info("收到消息：{},内容 :{}",token,message);

    }

    @OnError
    public void onError(Session session,Throwable error){
        LOG.error("发送错误",error);
    }


    //群发消息
    public void sendInfo(String message){
        for (String token: map.keySet()){
            Session session=map.get(token);
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                LOG.error("推送消息失败：{},内容：{}",token,message);
            }
            LOG.info("推送消息成功：{},内容：{}",token,message);
        }
    }

}

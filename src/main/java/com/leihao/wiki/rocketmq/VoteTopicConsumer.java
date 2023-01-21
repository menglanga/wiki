package com.leihao.wiki.rocketmq;

import com.leihao.wiki.websocket.WebSocketServer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(consumerGroup = "default",topic = "VOTE_TOPIC")
public class VoteTopicConsumer  implements RocketMQListener<MessageExt> {
    private static final Logger LOG= LoggerFactory.getLogger(VoteTopicConsumer.class);

    @Autowired
    private WebSocketServer webSocketServer;


    @Override
    public void onMessage(MessageExt messageExt) {
        byte[] body = messageExt.getBody();
        LOG.info("rocketMQ收到消息：{}",new String(body));
        webSocketServer.sendInfo(new String(body));
    }
}

package com.miaoshaproject.mq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class Test
{
    public static void main(String[] args) throws Exception {
        //生产者,可以指定producer集群
        DefaultMQProducer producer = new DefaultMQProducer("producer_group_name");
        //设置name server的地址
        producer.setNamesrvAddr("47.107.121.107:9876");
        producer.start();
        System.out.println(producer.getNamesrvAddr());
        System.out.println(producer.getClientIP());
        System.out.println("启动了生产者producer");
        //message必须指定topic,和消息体body
        // 可以选择指定tag,key来进行细分message
        Message msgA = new Message("stock", "这是topicA的消息,没有指定tag和key".getBytes(RemotingHelper.DEFAULT_CHARSET));
//        Message msgA = new Message("topicA", "这是topicA的消息,没有指定tag和key".getBytes(RemotingHelper.DEFAULT_CHARSET));
//        Message msgB = new Message("topicB", "这是topicB的消息,没有指定tag和key".getBytes(RemotingHelper.DEFAULT_CHARSET));
//        Message msgC = new Message("topicC","tag-a","这是topicC的消息,指定了tag-a".getBytes(RemotingHelper.DEFAULT_CHARSET));
//        Message msgD = new Message("topicC","tag-b","这是topicC的消息,指定了tag-b".getBytes(RemotingHelper.DEFAULT_CHARSET));
//        Message msgE = new Message("topicC","tag-a","key1","这是topicC的消息,指定了tag-a和key1".getBytes(RemotingHelper.DEFAULT_CHARSET));
        Message[] messages =new Message[]{msgA};
//        Message[] messages =new Message[]{msgA,msgB,msgC,msgD,msgE};
        //发送消息
        for (Message message : messages) {
            SendResult result = producer.send(message);
            System.out.println("消息发送成功:id:" + result.getMsgId() +
                    " result:" + result.getSendStatus());
        }
    }
}

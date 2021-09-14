package com.github.icezerocat.zero.activemq.config;

import com.github.icezerocat.zero.activemq.constant.MqEnum;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * Description: activeMq配置
 * CreateDate:  2021/8/26 11:20
 *
 * @author zero
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class ActiveMqConfig {

    final private ActiveMQProperties2 activeMQProperties;

    @Bean
    public Queue queue() {
        return new ActiveMQQueue(MqEnum.QUEUE_NAME.getName());
    }

    @Bean
    public Topic topic() {
        return new ActiveMQTopic(MqEnum.TOPIC_NAME.getName());
    }

    @Bean
    ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(this.activeMQProperties.getUser(),this.activeMQProperties.getPassword(),this.activeMQProperties.getBrokerUrl());
    }

    @Bean
    JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPriority(999);
        return jmsTemplate;
    }

    @Bean
    JmsMessagingTemplate jmsMessagingTemplate(JmsTemplate jmsTemplate) {
        return new JmsMessagingTemplate(jmsTemplate);
    }

    /**
     * jsm监听队列
     *
     * @param connectionFactory mq链接工厂
     * @return jms队列工厂
     */
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }

    /**
     * jsm监听主题
     *
     * @param connectionFactory 链接工厂
     * @return jsm主题工厂
     */
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        //设置为发布订阅方式, 默认情况下使用的生产消费者方式
        bean.setPubSubDomain(true);
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }
}

package org.narcissus.narcissuscoreservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.narcissus.narcissuscoreservice.model.Message;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class KafkaRequestHandler {
    private static final Logger log = LoggerFactory.getLogger(KafkaRequestHandler.class);
    private final ObjectMapper objectMapper;
    private final ConcurrentMessageListenerContainer<String, String> container;

    protected KafkaRequestHandler(
            ObjectMapper objectMapper,
            String bootstrapServers,
            String groupId,
            List<String> topic,
            int maxThread,
            Map<String, Object> extraProps
    ){
        this.objectMapper = objectMapper;
        //Kafka consumer settings
        Map<String, Object> props = new HashMap<>();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", groupId);
        props.put("key.deserializer", StringDeserializer.class);
        props.put("value.deserializer", StringDeserializer.class);
        props.put("auto.offset.reset", "earliest");
        if (extraProps!= null) props.putAll(extraProps);

        ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(props);

        ContainerProperties containerProperties = new ContainerProperties(topic.toArray(new String[0]));
        containerProperties.setMessageListener((MessageListener<String, String>) this::onMessage);
        this.container = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
        this.container.setConcurrency(maxThread);
        this.container.start();
    }

    private void onMessage(ConsumerRecord<String, String> record){
        try {
            Message message = objectMapper.readValue(record.value(), Message.class);
            handle(message);
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
    }

    protected abstract void handle(Message message);

    public void stop() {container.stop();}
}

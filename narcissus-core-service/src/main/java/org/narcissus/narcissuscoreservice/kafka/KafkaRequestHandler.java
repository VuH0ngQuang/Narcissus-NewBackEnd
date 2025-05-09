package org.narcissus.narcissuscoreservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.narcissus.narcissuscoreservice.constants.MessageTypeEnum;
import org.narcissus.narcissuscoreservice.model.messagePayload.Message;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.narcissus.narcissuscoreservice.model.messagePayload.ResponsePayload;
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
import java.util.UUID;


public abstract class KafkaRequestHandler {
    private static final ThreadLocal<UUID> currentKey = new ThreadLocal<>();
    private static final ThreadLocal<String> currentTopic = new ThreadLocal<>();
    private static final Logger log = LoggerFactory.getLogger(KafkaRequestHandler.class);
    private final Producer<UUID, String> producer;
    private final ObjectMapper objectMapper;
    private final ConcurrentMessageListenerContainer<UUID, String> container;

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
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put("bootstrap.servers", bootstrapServers);
        consumerProps.put("group.id", groupId);
        consumerProps.put("key.deserializer", UUIDDeserializer.class.getName());
        consumerProps.put("value.deserializer", StringDeserializer.class.getName());
        consumerProps.put("auto.offset.reset", "earliest");
        if (extraProps != null) consumerProps.putAll(extraProps);
        ConsumerFactory<UUID, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);

        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put("bootstrap.servers", bootstrapServers);
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.UUIDSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(producerProps);

        ContainerProperties containerProperties = new ContainerProperties(topic.toArray(new String[0]));
        containerProperties.setMessageListener((MessageListener<UUID, String>) this::onMessage);
        this.container = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
        this.container.setConcurrency(maxThread);
        this.container.start();
    }

    private void onMessage(ConsumerRecord<UUID, String> record){
        try {
            currentKey.set(record.key());
            currentTopic.set(record.topic());
            Message message = objectMapper.readValue(record.value(), Message.class);
            handle(message);
        } catch (Exception e) {
            log.error(String.valueOf(e));
        } finally {
            currentKey.remove();   // Clean up
            currentTopic.remove(); // Clean up
        }
    }

    public void sendResponse(ResponsePayload responsePayload, String uri) {
        UUID key = currentKey.get();
        String topic = currentTopic.get();

        if (key == null || topic == null) {
            log.error("Missing topic or key context for response");
            return;
        }

        try {
            Message message = new Message<>();
            message.setMessageType(MessageTypeEnum.RESPONSE);
            message.setSource(topic);
            message.setUri(uri);
            message.setPayload(responsePayload);
            String responseJson = objectMapper.writeValueAsString(message);
            ProducerRecord<UUID, String> record = new ProducerRecord<>(topic, key, responseJson);
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    log.error("Failed to send response");
                } else {
                    log.info("Sent response to topic {} with key {}", topic, key);
                }
            });
        } catch (Exception e) {
            log.error("Failed to serialize response", e);
        }
    }

    protected abstract void handle(Message message);

    public void stop() {container.stop();}
}

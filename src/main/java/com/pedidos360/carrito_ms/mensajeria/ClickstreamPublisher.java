package com.pedidos360.carrito_ms.mensajeria;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
 
/**
 * Publica eventos de clickstream en Kafka. El clickstream es telemetria: si Kafka
 * no responde, se registra el error y se sigue adelante, la operacion principal
 * del usuario (ver un producto, agregar al carrito) NUNCA debe fallar por esto.
 * (Los timeouts cortos estan en application.yml: max.block.ms = 2000.)
 */
@Slf4j
@Component
public class ClickstreamPublisher {
 
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;
    private final ObjectMapper mapper = new ObjectMapper();
 
    public ClickstreamPublisher(KafkaTemplate<String, String> kafkaTemplate,
                                @Value("${clickstream.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }
 
    public void publicar(ClickstreamEvent evento) {
        try {
            if (evento.getEventId() == null) {
                evento.setEventId(java.util.UUID.randomUUID().toString());
            }
            if (evento.getTimestamp() == null) {
                evento.setTimestamp(java.time.Instant.now().toString());
            }
            String json = mapper.writeValueAsString(evento);
            // key = usuarioId -> mismo usuario, misma particion, orden garantizado
            kafkaTemplate.send(topic, evento.getUsuarioId(), json)
                    .whenComplete((res, ex) -> {
                        if (ex != null) {
                            log.warn("No se pudo publicar evento {} en Kafka: {}", evento.getTipo(), ex.getMessage());
                        } else {
                            log.info("Evento {} publicado (usuario={}, particion={}, offset={})",
                                    evento.getTipo(), evento.getUsuarioId(),
                                    res.getRecordMetadata().partition(), res.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            log.warn("Evento {} descartado (Kafka no disponible): {}", evento.getTipo(), e.getMessage());
        }
    }
}
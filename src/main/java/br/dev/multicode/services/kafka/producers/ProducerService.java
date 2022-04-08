package br.dev.multicode.services.kafka.producers;

import io.smallrye.mutiny.Uni;

public interface ProducerService {

  <T> Uni<Void> sendToKafka(T message);

}

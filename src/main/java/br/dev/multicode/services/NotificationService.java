package br.dev.multicode.services;

import br.dev.multicode.services.kafka.producers.ProducerService;

public interface NotificationService {

  <T> void doNotification(T message, ProducerService producerService);

}

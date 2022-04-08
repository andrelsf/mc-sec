package br.dev.multicode.services.impl;

import br.dev.multicode.services.NotificationService;
import br.dev.multicode.services.kafka.producers.ProducerService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationServiceImpl implements NotificationService {

  @Override
  public <T> void doNotification(T message, ProducerService producerService)
  {
    Uni.createFrom()
        .item(message)
        .emitOn(Infrastructure.getDefaultWorkerPool())
        .subscribe()
        .with(producerService::sendToKafka, Throwable::getMessage);
  }
}

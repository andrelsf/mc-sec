package br.dev.multicode.services.kafka.producers;


import br.dev.multicode.models.OrderMessage;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import java.util.concurrent.CompletableFuture;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class InventoryProducer implements ProducerService {

  private final Logger logger = Logger.getLogger(this.getClass());

  @Inject
  @Channel("sec-inventory")
  Emitter<OrderMessage> emitter;

  @Override
  public <T> Uni<Void> sendToKafka(T message)
  {
    logger.infof("Start of send message to Kafka topic inventory");

    emitter.send(Message.of((OrderMessage) message)
        .withAck(() -> {
          logger.infof("Message sent successfully.");
          return CompletableFuture.completedFuture(null);
        })
        .withNack(throwable -> {
          logger.errorf("Failed to send message. ERROR: %s", throwable.getMessage());
          return CompletableFuture.completedFuture(null);
        }));

    return Uni.createFrom().voidItem();
  }

}

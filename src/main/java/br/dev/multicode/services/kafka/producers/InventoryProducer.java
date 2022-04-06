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
public class InventoryProducer {

  private final Logger logger = Logger.getLogger(this.getClass());

  @Inject
  @Channel("sec-inventory")
  Emitter<OrderMessage> emitter;

  public void doNotification(OrderMessage orderMessage) {
    Uni.createFrom()
        .item(orderMessage)
        .emitOn(Infrastructure.getDefaultWorkerPool())
        .subscribe()
        .with(this::sendOrderToKafka, Throwable::new);
  }

  private Uni<Void> sendOrderToKafka(final OrderMessage orderMessage)
  {
    logger.infof("Start of send message to Kafka topic inventory");

    emitter.send(Message.of(orderMessage)
        .withAck(() -> {
          logger.infof("Message sent successfully. eventId=%s", orderMessage.getEventId());
          return CompletableFuture.completedFuture(null);
        })
        .withNack(throwable -> {
          logger.infof("Message sent failed. ERROR: %s", throwable.getMessage());
          return CompletableFuture.completedFuture(null);
        }));

    return Uni.createFrom().voidItem();
  }

}

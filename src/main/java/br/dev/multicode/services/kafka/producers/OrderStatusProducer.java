package br.dev.multicode.services.kafka.producers;

import br.dev.multicode.models.CurrentOrderStatus;
import io.smallrye.mutiny.Uni;
import java.util.concurrent.CompletableFuture;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OrderStatusProducer {

  private final Logger logger = Logger.getLogger(this.getClass());

  @Inject
  @Channel("sec-order-status")
  Emitter<CurrentOrderStatus> emitter;

  public Uni<Void> sendToKafka(final CurrentOrderStatus currentOrderStatus)
  {
    logger.infof("Start of send message to kafka topic sec-order-status");

    emitter.send(Message.of(currentOrderStatus)
      .withAck(() -> {
        logger.infof("Message sent successfully. orderId=%s", currentOrderStatus.getOrderId());
        return CompletableFuture.completedFuture(null);
      })
      .withNack(throwable -> {
        logger.errorf("Failed to send the message. ERROR: %s", throwable.getMessage());
        return CompletableFuture.completedFuture(null);
      }));

    return Uni.createFrom().voidItem();
  }

}

package br.dev.multicode.services.kafka.producers;

import br.dev.multicode.models.OrderPaymentMessage;
import io.smallrye.mutiny.Uni;
import java.util.concurrent.CompletableFuture;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OrderPaymentProducer {

  private final Logger logger = Logger.getLogger(this.getClass());

  @Inject
  @Channel("sec-payment")
  Emitter<OrderPaymentMessage> emitter;

  public Uni<Void> sendOrderPaymentToKafka(final OrderPaymentMessage orderPaymentMessage)
  {
    logger.infof("Start of sending the payment message. orderId=%s", orderPaymentMessage.getOrderId());

    emitter.send(Message.of(orderPaymentMessage)
        .withAck(() -> {
          logger.infof("Message sent successfully. orderId=%s", orderPaymentMessage.getOrderId());
          return CompletableFuture.completedFuture(null);
        })
        .withNack(throwable -> {
          logger.errorf("Failed to send message. ERROR: %s", throwable.getMessage());
          return CompletableFuture.completedFuture(null);
        }));

    return Uni.createFrom().voidItem();
  }
}

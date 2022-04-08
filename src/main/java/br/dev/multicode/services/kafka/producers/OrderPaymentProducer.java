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
public class OrderPaymentProducer implements ProducerService {

  private final Logger logger = Logger.getLogger(this.getClass());

  @Inject
  @Channel("sec-payment")
  Emitter<OrderPaymentMessage> emitter;

  @Override
  public <T> Uni<Void> sendToKafka(T message)
  {
    logger.infof("Start of sending the payment message.");

    emitter.send(Message.of((OrderPaymentMessage) message)
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

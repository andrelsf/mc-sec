package br.dev.multicode.services.kafka.consumers;

import br.dev.multicode.models.OrderProcessingStatus;
import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OrderStatusConsumer {

  private final Logger log = Logger.getLogger(this.getClass());

  @Incoming("sec-response-status")
  public CompletionStage<Void> receiveOrderStatusResponseFromKafka(Message<OrderProcessingStatus> orderProcessingStatus)
  {
    var metadata = orderProcessingStatus.getMetadata(IncomingKafkaRecordMetadata.class)
        .orElseThrow();

    OrderProcessingStatus orderProcessingStatusReceived = orderProcessingStatus.getPayload();
    log.infof("SEC: %s - Got a order message: orderId=%s", metadata.getTopic(),
        orderProcessingStatusReceived.getOrderId());

    return orderProcessingStatus.ack();
  }

}

package br.dev.multicode.services.kafka.consumers;

import br.dev.multicode.models.OrderMessage;
import br.dev.multicode.services.OrderSecEventService;
import br.dev.multicode.services.kafka.producers.InventoryProducer;
import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OrderConsumer {

  private final Logger logger = Logger.getLogger(this.getClass());

  @Inject InventoryProducer inventoryProducer;

  @Inject OrderSecEventService orderSecEventService;

  @Incoming("sec-new-order")
  public CompletionStage<Void> receive(Message<OrderMessage> orderMessage)
  {
    var metadata = orderMessage.getMetadata(IncomingKafkaRecordMetadata.class)
        .orElseThrow();

    OrderMessage orderMessageReceived = orderMessage.getPayload();
    logger.infof("%s - Got a order message: %s", metadata.getTopic(), orderMessageReceived.getOrderId());

    inventoryProducer.doNotification(orderMessageReceived);
    orderSecEventService.create(orderMessageReceived);

    return orderMessage.ack();
  }

}

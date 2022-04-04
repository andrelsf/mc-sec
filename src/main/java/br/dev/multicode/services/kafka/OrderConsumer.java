package br.dev.multicode.services.kafka;

import br.dev.multicode.models.OrderMessage;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OrderConsumer {

  private final Logger logger = Logger.getLogger(this.getClass());

  @Incoming("sec-new-order")
  public void receive(OrderMessage orderMessage)
  {
    logger.infof("Got a order message: %s", orderMessage.getOrderId());
  }

}

package com.pinlun.springcamelhello;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringcamelhelloApplication {



    public static void main(String[] args) throws Exception{

        CamelContext camelContext = new DefaultCamelContext();
        try {
            camelContext.addComponent("activemq", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"));
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("activemq:queue:test.queue")
                            .to("stream:out");
                }
            });

            ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
            camelContext.start();
            producerTemplate.sendBody("activemq:test.queue", "Hello World");
            Thread.sleep(2000);
        } finally {
            camelContext.stop();
        }
    }
}

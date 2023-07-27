package com.andersenlab.messageclient;

import com.andersenlab.messagebroker.controller.BrokerControllerApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProducerApplication {

    @Autowired
    private BrokerControllerApi brokerControllerApi;

    private final static Logger LOG = LoggerFactory.getLogger(ProducerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
        //mvn spring-boot:run -Dspring-boot.run.arguments=
    }

//    @Override
//    public void run(String... args) throws Exception {
//        LOG.info("Executing run");
//        for (String arg: args) {
//            LOG.info("Incoming arg: " + arg);
//            brokerControllerApi.createDestination(arg);
//
//        }
//    }
}

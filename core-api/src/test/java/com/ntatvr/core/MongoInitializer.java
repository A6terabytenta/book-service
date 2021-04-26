package com.ntatvr.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.Network;

public class MongoInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
  private static final Logger LOGGER = LoggerFactory.getLogger(MongoInitializer.class);
  private static MongoDBContainer mongoDBContainer;

  static {
    Network network = Network.newNetwork();
    mongoDBContainer = new MongoDBContainer("mongo:4.4")
        .withStartupAttempts(2)
        .withNetwork(network)
        .withNetworkAliases("testcontainer")
        .withReuse(false);
    mongoDBContainer.start();
  }

  public MongoInitializer() {

  }

  @Override
  public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
    LOGGER.error("spring.data.mongodb.uri=" + mongoDBContainer.getReplicaSetUrl()
        + "?keepAlive=100&connectTimeoutMS=30000");
    TestPropertyValues.of("spring.data.mongodb.uri=" + mongoDBContainer.getReplicaSetUrl()
        + "?keepAlive=100&connectTimeoutMS=30000")
        .applyTo(configurableApplicationContext.getEnvironment());
  }
}

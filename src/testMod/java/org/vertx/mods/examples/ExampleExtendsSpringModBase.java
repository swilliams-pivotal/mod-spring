package org.vertx.mods.examples;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.vertx.java.core.Vertx;
import org.vertx.mods.spring.SpringModBase;

public class ExampleExtendsSpringModBase extends SpringModBase {

  @Override
  protected void beforeStartApplicationContext() {
    // Example
  }

  @Override
  protected void afterStartApplicationContext() {

    // Example trivial usage of a bean lookup
    ApplicationContext context = super.getApplicationContext();
    Vertx vertx = context.getBean("vertx", Vertx.class);

    Assert.isTrue(super.vertx == vertx);
  }

}

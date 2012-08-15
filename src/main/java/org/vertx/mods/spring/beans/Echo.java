/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vertx.mods.spring.beans;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.Lifecycle;
import org.springframework.util.Assert;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.mods.spring.VertxSupport;


/**
 * @author swilliams
 *
 */
public class Echo implements InitializingBean, Lifecycle {

  private static final String TEST_ADDRESS = "vertx.test.echo";

  private VertxSupport vertxSupport;

  private boolean running;

  private String handlerId;

  @Override
  public void start() {
    System.out.println("start()");

    Assert.notNull(vertxSupport, "VertxSupport should be injected!");

    this.handlerId = vertxSupport.getVertx().eventBus().registerHandler(TEST_ADDRESS, new Handler<Message<String>>() {
      @Override
      public void handle(Message<String> event) {
        System.out.println("echo.body: " + event.body);
        System.out.println("echo.replyAddress: " + event.replyAddress);
        event.reply(event.body);
      }});

    this.running = true;
  }

  @Override
  public void stop() {
    System.out.println("stop()");
    vertxSupport.getVertx().eventBus().unregisterHandler(handlerId);
    this.running = false;
  }

  @Override
  public boolean isRunning() {
    System.out.println("isRunning()");
    return running;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    System.out.println("afterPropertiesSet()");
  }


  public VertxSupport getVertxSupport() {
    return vertxSupport;
  }

  @Autowired
  public void setVertxSupport(VertxSupport vertxSupport) {
    this.vertxSupport = vertxSupport;
  }
}

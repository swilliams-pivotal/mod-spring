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
import org.springframework.context.Lifecycle;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.mods.spring.VertxSupport;


/**
 * @author swilliams
 *
 */
public class Echo implements InitializingBean, Lifecycle, VertxSupport {

  private static final String TEST_ADDRESS = "vertx.test.echo";

  private boolean running;

  private String handlerId;

  private Vertx vertx;

  @Override
  public void start() {
    System.out.println("start()");

    this.handlerId = getVertx().eventBus().registerHandler(TEST_ADDRESS, new Handler<Message<String>>() {
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
    getVertx().eventBus().unregisterHandler(handlerId);
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

  @Override
  public Vertx getVertx() {
    return vertx;
  }

  @Override
  public void setVertx(Vertx vertx) {
    this.vertx = vertx;
  }
}

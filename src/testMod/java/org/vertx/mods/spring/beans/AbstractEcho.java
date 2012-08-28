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

import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;


/**
 * @author swilliams
 *
 */
public abstract class AbstractEcho implements DisposableBean {

  private String handlerId;

  private Vertx vertx;

  protected void register(String address) throws Exception {
    Assert.notNull(getVertx(), "Vertx must not be null");

    this.handlerId = getVertx().eventBus().registerHandler(address, new Handler<Message<String>>() {

      @Override
      public void handle(Message<String> event) {
        event.reply(event.body);
      }});
  }

  public Vertx getVertx() {
    return vertx;
  }

  public void setVertx(Vertx vertx) {
    Assert.notNull(vertx, "Vertx must not be null");
    this.vertx = vertx;
  }

  @Override
  public void destroy() throws Exception {
    getVertx().eventBus().unregisterHandler(handlerId);
  }

}

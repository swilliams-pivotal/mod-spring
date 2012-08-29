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
package org.vertx.mods.spring;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.vertx.java.test.junit.VertxConfigurableJUnit4Runner;
import org.vertx.java.test.junit.annotations.TestVerticle;
import org.vertx.java.test.junit.support.QueueReplyHandler;
import org.vertx.java.test.junit.support.VertxTestBase;


@RunWith(VertxConfigurableJUnit4Runner.class)
@TestVerticle(main="test_deployer.js")
public class SpringModTest extends VertxTestBase {

  private static final String QUESTION = "Is there anybody there?";

  @Before
  public void setup() {
    sleep(1000L);
  }

  @Test
  public void testPropertyInjection() throws Exception {
    testEventBus("vertx-test-echo1");
  }

  @Test
  public void testAutowireInjection() throws Exception {
    testEventBus("vertx-test-echo2");
  }

  @Test
  public void testVertxAware() throws Exception {
    testEventBus("vertx-test-echo3");
  }


  private void testEventBus(String address) throws Exception {

    final long timeout = 2000L;
    final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
    final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

    getVertx().eventBus().send(address, QUESTION, new QueueReplyHandler<String>(queue, timeout, timeUnit));

    try {
      String answer = queue.poll(timeout, timeUnit);
      System.out.printf("For %s Q:%s A:%s %n", address, answer, QUESTION.equals(answer));
      Assert.assertTrue(QUESTION.equals(answer));

    } catch (InterruptedException e) {
      //
    }
  }

}

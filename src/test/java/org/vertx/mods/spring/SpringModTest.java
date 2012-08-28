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
@TestVerticle(main="test_verticle0.js")
public class SpringModTest extends VertxTestBase {

  private static final String QUESTION = "Is there anybody there?";

  @Before
  public void setup() {
    sleep(1000L);
  }

  @Test
  public void testEchoOne() throws Exception {
    testEventBus("vertx-test-echo1");
  }

  @Test
  public void testEchoTwo() throws Exception {
    testEventBus("vertx-test-echo2");
  }

  @Test
  public void testEchoThree() throws Exception {
    testEventBus("vertx-test-echo3");
  }


  private void testEventBus(String address) throws Exception {

    final long timeout = 5000L;
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

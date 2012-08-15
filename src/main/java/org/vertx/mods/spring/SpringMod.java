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

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.util.Assert;
import org.vertx.java.busmods.BusModBase;


/**
 * @author swilliams
 *
 */
public class SpringMod extends BusModBase {

  private GenericApplicationContext parent;

  private GenericApplicationContext context;

  @Override
  public void start() {

    Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

    Assert.notNull(getContainer().getConfig(), "config object is null, which can't be good");
    String springConfig = getContainer().getConfig().getString("springConfig", "applicationConfig.xml");

    DefaultVertxSupport vertxSupport = new DefaultVertxSupport();
    vertxSupport.setVertx(vertx);

    parent = new GenericApplicationContext();

    ConfigurableListableBeanFactory factory = parent.getBeanFactory();
    factory.registerSingleton("vertxSupport", vertxSupport);

    parent.refresh();
    parent.start();

    if (springConfig.endsWith(".xml")) {
      context = new GenericXmlApplicationContext();
      context.setParent(parent);
      ((GenericXmlApplicationContext) context).load(springConfig);
    }
    else {
      context = new AnnotationConfigApplicationContext();
      context.setParent(parent);

      try {
        Class<?> c = Class.forName(springConfig);
        ((AnnotationConfigApplicationContext) context).register(c);
      } catch (ClassNotFoundException e) {
        throw new IllegalArgumentException(e);
      }
    }

    configure();

    context.refresh();
    context.start();
    context.registerShutdownHook();

    super.start();
  }

  protected void configure() {
    // no-op
  }

  @Override
  public void stop() throws Exception {

    if (context != null) {
      context.close();
    }

    if (parent != null) {
      parent.close();
    }

    super.stop();
  }

  public ApplicationContext getContext() {
    return context;
  }

}

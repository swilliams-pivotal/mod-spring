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

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.util.Assert;
import org.vertx.java.busmods.BusModBase;


/**
 * @author swilliams
 *
 */
public abstract class SpringModBase extends BusModBase {

  private GenericApplicationContext parent;

  private GenericApplicationContext context;

  @Override
  public void start() {

    super.start();

    Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

    Assert.notNull(getContainer().getConfig(), "config object is null, which can't be good");
    String springConfig = getContainer().getConfig().getString("springConfig", "applicationConfig.xml");

    BeanPostProcessor vertxSupportProcessor = new VertxAwareBeanPostProcessor(vertx);
    this.parent = new GenericApplicationContext();
    ConfigurableListableBeanFactory factory = parent.getBeanFactory();
    factory.registerSingleton("vertx", vertx);
    factory.addBeanPostProcessor(vertxSupportProcessor);

    parent.refresh();
    parent.start();

    if (springConfig.endsWith(".xml")) {
      this.context = new GenericXmlApplicationContext();
      context.setParent(parent);

      factory = context.getBeanFactory();
      factory.addBeanPostProcessor(vertxSupportProcessor);

      ((GenericXmlApplicationContext) context).load(springConfig);
    }
    else {
      this.context = new AnnotationConfigApplicationContext();
      context.setParent(parent);

      factory = context.getBeanFactory();
      factory.addBeanPostProcessor(vertxSupportProcessor);

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
  }

  protected abstract void configure();

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

}

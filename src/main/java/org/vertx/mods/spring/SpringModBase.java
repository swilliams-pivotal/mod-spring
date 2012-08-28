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
public abstract class SpringModBase extends BusModBase {

  private GenericApplicationContext parent;

  private GenericApplicationContext context;

  @Override
  public final void start() {

    super.start();

    // Make sure we're using the correct classloader
    Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

    Assert.notNull(getContainer().getConfig(), "config object is null, which can't be good");
    String springConfig = getContainer().getConfig().getString("springConfig", "applicationConfig.xml");

    // Looks weird but avoids doing this twice below in the if/else block.
    // the vertx singleton needs to be registered before the context XML
    // is loaded or annotated @Configuration class is registered
    //.
    // I also like the analogy of the vertx instance being in a parent
    // context, as it's not instantiated in the context you really use.
    this.parent = new GenericApplicationContext();
    ConfigurableListableBeanFactory factory = parent.getBeanFactory();
    factory.registerSingleton("vertx", vertx);

    parent.refresh();
    parent.start();

    // Detect whether this app is configured with an XML or @Configuration.
    if (springConfig.endsWith(".xml")) {
      this.context = new GenericXmlApplicationContext();
      context.setParent(parent);

      ((GenericXmlApplicationContext) context).load(springConfig);
    }
    else {
      this.context = new AnnotationConfigApplicationContext();
      context.setParent(parent);

      try {
        Class<?> c = Class.forName(springConfig);
        ((AnnotationConfigApplicationContext) context).register(c);
      } catch (ClassNotFoundException e) {
        throw new IllegalArgumentException(e);
      }
    }

    factory = context.getBeanFactory();
    BeanPostProcessor vertxSupportProcessor = new VertxAwareBeanPostProcessor(vertx);
    factory.addBeanPostProcessor(vertxSupportProcessor);

    beforeStartApplicationContext();

    context.refresh();
    context.start();
    context.registerShutdownHook();
    
    afterStartApplicationContext();
  }

  protected abstract void beforeStartApplicationContext();

  protected abstract void afterStartApplicationContext();

  protected ApplicationContext getApplicationContext() {
    return context;
  }

  @Override
  public final void stop() throws Exception {

    if (context != null) {
      context.close();
    }

    if (parent != null) {
      parent.close();
    }

    super.stop();
  }

}

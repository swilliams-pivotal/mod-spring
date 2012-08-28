package org.vertx.mods.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.vertx.java.core.Vertx;

public class VertxAwareBeanPostProcessor implements BeanPostProcessor {

  private Vertx vertx;

  public VertxAwareBeanPostProcessor(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {

    if (bean instanceof VertxAware) {
      VertxAware vertxSupport = (VertxAware) bean;
      vertxSupport.setVertx(vertx);
      return vertxSupport;
    }

    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName)
      throws BeansException {

    return bean;
  }

}

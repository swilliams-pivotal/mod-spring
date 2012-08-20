package org.vertx.mods.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.vertx.java.core.Vertx;

public class VertxSupportProcessor implements BeanPostProcessor {

  private Vertx vertx;

  public VertxSupportProcessor(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {

    if (bean instanceof VertxSupport) {
      VertxSupport vertxSupport = (VertxSupport) bean;
      vertxSupport.setVertx(vertx);
    }

    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName)
      throws BeansException {

    return bean;
  }

}

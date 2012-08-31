# mod-spring

A non-runnable Spring support Module for vert.x.

## Download 

A download is available for testing at: [mod.zip](https://github.com/downloads/swilliams-vmw/mod-spring/mod.zip "mod.zip download for vertx.spring-v.10")

## HowTo use mod-spring

1. Specify mod-spring as an include, in your own module's mod.json:

    {
      "main": "org.vertx.mods.spring.SpringMod",
      "includes":"vertx.spring-v1.0"
    }

2. Specify a Spring XML configuration file or @Configuration class name when you deploy your module, in the JSON configuration with the 'springConfig' parameter. If you don't specify the parameter, the default 'applicationConfig.xml' is used. The default name for a Spring XML configuration file is 'applicationConfig.xml'.

3. Implement the VertxAware interface, use @Autowire or find the bean of type Vertx.class or named 'vertx' in the ApplicationContext to get access to the Vertx instance in your Spring application.

### Advanced usage

You can get programmatic access to the ApplicationContext if you need to, by extending SpringModBase, inside the 'beforeStartApplicationContext()' and 'afterStartApplicationContext()' methods. By this point the 'Vertx' object will have been registered as a singleton and a BeanPostProcessor registered that processes beans that implement the VertxAware interface.

1. Extend the abstract class 'org.vertx.mods.spring.SpringModBase'.

2. Again, you can specify mod-spring as an include, in your own module's mod.json, but this time, specify your own main class.

    {
      "main": "my.example.mod.ExtendsSpringModBase",
      "includes":"vertx.spring-v1.0"
    }

3. Specify the configuration file or annotated @Configuration class, as above.

4. Find and use the Vertx.class instance, as above.


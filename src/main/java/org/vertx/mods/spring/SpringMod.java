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


/**
 * Concrete final class, via a simple extension of SpringModBase.
 * Use this class name in your mod.json main declaration.
 * 
 * <code>
 * {
 *  "main": "org.vertx.mods.spring.SpringMod",
 *  "includes": "vertx.spring-v1.0"
 * }
 * </code>
 * 
 * @author swilliams
 *
 */
public final class SpringMod extends SpringModBase {

  @Override
  protected void beforeStartApplicationContext() {
    // NO-OP
  }

  @Override
  protected void afterStartApplicationContext() {
    // NO-OP
  }

}

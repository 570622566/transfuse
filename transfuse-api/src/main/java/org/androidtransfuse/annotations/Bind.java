/**
 * Copyright 2011-2015 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * On a `@TransfuseModule` class, defining a binding with `@Bind` defines a relationship between two related classes
 * in which for each given type the class bound to will be used during injection.
 *
 * [source,java]
 * .*Example:*
 * --
 * @TransfuseModule
 * @Bind(type=Cat.class, to=OrangeTabby.class)
 * public class Module{}
 * --
 *
 * In each instance where a `Cat` is injected, an instance of the `OrangeTabby` class will be used.
 *
 * @author John Ericksen
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bind {

    Class<?> type();

    Class<?> to();
}

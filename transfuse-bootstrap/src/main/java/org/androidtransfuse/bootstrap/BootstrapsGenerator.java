/**
 * Copyright 2013 John Ericksen
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
package org.androidtransfuse.bootstrap;

import com.sun.codemodel.*;
import org.androidtransfuse.adapter.PackageClass;
import org.androidtransfuse.gen.AbstractRepositoryGenerator;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.util.Repository;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class BootstrapsGenerator extends AbstractRepositoryGenerator {

    public static final PackageClass BOOTSTRAPS_INJECTOR = new PackageClass(
            Bootstraps.BOOTSTRAPS_INJECTOR_PACKAGE, Bootstraps.BOOTSTRAPS_INJECTOR_NAME);

    @Inject
    public BootstrapsGenerator(ClassGenerationUtil generationUtil, UniqueVariableNamer namer) {
        super(Repository.class, generationUtil, namer, BOOTSTRAPS_INJECTOR, Bootstraps.BootstrapInjector.class);
    }

    @Override
    protected JExpression generateInstance(JDefinedClass factoryRepositoryClass, JClass interfaceClass, JClass concreteType) throws JClassAlreadyExistsException {
        return JExpr._new(concreteType);
    }
}

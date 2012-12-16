/**
 * Copyright 2012 John Ericksen
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
package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.ParcelsGenerator;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;

/**
 * Executes the generation of the Parcels utility class in a Transaction Worker.
 *
 * @author John Ericksen
 */
public class ParcelsTransactionWorker extends AbstractCompletionTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void> {

    private ParcelsGenerator parcelsGenerator;

    @Inject
    public ParcelsTransactionWorker(ParcelsGenerator parcelsGenerator) {
        this.parcelsGenerator = parcelsGenerator;
    }

    @Override
    public Void innerRun(Map<Provider<ASTType>, JDefinedClass> value) {
        parcelsGenerator.generate(value);
        return null;
    }
}

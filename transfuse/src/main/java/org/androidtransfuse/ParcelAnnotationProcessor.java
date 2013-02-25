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
package org.androidtransfuse;

import org.androidtransfuse.annotations.Parcel;
import org.androidtransfuse.annotations.ScopeReference;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.config.ConfigurationScope;
import org.androidtransfuse.config.EnterableScope;
import org.androidtransfuse.processor.ParcelProcessor;
import org.androidtransfuse.processor.ReloadableASTElementFactory;
import org.androidtransfuse.util.SupportedAnnotations;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.inject.Inject;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Annotation processor which generates for classes annotated with @Parcel, Android Parcelable wrapper classes.
 * <p/>
 * In addition this processor will generate the org.androidtransfuse.Parcels class.  This utility defines a mapping
 * of annotated @Parcel class with the Parcelable wrapper and allows for easy wrapping of any processed @Parcel.
 *
 * @author John Ericksen
 */
@SupportedAnnotations(Parcel.class)
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@Bootstrap
public class ParcelAnnotationProcessor extends AnnotationProcessorBase {

    @Inject
    private ParcelProcessor parcelProcessor;
    @Inject
    private ReloadableASTElementFactory reloadableASTElementFactory;
    @Inject
    @ScopeReference(ConfigurationScope.class)
    private EnterableScope configurationScope;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        Bootstraps.getInjector(ParcelAnnotationProcessor.class)
                .addSingleton(ProcessingEnvironment.class, processingEnv)
                .inject(ParcelAnnotationProcessor.class, this);
    }

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {

        configurationScope.enter();

        parcelProcessor.submit(reloadableASTElementFactory.buildProviders(roundEnvironment.getElementsAnnotatedWith(Parcel.class)));

        parcelProcessor.execute();

        if (roundEnvironment.processingOver()) {
            // Throws an exception if errors still exist.
            parcelProcessor.checkForErrors();
        }

        configurationScope.exit();

        return true;
    }
}

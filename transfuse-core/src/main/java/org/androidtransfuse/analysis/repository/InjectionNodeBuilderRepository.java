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
package org.androidtransfuse.analysis.repository;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionNodeBuilder;
import org.androidtransfuse.gen.variableDecorator.GeneratedProviderInjectionNodeBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.util.matcher.Matcher;
import org.androidtransfuse.util.matcher.Matchers;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionNodeBuilderRepository implements InjectionNodeBuilder{


    private final Map<Matcher<InjectionSignature>, InjectionNodeBuilder> typeQualifierBindings = new HashMap<Matcher<InjectionSignature>, InjectionNodeBuilder>();
    private final Map<InjectionSignature, InjectionNodeBuilder> typeBindings = new HashMap<InjectionSignature, InjectionNodeBuilder>();
    private final VariableInjectionNodeBuilder defaultBinding;
    private final ASTClassFactory astClassFactory;
    private final Matcher<ASTType> providerMatcher;
    private final GeneratedProviderInjectionNodeBuilder generatedProviderInjectionNodeBuilder;

    @Inject
    public InjectionNodeBuilderRepository(
            VariableInjectionNodeBuilder defaultBinding,
            ASTClassFactory astClassFactory,
            GeneratedProviderInjectionNodeBuilder generatedProviderInjectionNodeBuilder) {
        this.defaultBinding = defaultBinding;
        this.astClassFactory = astClassFactory;
        this.generatedProviderInjectionNodeBuilder = generatedProviderInjectionNodeBuilder;

        this.providerMatcher = Matchers.type(astClassFactory.getType(Provider.class)).ignoreGenerics().build();
    }

    public void putAnnotation(Class<?> viewClass, InjectionNodeBuilder viewVariableBuilder) {
        putSignatureMatcher(Matchers.annotated().byType(astClassFactory.getType(viewClass)).build(), viewVariableBuilder);
    }

    public void putType(ASTType type, InjectionNodeBuilder variableBuilder) {
        putTypeMatcher(new InjectionSignature(type, ImmutableSet.<ASTAnnotation>of()), variableBuilder);
    }

    public void putType(Class<?> clazz, InjectionNodeBuilder variableBuilder) {
        putType(astClassFactory.getType(clazz), variableBuilder);
    }

    public void putTypeMatcher(InjectionSignature injectionSignature, InjectionNodeBuilder variableBuilder) {
        this.typeBindings.put(injectionSignature, variableBuilder);
    }

    public void putSignatureMatcher(Matcher<InjectionSignature> matcher, InjectionNodeBuilder variableBuilder) {
        this.typeQualifierBindings.put(matcher, variableBuilder);
    }

    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ImmutableSet<ASTAnnotation> qualifiers) {
        //check type and qualifiers
        InjectionSignature injectionSignature = new InjectionSignature(astType, qualifiers);
        InjectionNodeBuilder typeQualifierBuilder = get(typeQualifierBindings, injectionSignature);

        if(typeQualifierBuilder != null){
            return typeQualifierBuilder.buildInjectionNode(astType, context, qualifiers);
        }

        //check type
        InjectionNodeBuilder typeBindingBuilder = typeBindings.get(injectionSignature);

        if (typeBindingBuilder != null) {
            return typeBindingBuilder.buildInjectionNode(astType, context, qualifiers);
        }

        if(qualifiers.size() > 0){
            throw new TransfuseAnalysisException("Unable to find injection node for annotated type: " + astType + " " +
                    StringUtils.join(qualifiers, ", "));
        }

        //generated provider
        if(providerMatcher.matches(astType)){
            return generatedProviderInjectionNodeBuilder.buildInjectionNode(astType, context, qualifiers);
        }

        //default case
        return defaultBinding.buildInjectionNode(astType, context, qualifiers);
    }

    private <T> InjectionNodeBuilder get(Map<Matcher<T>, InjectionNodeBuilder> builderMap, T input){
        List<InjectionNodeBuilder> builders = new ArrayList<InjectionNodeBuilder>();
        for (Map.Entry<Matcher<T>, InjectionNodeBuilder> bindingEntry : builderMap.entrySet()) {
            if (bindingEntry.getKey().matches(input)) {
                builders.add(bindingEntry.getValue());
            }
        }
        if(builders.size() > 1){
            throw new TransfuseAnalysisException("Multiple types matched on type " + input + ":" + StringUtils.join(builders, ","));
        }
        if(builders.size() == 1){
            return builders.get(0);
        }
        return null;
    }
}
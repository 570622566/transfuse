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
package org.androidtransfuse.adapter.classes;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.lang.annotation.Annotation;

/**
 * Class specific AST Type
 *
 * @author John Ericksen
 */
public class ASTClassType implements ASTType {

    private final Class<?> clazz;
    private final PackageClass packageClass;
    private final ImmutableSet<ASTAnnotation> annotationList;
    private final ImmutableSet<ASTMethod> methods;
    private final ImmutableSet<ASTConstructor> constructors;
    private final ImmutableSet<ASTField> fields;
    private final ASTType superClass;
    private final ImmutableSet<ASTType> interfaces;

    public ASTClassType(Class<?> clazz,
                        PackageClass packageClass,
                        ImmutableSet<ASTAnnotation> annotationList,
                        ImmutableSet<ASTConstructor> constructors,
                        ImmutableSet<ASTMethod> methods,
                        ImmutableSet<ASTField> fields,
                        ASTType superClass,
                        ImmutableSet<ASTType> interfaces) {
        this.clazz = clazz;
        this.packageClass = packageClass;
        this.annotationList = annotationList;
        this.constructors = constructors;
        this.methods = methods;
        this.fields = fields;
        this.superClass = superClass;
        this.interfaces = interfaces;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return clazz.getAnnotation(annotation);
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return clazz.isAnnotationPresent(annotation);
    }

    @Override
    public ImmutableSet<ASTMethod> getMethods() {
        return methods;
    }

    @Override
    public ImmutableSet<ASTField> getFields() {
        return fields;
    }

    @Override
    public ImmutableSet<ASTConstructor> getConstructors() {
        return constructors;
    }

    @Override
    public String getName() {
        return packageClass.getCanonicalName();
    }

    @Override
    public boolean isConcreteClass() {
        return !clazz.isInterface() && !clazz.isSynthetic();
    }

    @Override
    public ImmutableSet<ASTAnnotation> getAnnotations() {
        return annotationList;
    }

    @Override
    public ASTType getSuperClass() {
        return superClass;
    }

    @Override
    public ImmutableSet<ASTType> getInterfaces() {
        return interfaces;
    }

    @Override
    public boolean isArray() {
        return clazz.isArray();
    }

    @Override
    public PackageClass getPackageClass() {
        return packageClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ASTType)) {
            return false;
        }

        ASTType that = (ASTType) o;

        return new EqualsBuilder().append(getName(), that.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getName()).hashCode();
    }

    @Override
    public ImmutableSet<ASTType> getGenericParameters() {
        return ImmutableSet.of();
    }

    @Override
    public boolean inheritsFrom(ASTType type) {
        return ASTUtils.getInstance().inherits(this, type, true, true);
    }

    @Override
    public boolean extendsFrom(ASTType type) {
        return ASTUtils.getInstance().inherits(this, type, false, true);
    }

    @Override
    public boolean implementsFrom(ASTType type) {
        return ASTUtils.getInstance().inherits(this, type, true, false);
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }

    @Override
    public String toString() {
        return getName();
    }
}
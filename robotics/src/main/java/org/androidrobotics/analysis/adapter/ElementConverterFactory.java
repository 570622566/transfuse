package org.androidrobotics.analysis.adapter;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ElementConverterFactory {

    @Inject
    private ASTTypeBuilderVisitor astTypeBuilderVisitor;
    @Inject
    private ElementConverterFactory astTypeElementConverterFactory;
    @Inject
    private ASTElementFactory astElementFactory;

    public <T> ASTTypeElementConverter<T> buildTypeConverter(Class<T> clazz) {
        return new ASTTypeElementConverter<T>(clazz, astElementFactory);
    }

    public <T> AnnotationTypeValueConverterVisitor<T> buildAnnotationValueConverter(Class<T> clazz) {
        return new AnnotationTypeValueConverterVisitor<T>(clazz, astTypeBuilderVisitor, astTypeElementConverterFactory);
    }
}

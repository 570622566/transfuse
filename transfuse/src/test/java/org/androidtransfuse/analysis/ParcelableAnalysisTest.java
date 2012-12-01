package org.androidtransfuse.analysis;

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Parcel;
import org.androidtransfuse.annotations.ParcelConverter;
import org.androidtransfuse.annotations.Transient;
import org.androidtransfuse.model.GetterSetterMethodPair;
import org.androidtransfuse.model.ParcelableDescriptor;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * @author John Ericksen
 */
public class ParcelableAnalysisTest {

    @Inject
    private ParcelableAnalysis parcelableAnalysis;
    @Inject
    private ASTClassFactory astClassFactory;

    @Before
    public void setup() {
        TransfuseTestInjector.inject(this);
    }

    @Test
    public void testBasic() {
        @Parcel
        class Basic {
            String stringValue;
            int intValue;

            public String getStringValue() {
                return stringValue;
            }

            public void setStringValue(String stringValue) {
                this.stringValue = stringValue;
            }

            public int getIntValue() {
                return intValue;
            }

            public void setIntValue(int intValue) {
                this.intValue = intValue;
            }
        }

        ASTType basicAst = astClassFactory.getType(Basic.class);
        ParcelableDescriptor analysis = parcelableAnalysis.analyze(basicAst);

        assertNull(analysis.getParcelConverterType());
        assertEquals(2, analysis.getGetterSetterPairs().size());
        assertTrue(contains(analysis.getGetterSetterPairs(), "intValue"));
        assertTrue(contains(analysis.getGetterSetterPairs(), "stringValue"));
    }

    @Test
    public void testMissingSetter() {
        @Parcel
        class MissingSetter {
            String stringValue;
            int intValue;

            public String getStringValue() {
                return stringValue;
            }

            public int getIntValue() {
                return intValue;
            }

            public void setIntValue(int intValue) {
                this.intValue = intValue;
            }
        }

        ASTType basicAst = astClassFactory.getType(MissingSetter.class);
        ParcelableDescriptor analysis = parcelableAnalysis.analyze(basicAst);

        assertNull(analysis.getParcelConverterType());
        assertEquals(1, analysis.getGetterSetterPairs().size());
        assertTrue(contains(analysis.getGetterSetterPairs(), "intValue"));
        assertFalse(contains(analysis.getGetterSetterPairs(), "stringValue"));
    }

    @Test
    public void testMissingGetter() {
        @Parcel
        class MissingGetter {
            String stringValue;
            int intValue;

            public void setStringValue(String stringValue) {
                this.stringValue = stringValue;
            }

            public int getIntValue() {
                return intValue;
            }

            public void setIntValue(int intValue) {
                this.intValue = intValue;
            }
        }

        ASTType basicAst = astClassFactory.getType(MissingGetter.class);
        ParcelableDescriptor analysis = parcelableAnalysis.analyze(basicAst);

        assertNull(analysis.getParcelConverterType());
        assertEquals(1, analysis.getGetterSetterPairs().size());
        assertTrue(contains(analysis.getGetterSetterPairs(), "intValue"));
        assertFalse(contains(analysis.getGetterSetterPairs(), "stringValue"));
    }

    @Test
    public void testParcelConverter() {

        class Converter implements ParcelConverter {
            @Override
            public void toParcel(Object input, android.os.Parcel destinationParcel) {
            }

            @Override
            public Object fromParcel(android.os.Parcel parcel) {
                return null;
            }
        }

        @Parcel(Converter.class)
        class Target {
        }

        ASTType targetAst = astClassFactory.getType(Target.class);
        ASTType converterAst = astClassFactory.getType(Converter.class);
        ParcelableDescriptor analysis = parcelableAnalysis.analyze(targetAst);

        assertEquals(converterAst, analysis.getParcelConverterType());
    }

    @Test
    public void testTransient() {
        @Parcel
        class Basic {
            String stringValue;
            int intValue;

            @Transient
            public String getStringValue() {
                return stringValue;
            }

            public void setStringValue(String stringValue) {
                this.stringValue = stringValue;
            }

            public int getIntValue() {
                return intValue;
            }

            @Transient
            public void setIntValue(int intValue) {
                this.intValue = intValue;
            }
        }

        ASTType basicAst = astClassFactory.getType(Basic.class);
        ParcelableDescriptor analysis = parcelableAnalysis.analyze(basicAst);

        assertNull(analysis.getParcelConverterType());
        assertEquals(0, analysis.getGetterSetterPairs().size());
    }

    private boolean contains(List<GetterSetterMethodPair> getterSetterPairs, String name) {
        for (GetterSetterMethodPair getterSetterPair : getterSetterPairs) {
            if (getterSetterPair.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }


}

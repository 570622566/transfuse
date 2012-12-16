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

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Provider;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author John Ericksen
 */
public class ParcelProcessorTest {

    private ParcelProcessor parcelProcessor;
    private TransactionProcessorPool mockGlobalProcessor;
    private TransactionProcessorPool mockSubmitProcessor;
    private ParcelTransactionFactory mockTransactionFactory;
    private Provider<ASTType> input;
    private Transaction mockTransaction;

    @Before
    public void setup() {
        mockGlobalProcessor = mock(TransactionProcessorPool.class);
        mockSubmitProcessor = mock(TransactionProcessorPool.class);
        mockTransactionFactory = mock(ParcelTransactionFactory.class);
        input = mock(Provider.class);
        mockTransaction = mock(Transaction.class);

        parcelProcessor = new ParcelProcessor(mockGlobalProcessor, mockSubmitProcessor, mockTransactionFactory);
    }

    @Test
    public void testSubmit() {

        when(mockTransactionFactory.buildTransaction(input)).thenReturn(mockTransaction);

        parcelProcessor.submit(Collections.singleton(input));

        verify(mockSubmitProcessor).submit(mockTransaction);
    }

    @Test
    public void testExecute() {

        parcelProcessor.execute();

        verify(mockGlobalProcessor).execute();
    }

    @Test
    public void testCheckForErrorsFailing() {
        testCheckForErrors(true);
    }

    @Test
    public void testCheckForErrorsPassing() {
        testCheckForErrors(false);
    }

    private void testCheckForErrors(boolean errored) {
        try {
            when(mockGlobalProcessor.isComplete()).thenReturn(!errored);
            when(mockGlobalProcessor.getErrors()).thenReturn(ImmutableSet.of(new Exception()));

            parcelProcessor.checkForErrors();

            // Should not get this far if errored.
            assertFalse(errored);
        } catch (TransfuseAnalysisException exception) {
            // Exception should be thrown if errored.
            assertTrue(errored);
        }
    }
}

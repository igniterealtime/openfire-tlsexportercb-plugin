/*
 * Copyright (C) 2026 Ignite Realtime Foundation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.igniterealtime.openfire.channelbinding;

import org.junit.jupiter.api.Test;

import javax.net.ssl.ExtendedSSLSession;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLKeyException;
import javax.net.ssl.SSLSession;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link JdkProviderTlsExporterChannelBindingProvider}.
 *
 * Covers scenarios for extracting TLS exporter channel binding data.
 */
public class JdkProviderTlsExporterChannelBindingProviderTest
{
    private final JdkProviderTlsExporterChannelBindingProvider provider = new JdkProviderTlsExporterChannelBindingProvider();

    /**
     * Verifies that an empty Optional is returned when the session is not an ExtendedSSLSession.
     */
    @Test
    void returnsEmptyIfNotExtendedSSLSession()
    {
        // Setup test fixture
        final SSLSession session = mock(SSLSession.class);
        when(session.getLocalCertificates()).thenReturn(null);
        final SSLEngine engine = mock(SSLEngine.class);
        when(engine.getSession()).thenReturn(session);
        when(engine.getUseClientMode()).thenReturn(false);

        // Execute system under test
        final Optional<byte[]> result = provider.getChannelBinding(engine);

        // Verify result
        assertTrue(result.isEmpty(), "Expected empty Optional when session is not ExtendedSSLSession");
    }

    /**
     * Verifies that the exported keying material is returned when exportKeyingMaterialData succeeds.
     */
    @Test
    void returnsExportedKeyingMaterial() throws Exception
    {
        // Setup test fixture
        final ExtendedSSLSession session = mock(ExtendedSSLSession.class);
        when(session.getLocalCertificates()).thenReturn(null);
        final SSLEngine engine = mock(SSLEngine.class);
        when(engine.getSession()).thenReturn(session);
        when(engine.getUseClientMode()).thenReturn(false);
        final byte[] expected = new byte[] {1,2,3};
        when(session.exportKeyingMaterialData(eq(JdkProviderTlsExporterChannelBindingProvider.EXPORTER_LABEL), any(), eq(32))).thenReturn(expected);

        // Execute system under test
        final Optional<byte[]> result = provider.getChannelBinding(engine);

        // Verify result
        assertTrue(result.isPresent(), "Expected Optional to be present when exportKeyingMaterialData returns data");
        assertArrayEquals(expected, result.get(), "Returned byte array does not match expected value");
    }

    /**
     * Verifies that an empty Optional is returned when exportKeyingMaterialData throws SSLKeyException.
     */
    @Test
    void returnsEmptyOnSSLKeyException() throws Exception
    {
        // Setup test fixture
        final ExtendedSSLSession session = mock(ExtendedSSLSession.class);
        when(session.getLocalCertificates()).thenReturn(null);
        final SSLEngine engine = mock(SSLEngine.class);
        when(engine.getSession()).thenReturn(session);
        when(engine.getUseClientMode()).thenReturn(false);
        when(session.exportKeyingMaterialData(any(), any(), anyInt())).thenThrow(new SSLKeyException("fail"));

        // Execute system under test
        final Optional<byte[]> result = provider.getChannelBinding(engine);

        // Verify result
        assertTrue(result.isEmpty(), "Expected empty Optional when exportKeyingMaterialData throws SSLKeyException");
    }

    /**
     * Verifies that an empty Optional is returned when exportKeyingMaterialData returns null.
     */
    @Test
    void returnsEmptyWhenExportKeyingMaterialDataReturnsNull() throws Exception
    {
        // Setup test fixture
        final ExtendedSSLSession session = mock(ExtendedSSLSession.class);
        when(session.getLocalCertificates()).thenReturn(null);
        final SSLEngine engine = mock(SSLEngine.class);
        when(engine.getSession()).thenReturn(session);
        when(engine.getUseClientMode()).thenReturn(false);
        when(session.exportKeyingMaterialData(any(), any(), anyInt())).thenReturn(null);

        // Execute system under test
        final Optional<byte[]> result = provider.getChannelBinding(engine);

        // Verify result
        assertTrue(result.isEmpty(), "Expected empty Optional when exportKeyingMaterialData returns null");
    }

    /**
     * Verifies that a NullPointerException is thrown when the session is null.
     */
    @Test
    void throwsOnNullSession()
    {
        // Execute system under test & Verify result
        assertThrows(NullPointerException.class, () -> provider.getChannelBinding(null), "Expected NullPointerException when session is null");
    }
}

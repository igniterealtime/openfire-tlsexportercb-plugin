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

import org.jivesoftware.util.channelbinding.ChannelBindingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.net.ssl.ExtendedSSLSession;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLKeyException;
import javax.net.ssl.SSLSession;
import java.util.Objects;
import java.util.Optional;

/**
 * Provides extraction of TLS exporter channel binding data using the Java JDK's exportKeyingMaterial method on
 * ExtendedSSLSession. This approach is expected to be available in Java JDK version 25 and later, but is not
 * guaranteed to be present in all environments or providers.
 *
 * The implementation uses the label defined by RFC 9266 for both TLS 1.2 and TLS 1.3. If the session is not an
 * ExtendedSSLSession or the exportKeyingMaterialData method is unavailable, this provider returns an empty result.
 */
public class JdkProviderTlsExporterChannelBindingProvider implements ChannelBindingProvider
{
    private static final Logger Log = LoggerFactory.getLogger(JdkProviderTlsExporterChannelBindingProvider.class);

    private static final int TLS_EXPORTER_LENGTH = 32;

    public static final String EXPORTER_LABEL = "EXPORTER-Channel-Binding";

    @Override
    public String getType()
    {
        return "tls-exporter";
    }

    /**
     * Attempts to extract the TLS exporter channel binding data from the provided SSL session using the
     * exportKeyingMaterialData method on ExtendedSSLSession. Returns an Optional containing the exported keying
     * material, or empty if the session is not an ExtendedSSLSession or the operation fails.
     *
     * @param engine the SSLEngine from which to extract channel binding data (must not be null)
     * @return an Optional containing the channel binding data, or empty if unavailable or unsupported
     */
    @Override
    public Optional<byte[]> getChannelBinding(@Nonnull final SSLEngine engine)
    {
        Objects.requireNonNull(engine, "session must not be null");
        final SSLSession session = engine.getSession();

        if (!(session instanceof ExtendedSSLSession extendedSession)) {
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(extendedSession.exportKeyingMaterialData(EXPORTER_LABEL, new byte[0], TLS_EXPORTER_LENGTH));
        } catch (SSLKeyException e) {
            Log.debug("exportKeyingMaterialData failed for session: {}", session, e);
        }
        return Optional.empty();
    }
}

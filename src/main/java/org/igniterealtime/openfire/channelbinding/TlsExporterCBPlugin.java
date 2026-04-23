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

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.util.channelbinding.ChannelBindingProviderManager;

import java.io.File;

/**
 * Plugin that registers a TLS Exporter Channel Binding provider with Openfire.
 *
 * @author Guus der Kinderen, guus@goodbytes.nl
 */
public class TlsExporterCBPlugin implements Plugin
{
    public JdkProviderTlsExporterChannelBindingProvider provider;

    /**
     * Initializes the plugin and registers the TLS Exporter Channel Binding provider.
     *
     * @param pluginManager the plugin manager
     * @param pluginDirectory the directory where the plugin is located
     */
    @Override
    public void initializePlugin(PluginManager pluginManager, File pluginDirectory)
    {
        provider = new JdkProviderTlsExporterChannelBindingProvider();
        ChannelBindingProviderManager.getInstance().addProvider(provider);
    }

    /**
     * Destroys the plugin and unregisters the TLS Exporter Channel Binding provider.
     */
    @Override
    public void destroyPlugin()
    {
        ChannelBindingProviderManager.getInstance().removeProvider(provider);
    }
}

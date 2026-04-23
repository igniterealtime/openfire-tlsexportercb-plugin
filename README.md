# Openfire TLS-Exporter Channel Binding Plugin

## Overview

This plugin adds support for the [tls-exporter channel binding](https://datatracker.ietf.org/doc/html/rfc9266) to the [Openfire](https://www.igniterealtime.org/projects/openfire/) real-time communications server.

### What is Channel Binding?

Channel binding is a security mechanism that cryptographically ties authentication at the application layer to the underlying secure transport (such as TLS). This helps prevent certain types of man-in-the-middle attacks by ensuring that both parties are using the same secure channel. The `tls-exporter` channel binding type is defined in [RFC 9266](https://datatracker.ietf.org/doc/html/rfc9266).

Openfire supports channel binding since version 5.1.0. This plugin specifically adds support for the `tls-exporter` channel binding type, which is only available in Java 25 and newer.

## Motivation

The functionality provided by this plugin requires Java 25 or later. As Openfire currently requires only Java 17 or later, this feature is provided as a plugin so that administrators running Openfire with Java 25+ can benefit from it. Once Openfire increases its minimum Java version to 25 or later, this plugin will likely be discontinued and its functionality merged into Openfire core.

## Installation

1. Ensure your Openfire server is running on Java 25 or newer.
2. Copy the `tlsexportercb.jar` file into the `plugins` directory of your Openfire installation.
3. The plugin will be automatically deployed.

## Reporting Issues

Issues may be reported to the [Ignite Realtime forums](https://discourse.igniterealtime.org) or via this repo's [GitHub Issues](https://github.com/igniterealtime/openfire-tlsexportercb-plugin).

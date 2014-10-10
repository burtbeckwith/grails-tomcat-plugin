/*
 * Copyright 2013-2014 SpringSource
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.plugins.tomcat.fork

import grails.build.logging.GrailsConsole
import grails.util.Metadata
import groovy.transform.CompileStatic

import org.apache.catalina.Context
import org.apache.catalina.LifecycleException
import org.apache.catalina.connector.Connector
import org.apache.catalina.startup.Tomcat
import org.apache.coyote.http11.Http11NioProtocol
import org.grails.plugins.tomcat.TomcatServer

/**
 * A Tomcat runner that runs a WAR file
 *
 * @author Graeme Rocher
 * @since 2.3
 */
@CompileStatic
class TomcatWarRunner extends TomcatServer {

	protected static final GrailsConsole CONSOLE = GrailsConsole.getInstance()

	protected Tomcat tomcat = new Tomcat()
	protected String warPath
	protected String contextPath

	TomcatWarRunner(String warPath, String contextPath) {
		this.warPath = warPath
		this.contextPath = contextPath
	}

	protected void enableSslConnector(String host, int httpsPort) {
		Connector sslConnector
		try {
			sslConnector = new Connector()
		}
		catch (Exception e) {
			throw new RuntimeException("Couldn't create HTTPS connector", e)
		}

		sslConnector.scheme = "https"
		sslConnector.secure = true
		sslConnector.port = httpsPort
		sslConnector.setProperty "SSLEnabled", "true"
		sslConnector.setAttribute "keystoreFile", keystoreFile
		sslConnector.setAttribute "keystorePass", keyPassword
		sslConnector.URIEncoding = "UTF-8"

		if (!host.equals("localhost")) {
			sslConnector.setAttribute "address", host
		}

		tomcat.service.addConnector sslConnector
	}

	@Override
	protected void doStart(String host, int httpPort, int httpsPort) {

		Metadata.getCurrent()[Metadata.WAR_DEPLOYED] = "true"
		tomcat.port = httpPort
		tomcat.silent = true

		if (getConfigParam("nio")) {
			CONSOLE.updateStatus "Enabling Tomcat NIO Connector"
			def connector = new Connector(Http11NioProtocol.name)
			connector.port = httpPort
			tomcat.service.addConnector connector
			tomcat.connector = connector
		}

		tomcat.baseDir = tomcatDir
		try {
			configureJarScanner tomcat.addWebapp(contextPath, warPath)
		}
		catch (Throwable e) {
			CONSOLE.error "Error loading Tomcat: $e.message", e
			System.exit 1
		}

		tomcat.enableNaming()

		final Connector connector = tomcat.connector

		// Only bind to host name if we aren't using the default
		if (!host.equals("localhost")) {
			connector.setAttribute "address", host
		}

		connector.URIEncoding = "UTF-8"

		if (httpsPort) {
			enableSslConnector host, httpsPort
		}

		ForkedTomcatServer.startKillSwitch tomcat, httpPort

		try {
			tomcat.start()
			CONSOLE.addStatus "Server running. Browse to http://${host ?: "localhost"}:$httpPort$contextPath"
		}
		catch (LifecycleException e) {
			CONSOLE.error "Error loading Tomcat: $e.message", e
			System.exit 1
		}
	}

	@Override
	void stop() {
		tomcat.stop()
	}
}

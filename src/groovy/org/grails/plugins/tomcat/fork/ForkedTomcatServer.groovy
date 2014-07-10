/*
 * Copyright 2012-2013 SpringSource
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
import grails.util.BuildSettings
import grails.util.Environment
import grails.web.container.EmbeddableServer
import groovy.transform.CompileStatic

import org.apache.catalina.startup.Tomcat
import org.codehaus.groovy.grails.cli.fork.ExecutionContext
import org.codehaus.groovy.grails.cli.fork.ForkedGrailsProcess
import org.codehaus.groovy.grails.plugins.GrailsPluginInfo
import org.codehaus.groovy.grails.plugins.GrailsPluginUtils
import org.grails.plugins.tomcat.TomcatKillSwitch

/**
 * An implementation of the Tomcat server that runs in forked mode.
 *
 * @author Graeme Rocher
 * @since 2.2
 */
// @CompileStatic
class ForkedTomcatServer extends ForkedGrailsProcess implements EmbeddableServer {

	public static final GrailsConsole CONSOLE = GrailsConsole.getInstance()
	@Delegate EmbeddableServer tomcatRunner

	ForkedTomcatServer(TomcatExecutionContext executionContext) {
		this.executionContext = executionContext
		forkReserve = true
	}

	protected ForkedTomcatServer() {
		executionContext = (TomcatExecutionContext)readExecutionContext()
		if (!executionContext) {
			throw new IllegalStateException("Forked server created without first creating execution context and calling fork()")
		}
	}

	static void main(String[] args) {
		new ForkedTomcatServer().run()
	}

	def run() {
		if (!isReserveProcess()) {
			runInternal()
		}
		else {
			CONSOLE.verbose "Waiting for resume signal for idle JVM"
			waitForResume()
			CONSOLE.verbose "Resuming idle JVM"
			runInternal()
		}
	}

	protected void runInternal() {
		TomcatExecutionContext ec = (TomcatExecutionContext)executionContext
		BuildSettings buildSettings = initializeBuildSettings(ec)
		URLClassLoader classLoader = initializeClassLoader(buildSettings)
		initializeLogging ec.grailsHome, classLoader

		tomcatRunner = createTomcatRunner(buildSettings, ec, classLoader)
		if (ec.securePort > 0) {
			tomcatRunner.startSecure ec.host, ec.port, ec.securePort
		}
		else {
			tomcatRunner.start ec.host, ec.port
		}

		setupReloading classLoader, buildSettings
	}

	@Override
	protected void discoverAndSetAgent(ExecutionContext executionContext) {
		// no agent for war mode
		if (!((TomcatExecutionContext)executionContext).warPath) {
			super.discoverAndSetAgent executionContext
		}
	}

	protected EmbeddableServer createTomcatRunner(BuildSettings buildSettings, TomcatExecutionContext ec, URLClassLoader classLoader) {
		if (ec.warPath) {
			if (Environment.isFork()) {
				BuildSettings.initialiseDefaultLog4j classLoader
			}

			return new TomcatWarRunner(ec.warPath, ec.contextPath)
		}

		new TomcatDevelopmentRunner("$buildSettings.baseDir/web-app", buildSettings.webXmlLocation.absolutePath, ec.contextPath, classLoader)
	}

	void start(String host, int port) {
		startSecure host, port, 0
	}

	void startSecure(String host, int httpPort, int httpsPort) {
		final ec = (TomcatExecutionContext)executionContext
		ec.host = host
		ec.port = httpPort
		ec.securePort = httpsPort
		def t = new Thread({
			final process = fork()
			Runtime.addShutdownHook {
				try {
					process.destroy()
				}
				catch (e) {
					// ignore, nothing we can do
				}
			}
		})

		t.start()

		while(!isAvailable(host, httpPort)) {
			sleep 100
		}
		System.setProperty TomcatKillSwitch.TOMCAT_KILL_SWITCH_ACTIVE, "true"
	}

	boolean isAvailable(String host, int port) {
		try {
			new Socket(host ?: 'localhost', port)
			return true
		}
		catch (e) {
			return false
		}
	}

	void stop() {
		final ec = (TomcatExecutionContext)executionContext
		try { new URL("http://${ec?.host ?: 'localhost'}:${(ec?.port ?: 8080 ) + 1}").text }
		catch(ignored) {}
	}

	@Override
	Collection<File> findSystemClasspathJars(BuildSettings buildSettings) {
		Set<File> jars = []
		jars.addAll super.findSystemClasspathJars(buildSettings)

		GrailsPluginInfo info = GrailsPluginUtils.getPluginBuildSettings().getPluginInfoForName('tomcat8')
		String jarName = "grails-plugin-tomcat8-${info.version}.jar"
		File jar = info.descriptor.file.parentFile.listFiles().find { File f -> f.name.equals(jarName) }

		if (jar?.exists()) {
			jars << jar
		}
		else {
			CONSOLE.error "Tomcat plugin classes JAR $jarName not found"
		}

		jars
	}

	static void startKillSwitch(final Tomcat tomcat, final int serverPort) {
		new Thread(new TomcatKillSwitch(tomcat, serverPort)).start()
	}

	void restart() {
		stop()
		start()
	}

	void start() {
		start null, null
	}

	void start(int port) {
		start null, port
	}

	void startSecure() {
		tomcatRunner.startSecure null
	}

	void startSecure(int port) {
		tomcatRunner.startSecure null, null, port
	}
}

// No programmable web.xml path yet, so put it in the right place automatically
eventGenerateWebXmlEnd = {
	System.setProperty("grails.server.factory", "org.grails.plugins.tomcat.TomcatServerFactory")
}

eventCreatePluginArchiveStart = { stagingDir ->
	if (plugin.name != 'tomcat8') {
		return
	}

	ant.jar(destfile: "$stagingDir/grails-plugin-tomcat8-${plugin.version}.jar", filesonly: true) {
		fileset(dir: classesDir, includes: 'org/grails/plugins/tomcat/**/*.class')
	}
}

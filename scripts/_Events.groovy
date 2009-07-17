// No programmable web.xml path yet, so put it in the right place automatically
eventGenerateWebXmlEnd = {
	
	ant.copy(file:"${grailsSettings.projectWorkDir}/resources/web.xml", todir:"${basedir}/web-app/WEB-INF") 
	System.setProperty("grails.server.factory", "org.grails.tomcat.TomcatServerFactory")
}
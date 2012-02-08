
tomcatVersion = "7.0.25"

grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

    inherits "global"
    log "warn"

    repositories {
        grailsCentral()
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
		build( "org.apache.tomcat:tomcat-catalina-ant:$tomcatVersion" ) {
			transitive = false
		}
		build "org.apache.tomcat.embed:tomcat-embed-core:$tomcatVersion"
		build "org.apache.tomcat.embed:tomcat-embed-jasper:$tomcatVersion"	
		build "org.apache.tomcat.embed:tomcat-embed-logging-log4j:$tomcatVersion"	
		build "org.apache.tomcat.embed:tomcat-embed-logging-juli:$tomcatVersion"			
		
		// needed for JSP compilation
		runtime "org.eclipse.jdt.core.compiler:ecj:3.6.2"

        build "org.grails:grails-plugin-tomcat:${grailsVersion}"
    }

}

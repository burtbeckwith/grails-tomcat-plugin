
tomcatVersion = "7.0.8"

grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir	= "target/test-reports"
grails.project.dependency.resolution = {
    inherits "global" // inherit Grails' default dependencies
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
		compile "org.apache.tomcat.embed:tomcat-embed-core:$tomcatVersion"
		compile "org.apache.tomcat.embed:tomcat-embed-jasper:$tomcatVersion"	
		compile "org.apache.tomcat.embed:tomcat-embed-logging-log4j:$tomcatVersion"	

        compile "org.grails:grails-plugin-tomcat:${grailsVersion}"
    }

}

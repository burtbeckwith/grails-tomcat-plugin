if (System.getenv('TRAVIS_BRANCH')) {
	grails.project.repos.grailsCentral.username = System.getenv('GRAILS_CENTRAL_USERNAME')
	grails.project.repos.grailsCentral.password = System.getenv('GRAILS_CENTRAL_PASSWORD')
}

grails.project.work.dir = 'target'

grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		mavenLocal()
		grailsCentral()
		mavenCentral()
	}

	dependencies {
		String tomcatVersion = '9.0.62'
		compile "org.apache.tomcat.embed:tomcat-embed-core:$tomcatVersion"
		['el', 'jasper', 'logging-log4j', 'logging-juli', 'websocket'].each {
			runtime "org.apache.tomcat.embed:tomcat-embed-$it:$tomcatVersion"
		}
	}

	plugins {
		build ':release:3.1.2', ':rest-client-builder:2.1.1', {
			export = false
		}
	}
}

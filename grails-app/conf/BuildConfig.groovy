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
		grailsCentral()
		mavenLocal()
		mavenCentral()
	}

	dependencies {
		String tomcatVersion = '8.0.22'

		compile "org.apache.tomcat.embed:tomcat-embed-core:$tomcatVersion", {
			excludes 'tomcat-embed-logging-juli', 'tomcat-embed-logging-log4j'
		}

		runtime "org.apache.tomcat.embed:tomcat-embed-el:$tomcatVersion"

		runtime "org.apache.tomcat.embed:tomcat-embed-jasper:$tomcatVersion", {
			excludes 'ecj', 'tomcat-embed-core', 'tomcat-embed-el'
		}

		runtime "org.apache.tomcat.embed:tomcat-embed-logging-log4j:$tomcatVersion"

		runtime "org.apache.tomcat.embed:tomcat-embed-logging-juli:$tomcatVersion"

		runtime "org.apache.tomcat.embed:tomcat-embed-websocket:$tomcatVersion", {
			excludes 'tomcat-embed-core'
		}

		compile 'javax.servlet:javax.servlet-api:3.1.0'

		// needed for JSP compilation
		runtime 'org.eclipse.jdt.core.compiler:ecj:4.4.2', {
			excludes 'ant'
		}
	}

	plugins {
		build ':release:3.1.1', ':rest-client-builder:2.1.1', {
			export = false
		}
	}
}

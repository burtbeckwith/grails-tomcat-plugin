/*
 * Copyright 2013-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class TomcatGrailsPlugin {
	def version = '9.0.62'
	def grailsVersion = '2.5 > *'
	def scopes = [excludes: 'war']
	def title = 'Apache Tomcat plugin'
	def description = 'Makes Tomcat 9.x the servlet container for Grails at development time'
	def documentation = 'http://grails.org/plugin/tomcat'
	def license = 'APACHE'
	def organization = [name: 'Grails', url: 'http://www.grails.org']
	def developers = [
		[name: 'Graeme Rocher', email: 'rocherg@ociweb.com'],
		[name: 'Burt Beckwith', email: 'burt@burtbeckwith.com']
	]
	def issueManagement = [url: 'https://github.com/grails-plugins/grails-tomcat-plugin/issues']
	def scm = [url: 'https://github.com/grails-plugins/grails-tomcat-plugin']
}

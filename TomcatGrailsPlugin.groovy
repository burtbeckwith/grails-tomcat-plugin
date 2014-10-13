/* Copyright 2013-2014 SpringSource.
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
	def version = '8.0.14.1'
	def grailsVersion = '2.3 > *'
	def scopes = [excludes: 'war']
	def title = 'Apache Tomcat plugin'
	def description = 'Makes Tomcat 8.x the servlet container for Grails at development time'
	def documentation = 'http://grails.org/plugin/tomcat'
	def license = 'APACHE'
	def organization = [name: 'Pivotal', url: 'http://www.pivotal.io/oss/']
	def developers = [
		[name: 'Graeme Rocher', email: 'grocher@pivotal.io'],
		[name: 'Burt Beckwith', email: 'burt@burtbeckwith.com']
	]
	def issueManagement = [system: 'JIRA', url: 'http://jira.grails.org/browse/GPTOMCAT']
	def scm = [url: 'https://github.com/grails-plugins/grails-tomcat-plugin']
}

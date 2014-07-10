rm -rf target/release
mkdir target/release
cd target/release
git clone git@github.com:grails-plugins/grails-tomcat8-plugin.git
cd grails-tomcat8-plugin
grails clean
grails compile
#grails publish-plugin --snapshot --stacktrace --allow-overwrite
grails publish-plugin --stacktrace

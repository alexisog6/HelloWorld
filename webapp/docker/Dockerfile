
FROM tomcat:alpine

RUN rm -rf $CATALINA_HOME/webapps

ADD webapp-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/ROOT.war

CMD ["catalina.sh", "run"]

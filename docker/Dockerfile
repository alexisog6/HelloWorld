
FROM tomcat:alpine

RUN rm -rf $CATALINA_HOME/webapps

ADD ROOT.war $CATALINA_HOME/webapps/ROOT.war

CMD ["catalina.sh", "run"]


FROM tomcat:alpine

RUN rm -rf $CATALINA_HOME/webapps \
  && sed -i.bak -e "s/securerandom.source=file:\/dev\/random/securerandom.source=file:\/dev\/urandom/g" -e "s/securerandom.strongAlgorithms=NativePRNGBlocking/securerandom.strongAlgorithms=NativePRNG/g" $JAVA_HOME/lib/security/java.security

ADD webapp-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/ROOT.war

CMD ["catalina.sh", "run"]

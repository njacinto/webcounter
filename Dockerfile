FROM tomcat:8.5
MAINTAINER nfpj

#ADD your.war /usr/local/tomcat/webapps/
RUN ["rm", "-fr", "/usr/local/tomcat/webapps/ROOT"]
COPY ./target/webcounter-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]

#docker run --rm -it -p 8080:8080 yourName


set MAVEN_OPTS=-Xmx512m -XX:MaxPermSize=128m -Dhttps.protocols=TLSv1.2

mvn clean install -DskipTests -Dregenerateapi


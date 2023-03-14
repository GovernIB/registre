REM set JAVA_HOME=C:\Program Files\Java\jdk1.7.0_80
REM set MAVEN_OPTS=-Xmx512m -XX:MaxPermSize=128m -Dhttps.protocols=TLSv1.2

mvn install -DskipTests -Dregenerateapi -X


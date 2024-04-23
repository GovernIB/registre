
REM mvn exec:java  -Dexec.mainClass="org.fundaciobit.genapp.gensql.IndexGenerator"

REM mvn exec:java  -Dexec.mainClass="org.fundaciobit.genapp.sqlgenerator.SqlGenerator" -Dexec.args="regweb3 regweb3PULocal %1%"
mvn exec:java -Dexec.mainClass="org.fundaciobit.genapp.sqlgenerator.SqlGenerator" -Dexec.args="regweb3 regweb3PULocal %1%"

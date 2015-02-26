

call mvn install:install-file -Dfile=./lib/regweb-model-2.0.1.jar -DgroupId=es.caib.regweb.model.v2 -DartifactId=regweb-model -Dversion=1.0  -Dpackaging=jar

call mvn install:install-file -Dfile=./lib/jt400.jar -DgroupId=db2jdbcdriver -DartifactId=db2jdbcdriver  -Dversion=1.0  -Dpackaging=jar

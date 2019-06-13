cd `dirname $0`
cd ..
rm -f ./src/main/resources/com/sankuai/it/iam/mapper/*
java -cp .:./generator/mysql-connector-java-5.1.44.jar -jar ./generator/mybatis-generator-core-1.3.5.jar -configfile ./src/main/resources/generatorConfig.xml -overwrite
rm -f ./src/main/resources/com/sankuai/it/iam/sso/mapper/*
java -cp .:./generator/mysql-connector-java-5.1.44.jar -jar ./generator/mybatis-generator-core-1.3.5.jar -configfile ./src/main/resources/ssoucGeneratorConfig.xml -overwrite
rm -f ./src/main/resources/com/sankuai/it/iam/sync/mapper/*
java -cp .:./generator/mysql-connector-java-5.1.44.jar -jar ./generator/mybatis-generator-core-1.3.5.jar -configfile ./src/main/resources/syncGeneratorConfig.xml -overwrite
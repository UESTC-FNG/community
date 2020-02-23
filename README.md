## FNG社区

#部署
- GIT
- JDK
- Maven
- MySql
#步骤
- yum update
- yum install git
- mkdir code
- cd code
- git clone https://github.com/UESTC-FNG/community.git
- git install maven
- mvn clean compile package
- cp src/main/resource/application.properties src/main/resource/application-production.properties
- vim src/main/resource/application-production.properties
java -jar -Dspring.profiles.active=production target/community-0.0.1-SNAPSHOT.jar
##资料
[elastic社区](https://elasticsearch.cn/)   
[OAuth教程](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)
[bootstrap文档](https://v3.bootcss.com/getting-started)
[MyBatis整合Spring Boot](https://cloud.tencent.com/developer/article/1362818)
[thymeleaf](https://www.thymeleaf.org/)
##工具
[flyway](https://flywaydb.org/)
[lombok  @Data](https://projectlombok.org/)
[md editor](https://pandao.github.io/editor.md/)
[TencentCloud](https://cloud.tencent.com/)
[devtools]
[MyBatis Generator](https://mybatis.org/generator/running/runningWithMaven.html)
[LiveReload插件]
[postman](https://www.postman.com/)

##
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate

#
ezgrnX~2Y}6AU%TH
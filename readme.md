**Steps to setup the project**

1. using dependencyManagement to setup Spring Cloud version
2. adding alias to host file to enable eureka replicas alias
3. start eureka like: java -jar xx.jar --spring.profiles.active=reg1 and then reg2
4. start config server, access config like: http://localhost:8001/gateway-dev.yml
5. install jetty, create base dir like: java -jar "D:\Program Files\jetty-distribution-9.4.26.v20200117\start.jar" --add-to-start=http,deploy
6. modify start.ini like: jetty.http.port=9090, run jetty: java -jar "D:\Program Files\jetty-distribution-9.4.26.v20200117\start.jar"
7. access ui: http://localhost:9090/ui/
8. install lombok plugin
9. install scoop on windows
10. install rabbitmq via scoop: scoop bucket add extras scoop install rabbitmq; config erlang_home
11. start rabbitmq: rabbitmq-server -detached; enable web: rabbitmq-plugins enable rabbitmq_management; stop it: rabbitmqctl stop
12. login web using guest/guest from http://localhost:15672/
13. start gateway, note the CORS issue
14. start social-multiplication, gamification
15. enjoy game
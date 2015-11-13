rabbitmq-java-client
==================

Este projeto é um exemplo de como podemos utilizar o RabbitMQ com java. 
> Foi criado algumas classes base para configurar os Consumer e o Producers. Para testar a aplicação será utilizado um producer Standalone e como Consumer foi criado uma applicação que vai rodar dentro de um container web (Tomcat) como uma aplicação web, mas que possui apenas o backend. Também foi adicionado suporte ao Spring, para dar mais robustes a aplicação.

Colocando o projeto para rodar
----------

Instalação do RabbitMQ server

A instalação pode ser feita usando docker com o seguinte commnado um docker.

$ docker run -d --hostname my-host-rabbit --name my-rabbit -p 15672:15672 -p 5672:5672 rabbitmq:3-management

Visualizando logs

$ docker logs my-rabbit

Após a instalção do RabbitMQ, siga os seguintes passos:
- Faça um clone do projeto
- Adicione na IDE de sua preferência
- Baixe as dependências com o Maven
- Altere a configurações do servidor na classe "br.com.msilveira.mq.core.ClientSettings
- Suba o servidor web
- Rode o ProducerStandalone.
- ;)
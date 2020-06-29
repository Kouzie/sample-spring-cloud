# sample-spring-cloud

`Hoxton.SR4` 기반 spring cloud 예제   

`account`, `customer` `ribbon` 을 사용한 client side load balancing 지원
`order` `feign` 을 사용한 client side load balancing

`spring gateway` 를 사용한 `reverse proxy`


## install zipkin

```$xslt
docker run -d -p 9411:9411 openzipkin/zipkin
```
## install ELK

> https://github.com/deviantony/docker-elk

### disable paid features

`elasticsearch.yml`  
`xpack.license.self_generated.type: trial` ->  
`xpack.license.self_generated.type: basic`  

```$xslt
ELK_VERSION=7.6.2
docker-compose up -d 
```

## install rabitMq

```$xslt
docker run -d --name rabbit -p 5672:5672 -p 15672:15672 rabbitmq:management
```
> http://localhost:15672/

guest/guest 로그인 

## install pact

```$xslt

$ docker run -d --name postgres -p  5432:5432 \
-e POSTGRES_USER=oauth -e POSTGRES_PASSWORD=oauth123 -e POSTGRES_DB=oauth postgres

$ docker run -d --name pact-broker --link postgres:postgres -p 9292:9292 \
-e PACT_BROKER_DATABASE_USERNAME=oauth \
-e PACT_BROKER_DATABASE_PASSWORD=oauth123 \
-e PACT_BROKER_DATABASE_HOST=postgres \
-e PACT_BROKER_DATABASE_NAME=oauth pactfoundation/pact-broker
d9f90ea83a58458dd515c542dcc8d0c9c9b7078b01730630c2779a3ca8ec4fa9
```

### hystrix 접근

> http://127.0.0.1:19000/hystrix
> localhost:19000/turbine.stream
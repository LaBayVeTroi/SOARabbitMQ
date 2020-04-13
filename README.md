# SOA BTL 2

Use rabbitmq and jsoup for distributed crawl app
Users send link of a website to crawler and receive the title of that website 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

```
Docker 

Java 
```

### Installing

Create docker network for easy connection

```
docker network create -d bridge rabbitmq-network
```

Run docker container of rabbimq-server

```
docker run -d -p 15672:15672 --network rabbitmq-server -h rabbitmq-host --name rabbitmq-host rabbitmq:3-management
```

Create docker images of crawler

```
Go to crawler/src

docker build -t rabbitmq-crawler .
```

Run docker container of crawler

```
docker run --network rabbitmq-network rabbitmq-crawler
```

Create docker images of virtual user

```
Go to user/src

docker build -t rabbitmq-user .
```

Run docker container of virtual user

```
docker run -it --network rabbitmq-network rabbitmq-user
```

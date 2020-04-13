FROM java:8-jdk-alpine
COPY ./ /usr/app
WORKDIR /usr/app
CMD javac -cp .:ExJar/amqp-client-5.8.0.jar:ExJar/jsoup-1.13.1.jar:ExJar/org.slf4j.jar:ExJar/slf4j-simple-1.6.1.jar Main.java && java -cp .:ExJar/amqp-client-5.8.0.jar:ExJar/jsoup-1.13.1.jar:ExJar/org.slf4j.jar:ExJar/slf4j-simple-1.6.1.jar Main
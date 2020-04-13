FROM java:8-jdk-alpine
COPY ./ /usr/app
WORKDIR /usr/app/src
CMD javac -cp .:../lib/jsoup-1.13.1.jar TestJsoup.java && java -cp .:../    lib/jsoup-1.13.1.jar TestJsoup
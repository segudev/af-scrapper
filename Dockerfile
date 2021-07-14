FROM ghcr.io/graalvm/graalvm-ce:latest

RUN gu install native-image

COPY ./target/af-back-standalone.jar /usr/app.jar

RUN native-image --report-unsupported-elements-at-runtime \
             --initialize-at-build-time \
             --no-server \
             -jar /usr/app.jar \
             -H:Name=/usr/app

CMD ["/usr/app"]
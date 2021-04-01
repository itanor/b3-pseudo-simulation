FROM openjdk:14-jdk

# Add the user UID:999, GID:999, home at /app
RUN groupadd -r app -g 999 && \
    useradd -u 999 -r -g app -m -d /app -s /sbin/nologin -c "App user" app && \
    chmod 755 /app

WORKDIR /app

USER app

COPY . /app

EXPOSE 10000 20000

CMD [\
  "java",\
  "-jar",\
  "target/b3service-0.0.1-SNAPSHOT.jar",\
  "stock"\
]

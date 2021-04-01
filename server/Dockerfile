FROM sayden/zeromq-go

RUN groupadd -r app -g 999 && \
    useradd -u 999 -r -g app -m -d /app -s /sbin/nologin -c "App user" app && \
    chmod 755 /app

ARG repSocketUrl
ARG pubSocketUrl
ARG threads=10

ENV REP_SOCKET_URL=${repSocketUrl} \
    PUB_SOCKET_URL=${pubSocketUrl} \
    THREADS=${threads}

RUN go get github.com/pebbe/zmq4

WORKDIR /app

USER app

COPY . /app

RUN mkdir /app/bin && \
    go build \
        -o bin/req-to-pub-publisher \
        /app/src/main/args.go \
        /app/src/main/socket-proxy.go \
        /app/src/main/socket-options.go \
        /app/src/main/req-to-pub-publisher.go

EXPOSE 10000 20000

CMD [\
  "/bin/bash",\
  "-c",\
  "./bin/req-to-pub-publisher ${REP_SOCKET_URL} ${PUB_SOCKET_URL} ${THREADS}"\
]

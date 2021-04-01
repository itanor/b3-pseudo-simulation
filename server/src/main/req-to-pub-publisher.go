package main

import (
  zmq "github.com/pebbe/zmq4"
)

func main() {
  if insufficientArgs() {
    insufficientArgsMessage()
    return
  }

  repSocketUrl, pubSocketUrl, threads := extractUrlsFromArgs()
  printUrls(repSocketUrl, pubSocketUrl)

  publishers := ProxyConfig{
    front: zmq.XSUB,
    back: zmq.XPUB,
    control: nil,
    backSocketUrl: pubSocketUrl,
    backType: zmq.PUB,
  }
  repliers := ProxyConfig{
    front: zmq.ROUTER,
    back: zmq.DEALER,
    control: nil,
    frontSocketUrl: repSocketUrl,
  }

  serve(publishers, repliers, threads)
}

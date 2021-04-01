package main

import (
  zmq "github.com/pebbe/zmq4"
)

func setKeepAlive(socket *zmq.Socket) {
  socket.SetTcpKeepalive(1)
  socket.SetTcpKeepaliveIdle(300)
  socket.SetTcpKeepaliveIntvl(300)
}

func setHWM(socket *zmq.Socket) {
  socket.SetRcvhwm(10000)
  socket.SetSndhwm(10000)
}

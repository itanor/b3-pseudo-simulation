package main

import (
  zmq "github.com/pebbe/zmq4"
)

type ProxyConfig struct {
  front          zmq.Type
  back           zmq.Type
  control        *zmq.Socket
  backSocketUrl  string
  frontSocketUrl string
  backType       zmq.Type
}

func serve(backCfg ProxyConfig, frontCfg ProxyConfig, threads int64) {
  backIpc := "ipc://back.ipc"
  go backProxy(
    backCfg.backSocketUrl,
    backIpc,
    backCfg.front,
    backCfg.back,
    backCfg.control,
  )
  frontIpc := "ipc://front.ipc"
  for i := 0; i < int(threads); i++ {
    go communicate(frontIpc, backIpc, backCfg.backType)
  }
  frontProxy(
    frontCfg.frontSocketUrl,
    frontIpc,
    frontCfg.front,
    frontCfg.back,
    frontCfg.control,
  )
}

func frontProxy(
  frontUrl string,
  ipc string,
  frontType zmq.Type,
  backType zmq.Type,
  controlSocket *zmq.Socket) {

  front, _ := zmq.NewSocket(frontType)
  setKeepAlive(front)
  setHWM(front)
  front.Bind(frontUrl)
  defer front.Close()

  back, _ := zmq.NewSocket(backType)
  setKeepAlive(back)
  setHWM(back)
  back.Bind(ipc)
  defer back.Close()

  zmq.Proxy(front, back, controlSocket)
}

func backProxy(
  backUrl string,
  ipc string,
  frontType zmq.Type,
  backType zmq.Type,
  controlSocket *zmq.Socket) {

  front, _ := zmq.NewSocket(frontType)
  setKeepAlive(front)
  setHWM(front)
  front.Bind(ipc)
  defer front.Close()

  back, _ := zmq.NewSocket(backType)
  setKeepAlive(back)
  setHWM(back)
  back.Bind(backUrl)
  defer back.Close()

  zmq.Proxy(front, back, controlSocket)
}

func communicate(frontIpc string, backIpc string, socketType zmq.Type) {
  front, _ := zmq.NewSocket(zmq.REP)
  setKeepAlive(front)
  setHWM(front)
  front.Connect(frontIpc)
  defer front.Close()

	back, _ := zmq.NewSocket(socketType)
  setKeepAlive(back)
  setHWM(back)
  back.Connect(backIpc)
  defer back.Close()

  for {
    received, _ := front.Recv(0)
    front.Send("ok", 0)

    back.Send(received, 0)
  }
}

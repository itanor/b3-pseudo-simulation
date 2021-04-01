package main

import (
  "os"
  "log"
  "strconv"
)

func insufficientArgs() bool {
  return len(os.Args) < 4
}

func extractUrlsFromArgs() (frontSocketUrl string, backSocketUrl string, threads int64) {
  threads, cannotParse := strconv.ParseInt(os.Args[3], 10, 0)
  if cannotParse != nil {
    log.Println("Cannot parse thread's number! Exiting...")
    os.Exit(1)
  }
  return string(os.Args[1]), string(os.Args[2]), threads
}

func insufficientArgsMessage() {
  log.Println("Insufficient arguments!")
  log.Println("Usage  : ./publisher frontSocketUrl backSocketUrl")
  log.Println("Example: ./publisher tcp://*:10000 tcp://*:20000")
}

func printUrls(frontSocketUrl string, backSocketUrl string) {
  log.Println("Listening...")
  log.Println("frontSocketUrl :", frontSocketUrl)
  log.Println("backSocketUrl:", backSocketUrl)
}

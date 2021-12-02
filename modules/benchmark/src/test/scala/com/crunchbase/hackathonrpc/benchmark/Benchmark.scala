package com.crunchbase.hackathonrpc.benchmark

object Benchmark {
  val host = sys.env.getOrElse("HOST", "http://localhost:8080")
}

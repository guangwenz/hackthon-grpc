package com.crunchbase.hackathonrpc.benchmark

object Benchmark {
  val host = sys.env.getOrElse("HOST", "http://localhost:8080")
  val userCount = sys.env.getOrElse("USER_COUNT", "100").toInt
  val userRequests = sys.env.getOrElse("USER_REQUESTS", "1000").toInt
}

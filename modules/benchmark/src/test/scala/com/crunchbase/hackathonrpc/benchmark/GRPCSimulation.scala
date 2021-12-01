package com.crunchbase.hackathongrpc.benchmark

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class GRPCSimulation extends Simulation {
  val httpProtocol = http.baseUrl("http://localhost:8080")

  val rpc = scenario("RPC").exec(repeat(1000, "n") {
    exec(http("grpc").get("/rpc/hello/test#{n}"))
  })

  setUp(rpc.inject(atOnceUsers(100)))
    .protocols(httpProtocol)
}

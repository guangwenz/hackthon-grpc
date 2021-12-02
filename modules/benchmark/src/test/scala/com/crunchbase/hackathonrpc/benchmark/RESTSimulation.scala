package com.crunchbase.hackathonrpc.benchmark

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RESTSimulation extends Simulation {
  val httpProtocol = http.baseUrl(Benchmark.host)

  val rest = scenario("REST").exec(repeat(1000, "n") {
    exec(http("rest").get("/rest/hello/test#{n}"))
  })

  setUp(rest.inject(atOnceUsers(100)).protocols(httpProtocol))
}

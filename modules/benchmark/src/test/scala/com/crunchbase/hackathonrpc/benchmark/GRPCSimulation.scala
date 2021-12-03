package com.crunchbase.hackathonrpc.benchmark

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class GRPCSimulation extends Simulation {
  val httpProtocol = http.baseUrl(Benchmark.host)

  val rpc = scenario("RPC").exec(repeat(Benchmark.userRequests, "n") {
    exec(http("grpc").get("/rpc/hello/test#{n}"))
  })
  val rest = scenario("REST GET").exec(repeat(Benchmark.userRequests, "n") {
    exec(http("rest-get").get("/rest/hello/test#{n}"))
  })
  val restPost =
    scenario("REST POST").exec(repeat(Benchmark.userRequests, "n") {
      exec(http("rest-post").get("/rest-post/hello/test#{n}"))
    })

  setUp(
    rpc.inject(atOnceUsers(Benchmark.userCount)),
    rest.inject(atOnceUsers(Benchmark.userCount)),
    restPost.inject(atOnceUsers(Benchmark.userCount))
  )
    .protocols(httpProtocol)
}

// package com.crunchbase.hackathonrpc.benchmark

// import io.gatling.core.Predef._
// import io.gatling.http.Predef._

// class RESTPostSimulation extends Simulation {
//   val httpProtocol = http.baseUrl(Benchmark.host)

//   val rest = scenario("REST-POST").exec(repeat(Benchmark.userRequests, "n") {
//     exec(http("rest").get("/rest-post/hello/test#{n}"))
//   })

//   setUp(rest.inject(atOnceUsers(Benchmark.userCount)).protocols(httpProtocol))
// }

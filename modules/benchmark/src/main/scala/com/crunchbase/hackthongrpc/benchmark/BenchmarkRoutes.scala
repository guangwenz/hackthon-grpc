package com.crunchbase.hackthongrpc.benchmark

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object BenchmarkRoutes {

  def routes[F[_]:Sync](greetClient: GreetClient[F]):HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "rpc" / "hello" / name =>
        greetClient.grpc(name).flatMap(Ok(_))

      case GET -> Root / "rest" / "hello" / name =>
        greetClient.rest(name).flatMap(Ok(_))
    }
  }
}

package com.crunchbase.hackthongrpc

import cats.effect.{Async, Resource}
import cats.syntax.all._
import com.comcast.ip4s._
import fs2.Stream
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object HackthongrpcServer {
  def stream[F[_] : Async]: Stream[F, Nothing] = {
    val helloWorldAlg = HelloWorld.impl[F]
    val httpApp = (
      HackthongrpcRoutes.helloWorldRoutes[F](helloWorldAlg)
      ).orNotFound
    val finalHttpApp = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)
    Stream.resource(
      EmberServerBuilder.default[F]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"5555")
        .withHttpApp(finalHttpApp)
        .build >> Resource.eval(Async[F].never)
    ).drain
  }
}

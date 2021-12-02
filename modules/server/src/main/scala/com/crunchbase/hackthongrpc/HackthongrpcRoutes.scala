package com.crunchbase.hackthongrpc

import cats.effect.Async
import cats.implicits._
import io.circe.Decoder
import io.circe.generic.semiauto._
import org.http4s.circe._
import org.http4s.{EntityDecoder, HttpRoutes}
import org.http4s.dsl.Http4sDsl

object HackthongrpcRoutes {
  final case class GreetRequest(name:String)
  implicit val decoder: Decoder[GreetRequest] = deriveDecoder

  def helloWorldRoutes[F[_]: Async](H: HelloWorld[F]): HttpRoutes[F] = {
    implicit val entityDecoder: EntityDecoder[F, GreetRequest] = jsonOf[F, GreetRequest]
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting)
        } yield resp
      case req@POST -> Root / "hello" =>
        for {
          req <- req.as[GreetRequest]
          greeting <- H.hello(HelloWorld.Name(req.name))
          resp <- Ok(greeting)
        } yield resp
    }
  }
}
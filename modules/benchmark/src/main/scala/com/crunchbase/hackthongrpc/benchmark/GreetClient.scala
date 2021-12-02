package com.crunchbase.hackthongrpc.benchmark

import cats.effect.kernel.Async
import cats.implicits._
import com.crunchbase.protos.greet.{GreeterFs2Grpc, HelloRequest}
import io.circe.Decoder
import io.circe.generic.semiauto._
import io.circe.literal._
import io.grpc.Metadata
import org.http4s.Method._
import org.http4s._
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl

trait GreetClient[F[_]] {
  def rest(name: String): F[String]

  def restPost(name: String): F[String]

  def grpc(name: String): F[String]
}

object GreetClient {
  def apply[F[_] : Async](client: Client[F], greeterFs2Grpc: GreeterFs2Grpc[F, Metadata]): GreetClient[F] = {
    new GreetClient[F] {
      implicit val decoder: Decoder[GreetResponse] = deriveDecoder[GreetResponse]
      implicit val entityDecoder: EntityDecoder[F, GreetResponse] = jsonOf[F, GreetResponse]

      val dsl: Http4sClientDsl[F] = new Http4sClientDsl[F] {}

      import dsl._

      override def rest(name: String): F[String] = {
        for {
          resp <- client.expect[GreetResponse](GET(Uri.unsafeFromString(s"http://localhost:5555/hello/$name")))
        } yield resp.message
      }

      override def restPost(name: String): F[String] = for {
        resp <- client.expect[GreetResponse](POST(Uri.unsafeFromString(s"http://localhost:5555/hello")).withEntity(json"""{"name":$name}"""))
      } yield resp.message

      override def grpc(name: String): F[String] = {
        for {
          resp <- greeterFs2Grpc.sayHello(HelloRequest(name), new Metadata())
        } yield resp.message
      }
    }
  }

  final case class GreetResponse(message: String)
}
package com.crunchbase.hackthongrpc.benchmark

import cats.effect._
import cats.implicits._
import com.comcast.ip4s._
import com.crunchbase.protos.ping.GreeterFs2Grpc
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
import fs2.grpc.syntax.all._
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.Logger

object BenchmarkApp extends IOApp{
  def server[F[_]:Async]() = {
    for {
      client <- EmberClientBuilder.default[F].build
      channel <- NettyChannelBuilder.forAddress("127.0.0.1",9999).usePlaintext().resource[F]
      stub <- GreeterFs2Grpc.stubResource[F](channel)
      greetCli = GreetClient(client, stub)
      finalHttp = Logger.httpApp(logHeaders = true,logBody = true)(BenchmarkRoutes.routes(greetCli).orNotFound)
      server <- EmberServerBuilder.default[F].withHost(ipv4"0.0.0.0").withPort(port"8080").withHttpApp(finalHttp).build
    } yield server
  }
  override def run(args: List[String]): IO[ExitCode] = {
    fs2.Stream.resource(server[IO]() >> Resource.eval(IO.never)).compile.drain.as(ExitCode.Success)
  }
}

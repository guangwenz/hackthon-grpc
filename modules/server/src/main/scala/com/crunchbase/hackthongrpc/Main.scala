package com.crunchbase.hackthongrpc

import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.crunchbase.protos.greet.{GreeterFs2Grpc, HelloReply, HelloRequest}
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
import fs2.grpc.syntax.all._
import io.grpc._

object Main extends IOApp {
  val grpcResource: Resource[IO, ServerServiceDefinition] = GreeterFs2Grpc.bindServiceResource[IO]((request: HelloRequest, _: Metadata) => IO(HelloReply(s"hello ${request.name}")))
  def run(service: ServerServiceDefinition): IO[Nothing] = NettyServerBuilder.forPort(9999).addService(service).resource[IO].evalMap(server => IO(server.start())).useForever

  def run(args: List[String]) = {
    fs2.Stream(
      fs2.Stream.eval(grpcResource.use(run)),
      HackthongrpcServer.stream[IO]
    ).parJoin(2).compile.drain.as(ExitCode.Success)
  }
}

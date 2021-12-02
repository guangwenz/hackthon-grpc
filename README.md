## Quick start

1. `sbt server/run` to run the server

2. `JAVA_OPTS="--add-opens java.base/jdk.internal.misc=ALL-UNNAMED -Dio.netty.tryReflectionSetAccessible=true" sbt benchmark/run` to run the benchmark api server
3. `sbt benchmark/Gatling/test` to start the gatling test


## Modules
### protobuf
define protobuf files and hold generated scala source code for them
### server
a simple server to greet people by name. It serves through both HTTP rest and gRPC

### benchmark
a simple benchmark server providing 2 APIs
1. `/rpc/hello/{name}` to consume greet service from `server` above through gRPC
2. `/rest/hello/{name}` to consume greet service from `server` above through RESTful HTTP API


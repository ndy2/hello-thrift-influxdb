import com.twitter.finagle.Thrift
import com.twitter.util.{Await, Future}
import hello.PingService

import java.io

object DemoClient extends App {

  private val client: PingService.MethodPerEndpoint = Thrift.client
    .withLabel("demo-thrift-client")
    .build[PingService.MethodPerEndpoint](s"localhost:8888")

  private val value: Future[io.Serializable] = client
    .ping()

  println(Await.result(value))
}

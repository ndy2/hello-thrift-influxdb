import DemoServer.statsReceiver
import com.google.inject.Singleton
import com.twitter.finagle.stats.Counter
import com.twitter.util.Future
import hello.MyThriftException

@Singleton
class PingService {

  val counter: Counter = statsReceiver
    .scope("srv", "demo")
    .counter("counter")

  def ping(): Future[String] = {
    println("ping")

    counter.incr()
    Future.value("pong")
  }

  def counter(name: String): Future[String] = {
    println("counter")

    statsReceiver
      .scope("srv", "demo")
      .counter(name)
      .incr()

    Future.value("counter")
  }

  def throwMteDeclared(): Future[String] = {
    println("throwMteDeclared")
    throw new MyThriftException("throwMteDeclared")
  }
}

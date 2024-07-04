import com.google.inject.{Inject, Singleton}
import com.twitter.util.Future
import hello.PingService

@Singleton
class PingMethodEndPoint @Inject()(service: PingService) extends PingService.MethodPerEndpoint {

  override def ping(): Future[String] = service.ping()

  override def counter(name: String): Future[String] = service.counter(name)

  override def throwMteDeclared(): Future[String] = service.throwMteDeclared()
}


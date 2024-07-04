import com.codahale.metrics.MetricFilter
import com.twitter.finagle.ThriftMux
import com.twitter.finagle.metrics.MetricsStatsReceiver
import com.twitter.inject.server.TwitterServer
import metrics.DemoMetric
import metrics_influxdb.{HttpInfluxdbProtocol, InfluxdbReporter}

import java.net.InetAddress
import java.util.concurrent.TimeUnit

object DemoServer extends TwitterServer with DemoMetric {

  private val pingService = new PingService
  private val pingMethodEndPoint = new PingMethodEndPoint(pingService)
  private val server: ThriftMux.Server = ThriftMux.server


  private val clntReporter =
    InfluxdbReporter
      .forRegistry(MetricsStatsReceiver.metrics)
      .protocol(new HttpInfluxdbProtocol(
        "localhost",
        8086,
        null,
        null,
        "metrics"
      ))
      .tag("service", "demo-server")
      .tag("hostname", InetAddress.getLocalHost.getHostName)
      .filter(MetricFilter.startsWith("clnt"))
      .build()

  private val thriftReporter =
    InfluxdbReporter
      .forRegistry(MetricsStatsReceiver.metrics)
      .protocol(new HttpInfluxdbProtocol(
        "localhost",
        8086,
        null,
        null,
        "metrics"
      ))
      .tag("service", "demo-server")
      .tag("hostname", InetAddress.getLocalHost.getHostName)
      .filter(MetricFilter.startsWith("srv.thrift"))
      .build()

  premain {
    influxDbReporterMap("default-influxdb-reporter").stop()
    for (i <- 0 to 5) {
      Thread.sleep(1000)
      println("sleep...")
    }

    println("thriftReporter & clntReporter starts at same time")
    thriftReporter.start(5, TimeUnit.SECONDS)
    clntReporter.start(10, TimeUnit.SECONDS)
    println("thriftReporter & clntReporter started at same time")
  }

  override protected def start(): Unit = {
    await(server.serveIface("localhost:8888", pingMethodEndPoint)
    )
  }
}

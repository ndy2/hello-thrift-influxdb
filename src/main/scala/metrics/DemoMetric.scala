package metrics

import com.codahale.metrics.ScheduledReporter
import com.twitter.app.{App, LoadService}
import com.twitter.finagle.metrics.MetricsStatsReceiver
import metrics_influxdb.{HttpInfluxdbProtocol, InfluxdbReporter}

import java.net.InetAddress
import java.util.concurrent.TimeUnit
import scala.collection.mutable

trait DemoMetric {
  self: App =>
  val influxDbReporterMap = mutable.Map[String, ScheduledReporter]()

  premain {
    val reporter: ScheduledReporter = InfluxdbReporter
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
      .build()
    reporter.start(5, TimeUnit.SECONDS)
    Thread.sleep(20000)
    influxDbReporterMap.put("default-influxdb-reporter", reporter)
  }
}

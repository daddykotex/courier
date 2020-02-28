package courier

import courier._, Defaults._
import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * sbt:root>examples/runMain courier.SimpleExample
  */
object SimpleExample extends App with ExampleSetup {
    val (creds, mailer) = setup()

    val futSend = mailer(Envelope.from(creds.username.addr)
        .to(creds.username.addr)
        .subject("simple")
        .content(Text("hello there")))

    Await.result(futSend, 10.seconds)
}
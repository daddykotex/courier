package courier

import courier._, Defaults._
import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * sbt:root>examples/runMain courier.MultipartExample /Users/david/Desktop/courier.jpg
  */
object MultipartExample extends App with ExampleSetup {
    val maybeFilePath = args.headOption
    val filePath = maybeFilePath.getOrElse { throw new RuntimeException("a file must be provided as an argument to the main") }

    val (creds, mailer) = setup()

    val imageFile = new java.io.File(filePath);
    println(imageFile.getAbsolutePath())
    val futSend = mailer(Envelope.from(creds.username.addr)
        .to(creds.username.addr)
        .subject("multipart")
        .content(Multipart()
           .attach(imageFile)
           .html("<html><body><h1>Multipart</h1></body></html>")))

    Await.result(futSend, 10.seconds)
}
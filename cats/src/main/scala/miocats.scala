package courier.miocats

import cats.effect.IO
import courier.MailerIO

object CatsMailerIO {
  implicit val catsMailerIO: MailerIO[IO] = new MailerIO[IO] {
    def run(f: => Unit): IO[Unit] = IO { f }
  }
}

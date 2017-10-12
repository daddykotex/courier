package com.github.daddykotex.courier.miocats

import cats.effect.IO
import com.github.daddykotex.courier.MailerIO

object CatsMailerIO {
  implicit val catsMailerIO: MailerIO[IO] = new MailerIO[IO] {
    def run(f: => Unit): IO[Unit] = IO { f }
  }
}

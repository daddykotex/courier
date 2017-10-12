package com.github.daddykotex.courier

import javax.mail.{Message, Session => MailSession, Transport}
import java.util.Properties
import javax.mail.internet.MimeMessage

object Mailer {
  def apply(host: String, port: Int): Session.Builder =
    Mailer().session.host(host).port(port)
}

case class Mailer(_session: MailSession = MailSession.getDefaultInstance(new Properties())) {
  def session = Session.Builder(this)

  def apply[F[_]](e: Envelope)(implicit mailerIO: MailerIO[F]): F[Unit] = {
    val msg = new MimeMessage(_session) {
      e.subject.foreach {
        case (subject, Some(charset)) => setSubject(subject, charset.name())
        case (subject, None) => setSubject(subject)
      }
      setFrom(e.from)
      e.to.foreach(addRecipient(Message.RecipientType.TO, _))
      e.cc.foreach(addRecipient(Message.RecipientType.CC, _))
      e.bcc.foreach(addRecipient(Message.RecipientType.BCC, _))
      e.replyTo.foreach(a => setReplyTo(Array(a)))
      e.headers.foreach(h => addHeader(h._1, h._2))
      e.contents match {
        case Text(txt, charset) => setText(txt, charset.displayName)
        case mp: Multipart => setContent(mp.parts)
      }
    }
    mailerIO.run(Transport.send(msg))
  }
}

trait MailerIO[F[_]] {
  /* Note that f here is blocking */
  def run(f: => Unit): F[Unit]
}

object MailerIO {
  import scala.concurrent.{blocking, ExecutionContext, Future}
  implicit def futureMailerIO(implicit ec: ExecutionContext): MailerIO[Future] = new MailerIO[Future] {
    def run(f: => Unit): Future[Unit] = Future {
      blocking { f }
    }
  }
}

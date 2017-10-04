We start with some basic imports:

```scala
scala> import com.github.daddykotex.courier._
import com.github.daddykotex.courier._

scala> import com.github.daddykotex.courier.Defaults._
import com.github.daddykotex.courier.Defaults._
```

Then you define a mailer instance and an email to send:

```scala
scala> val mailer: Mailer = Mailer("smtp.gmail.com", 587).auth(true).as("you@gmail.com", "p@$$w0rd").startTtls(true)()
mailer: com.github.daddykotex.courier.Mailer = Mailer(javax.mail.Session@11311528)

scala> val email: Envelope = Envelope.from("you" `@` "gmail.com").to("mom" `@` "gmail.com").cc("dad" `@` "gmail.com").subject("miss you").content(Text("hi mom"))
email: com.github.daddykotex.courier.Envelope = Envelope(you@gmail.com,Some((miss you,None)),List(mom@gmail.com),List(dad@gmail.com),List(),None,None,List(),Text(hi mom,UTF-8))
```

Then you send it:
```scala
scala> import scala.concurrent.Future
import scala.concurrent.Future

scala> val eventualMailToMom: Future[Unit] =
     |   mailer(email)
eventualMailToMom: scala.concurrent.Future[Unit] = Future(<not completed>)
```

If you want to use an alternate effect type, you just need to implement a `MailerIO` instance:
```scala
scala> type Id[A] = A
defined type alias Id

scala> implicit val idMailerIO: MailerIO[Id] = new MailerIO[Id] {
     |     def run(f: => Unit): Id[Unit] = Unit
     | }
idMailerIO: com.github.daddykotex.courier.MailerIO[Id] = $anon$1@3e1044d9

scala> val otherEffectType: Id[Unit] = mailer(email)
otherEffectType: Id[Unit] = ()
```

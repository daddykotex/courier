We start with some basic imports:

```tut
import courier._, Defaults._
```

Then you define a mailer instance and an email to send:

```tut
val mailer: Mailer = Mailer("smtp.gmail.com", 587).auth(true).as("you@gmail.com", "p@$$w0rd").startTtls(true)()

val email: Envelope = Envelope.from("you" `@` "gmail.com").to("mom" `@` "gmail.com").cc("dad" `@` "gmail.com").subject("miss you").content(Text("hi mom"))

```

Then you send it:
```tut
import scala.concurrent.Future
val eventualMailToMom: Future[Unit] =
  mailer(email)
```

If you want to use an alternate effect type, you just need to implement a `MailerIO` instance:
```tut
type Id[A] = A
implicit val idMailerIO: MailerIO[Id] = new MailerIO[Id] {
    def run(f: => Unit): Id[Unit] = Unit
}
val otherEffectType: Id[Unit] = mailer(email)
```
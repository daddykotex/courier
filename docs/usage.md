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
mailer: com.github.daddykotex.courier.Mailer = Mailer(javax.mail.Session@47daaed7)

scala> val email: Envelope = Envelope.from("you" `@` "gmail.com").to("mom" `@` "gmail.com").cc("dad" `@` "gmail.com").subject("miss you").content(Text("hi mom"))
email: com.github.daddykotex.courier.Envelope = Envelope(you@gmail.com,Some((miss you,None)),List(mom@gmail.com),List(dad@gmail.com),List(),None,None,List(),Text(hi mom,UTF-8))
```

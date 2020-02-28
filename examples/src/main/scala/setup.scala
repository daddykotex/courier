package courier

import courier._

final case class Creds(username: String, password: String)

trait ExampleSetup extends App {
    def setup(): (Creds, Mailer) = {
        val maybeCreds = for {
            u <- sys.env.get("SMTP_USERNAME")
            p <- sys.env.get("SMTP_PASSWORD")
        } yield Creds(username = u, password = p)
    
        val creds = maybeCreds.getOrElse { throw new RuntimeException("Please define SMTP_USERNAME and SMTP_PASSWORD")}
    
        val mailer = Mailer("smtp.gmail.com", 587)
                   .auth(true)
                   .as(creds.username, creds.password)
                   .startTls(true)()
        (creds, mailer)
    }
}
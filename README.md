# courier

This is a fork from [softprops/courier](https://github.com/softprops/courier). There are 2 reasons for the fork:

1. the project is barely maintained anymore
1. courier forces you to use Scala's `Future`

## Use your own effect type

The original courier forces the user to use a `scala.concurrent.Future` when you send the mail. This can be problematic when you are working on a purely functional codebase and you would like to avoid `Future`.

This version introduces a type constructor along with a type class to let you choose what you want to handle to wrap the asynchronous nature of sending mails with courier.

## usage

See (useage.md)[./docs/usage.md]

deliver electronic mail via gmail






# ThymeLeaf WebMVC Form navigation example

This repository contains an example application that showcases the following features:

http://localhost:8080/quote/create presents a screen to enter an `Author` and `Text` to submit a `Quote`.

On failure (i.e. domain validation failed), the user remains on the same screen, with the originally filled in fields. And a message is shown explaining something went wrong.

On success (i.e. domain validation succeeded), the user is redirected to the detail screen of the saved `Quote` : http://localhost:8080/quote/{id}. And a message is shown that the `Quote` was successfully saved.

Upon refresh of the success page (or navigating directly to the quote with saved id), the message disappears.

## FYI

_Jaimie_ is a black-listed author, you can try entering a quote by _Gianni_ to trigger a `DomainValidationException`.
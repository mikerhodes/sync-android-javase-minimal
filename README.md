# Cloudant Sync - JavaSE sample

This is a minimal app which will replicate a database onto a desktop machine
using the [Cloudant Sync for Android and JavaSE](https://github.com/cloudant/sync-android) 
library.

It's main purpose is to demo the library, but also to provide a base for
anyone finding bugs in the library -- it's hopefully easy to add code into
the example which demonstrates a bug to help us (the library devs) to
reproduce the problem and fix it.

## Pre-requisites

1. OS X, included `.dylib` is OS X only.
1. Java SE v7 or above.

## Getting started

First, make sure you can run the existing code -- this will tease out any
issues in your environment.

1. Open `src/main/java/bugrepro/Main.java`. 
2. Change the `datastoreManagerPath` variable to an _existing_ folder on
   disk.
3. Change the `remoteURL` variable to a database to pull from. Add credentials
   if required.
4. Save `Main.java` and run using `./gradlew run`.

You should see gradle being downloaded, the app being built and finally a run
of the app. If that all works, proceed to adding code to reproduce the bug
you've found!
   

## Reproducing

1. Fill in your code in `src/main/java/bugrepro/Main.java`.
2. Run using `./gradlew run`.

# Introduction
This is a reactive mongo demo app.

# Prequisites
MongoDB is used for storage.

## Install MongoDB
[Follow installation instructions for your platform](http://docs.mongodb.org/manual/installation/)

## Configure MongoDB
Start server with `mongod --dppath <some_dir>`

# How to run
Unzip _tvi-back-office-1.0-SNAPSHOT.zip_ anywhere and run `bin/tvi-back-office`

## Test run
Create random SCPP by running `python scpp.py http://localhost:9000/transactions`

## Additional Configuration
You can change the configuration, e.g. the MongoDB endpoint, in `conf/application.conf`. The default configuration resembles MongoDB's default configuration, so it should work out of the box.

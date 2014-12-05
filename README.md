# Introduction
This is a simple TVI back office API. It supports:

* SCPP messages on `/transactions` by HTTP POST (application/json)
* Retrieving current stored SCPP messages on `/transactions` by HTTP GET
* Tariff messages on `/tariffs` by HTTP POST (application/json)
* Retrieving the current tariff on `/tariffs/current` by HTTP GET
* Getting a transactions overview as CSV on `/transactions.csv` by HTTP GET
* Getting a charge sessions overview as CSV on `/chargeSessionsOverview.csv` by HTTP GET

# Prequisites
The TVI back office usage MongoDB for it's storage.

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

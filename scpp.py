#!/usr/bin/env/python
# -*- coding: utf-8 -*-


from datetime import datetime, timedelta
from random import random, choice
from re import sub
from time import sleep
from urllib2 import urlopen, Request
import json
import sys


seconds_in_day = 86400
customers = [u'pete', u'john', u'erik', u'lambert', u'cédric', u'Ярослав']

def scppMsg(user, starttime, endtime, kwh):
    return json.dumps({'customerId': user,
                       'startTime': iso8601ZFormat(starttime),
                       'endTime': iso8601ZFormat(endtime),
                       'volume': kwh})

def iso8601ZFormat(time):
    return sub('\.\d+$', 'Z', time.isoformat())

if len(sys.argv) != 2:
    sys.stderr.write("Usage: scpp.py <backoffice URL>\n")
    sys.exit(1)
backofficeUrl = sys.argv[1]

while 1:
    sleep(random())

    endtime = datetime.utcnow()
    duration = int(random() * seconds_in_day)
    starttime = endtime - timedelta(0, duration)
    user = choice(customers)
    kwh = random() * 85

    message = scppMsg(user, starttime, endtime, kwh)
    print "POSTing %s to %s" % (message, backofficeUrl)
    request = Request(url=backofficeUrl, data=message, headers={'Content-Type': 'application/json'})
    print "Response code: %d\n" % urlopen(request).getcode()

    # TODO:
    # encode as JSON
    # submit


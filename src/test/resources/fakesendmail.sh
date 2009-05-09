#!/bin/bash

# This is a totally preposterous script which, if called like sendmail (i.e.,
# with the -t command line flag, echos everything from stdin to a file) to a
# file named 'sent-mail.txt' in the current directory. This allows us to test
# sending error reports via the sendmail command without actually sending mail.
# 
# To wit:
#   echo "YAY FOR ME I AM HAPPY" | fakesendmail.sh
#   cat sent-mail.txt
#
# This script is called from SendmailErrorReporterTest.

if [ $1 == "-t" ]; then
    echo `cat /dev/stdin` > sent-mail.txt
    exit 0
fi
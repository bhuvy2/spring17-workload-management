#!/bin/bash

hosts=""
while read line; do
    hosts+=$line
    hosts+="\n"
done

from="shenoy.200@gmail.com"
subject="Worker Node Down"
message="Subject:$subject \n\nHello. The following worker node is no longer active. Please verify.\n\n$hosts"

echo -e $message | sudo /usr/sbin/sendmail -f shenoy.200@gmail.com shenoy.200@gmail.com

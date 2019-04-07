#!/bin/bash

function generate(){
    n=1
    while [ $n -le $3 ];do
            if [ $? -eq 0 ]
                then
                    let n++
		     project1=$2
                    curl $project1
            fi
    done
}

function fork(){
        count=1;
        while [ "$count" -le "$1" ]
        do
                generate $(($count)) $2 $3&
           
                count=$(( count+1 ))
        done
   }
if [ !${1} ]
then
echo ./test.sh [url] [concurrent_requests_number] [total_requests_number]
fi
   s=$((${3}/${2}))
 
   fork ${2} ${1} $s

    exit 0



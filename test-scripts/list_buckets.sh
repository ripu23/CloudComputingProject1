#!/usr/bin/env bash
BUCKET=${1}
if [ !${1} ]
then
echo usage: ./list_data.sh bucketname
fi
echo current bucket used: ${1}, change it if needed
KEYS=$(aws --region us-west-1 s3api list-objects --bucket ${1} --query 'Contents[*].[Key]' --output text)
if [[ $KEYS == "None" ]]
then
	echo bucket[${1}] is empty now
	exit
fi
KEYSVEC=($KEYS)

for key in ${KEYSVEC[@]}
do
		aws s3api get-object --bucket ${1} --key $key ./tmp_result > /dev/null
		echo [$key,$(cat ./tmp_result)]
		rm ./tmp_result
done


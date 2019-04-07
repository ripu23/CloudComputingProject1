#!/usr/bin/env bash
aws --region us-west-1 ec2 describe-instances --filters "Name=instance-state-code,Values=16,0" --query 'Reservations[*].Instances[*].[InstanceId]' --output text | wc -l

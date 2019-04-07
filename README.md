# CloudComputingProject1
An application that performs object detection using an AWS image of a deep learning model. 
It can scale automatically based on concurrent requests. The results are stored in S3. 
<br>

## Technologies: <br>
Backend: ```Java(Spring-boot)```. <br>
Cloud: ```AWS (EC2, SQS, S3)```.
<br>
This application was a course project for CSE-546 (Cloud Computing). <br>
## Working of the application:<br> 
1. User hits the static public url of the application (application runs on EC2). <br>
2. The app instance puts the request in the queue(SQS) and polls for the response in the output queue. <br>
3. The worker app running on other EC2 instances, polls the request in the input queue and performs
deep learning and detects the objects in the video.<br>
4. The results are put by the worker in the output queue as well as S3. <br>
5. The app instance polls the output queue continuously and when it receives the response, sends it back to
the user.<br>
(The app instance runs on one EC2 instance and starts and terminates the workers based on the 
number of requests.)

## Architecture:
![alt text](https://github.com/ripu23/CloudComputingProject1/blob/master/architecture.png)

package com.cloud.app.aws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import static java.util.Collections.singletonList;

import com.amazonaws.services.ec2.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cloud.app.config.AwsConfig;
import com.cloud.app.utility.Constants;

@Component
public class Ec2OperationsImpl implements Ec2Operations {

    @Autowired
    private AwsConfig awsConfig;

    public List<Instance> startInstances(int numInstances, String startupScript) {
        // Add a tag to identify this instance when counting number of instances
        TagSpecification tagSpecification = new TagSpecification()
                .withResourceType("instance")
                .withTags(singletonList(new Tag("Type", Constants.APP_INSTANCE_TAG)));

        IamInstanceProfileSpecification profile = new IamInstanceProfileSpecification()
                .withArn(Constants.APP_INSTANCE_PROFILE);

        RunInstancesRequest rir = new RunInstancesRequest()
                .withImageId(Constants.APP_AMI_ID)
                .withMinCount(numInstances)
                .withMaxCount(numInstances)
                .withInstanceType(Constants.APP_INSTANCE_TYPE)
                .withSecurityGroupIds(singletonList(Constants.SECURITY_GROUP_ID))
                .withTagSpecifications(tagSpecification)
                .withKeyName(Constants.APP_INSTANCE_KEYPAIR)
                .withIamInstanceProfile(profile)
                .withUserData(Base64.getEncoder().encodeToString(startupScript.getBytes()))
                .withInstanceInitiatedShutdownBehavior(ShutdownBehavior.Terminate);
        try {
            RunInstancesResult res = awsConfig.amazonEC2().runInstances(rir);
            return res.getReservation().getInstances();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Instance> startInstances(int numInstances) {
        return startInstances(numInstances, "");
    }

    public List<Instance> getAppInstances() {
        // Get a list of app instances by using the tag we applied as filter
        DescribeInstancesRequest request = new DescribeInstancesRequest()
                .withFilters(new Filter("tag:Type", singletonList(Constants.APP_INSTANCE_TAG)))
                .withFilters(new Filter("instance-state-name",
                        new ArrayList<>(Arrays.asList("pending", "running"))));
        DescribeInstancesResult response = awsConfig.amazonEC2().describeInstances(request);
        ArrayList<Instance> instances = new ArrayList<>();
        for(Reservation reservation : response.getReservations()) {
            for(Instance instance : reservation.getInstances()) {
                instances.add(instance);
            }
        }
        return instances;
    }

    public void setInstanceName(Instance instance, String name) {
        System.out.println("Naming instance: " + name);
        CreateTagsRequest createTagsRequest = new CreateTagsRequest()
                .withTags(singletonList(new Tag("Name", name)))
                .withResources(instance.getInstanceId());
        awsConfig.amazonEC2().createTags(createTagsRequest);
    }

}

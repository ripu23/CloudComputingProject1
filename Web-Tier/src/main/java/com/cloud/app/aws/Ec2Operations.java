package com.cloud.app.aws;

import com.amazonaws.services.ec2.model.Instance;

import java.util.List;

public interface Ec2Operations {

    List<Instance> startInstances(int numInstances, String startupScript);

    List<Instance> startInstances(int numInstances);

    List<Instance> getAppInstances();

    void setInstanceName(Instance instance, String name);

}

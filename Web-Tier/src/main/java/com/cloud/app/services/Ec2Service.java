package com.cloud.app.services;

import com.amazonaws.services.ec2.model.Instance;

import java.util.List;

public interface Ec2Service {

    List<Instance> startInstances(int numInstances);

    List<Instance> getAppInstances();

    void setInstanceName(Instance instance, String name);

}

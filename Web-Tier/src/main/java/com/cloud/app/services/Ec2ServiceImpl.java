package com.cloud.app.services;

import java.util.List;

import com.amazonaws.services.ec2.model.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.app.aws.Ec2Operations;

@Service
public class Ec2ServiceImpl implements Ec2Service {

    @Autowired
    private Ec2Operations ec2Operations;

    @Override
    public List<Instance> startInstances(int numInstances) {
        String startScript = "#!/bin/bash\n" +
                "cd /home/ubuntu/\n" +
                "java -jar /home/ubuntu/project1-app-tier-0.0.1-SNAPSHOT.jar";
        return ec2Operations.startInstances(numInstances, startScript);
    }

    @Override
    public List<Instance> getAppInstances() {
        return ec2Operations.getAppInstances();
    }

    @Override
    public void setInstanceName(Instance instance, String name) {
        ec2Operations.setInstanceName(instance, name);
    }

}

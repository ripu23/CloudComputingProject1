package com.cloud.app.services;

import java.util.*;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cloud.app.utility.Constants;

@Component
public class AutoScalerServiceImpl implements AutoScalerService {

    Logger logger = LoggerFactory.getLogger(AutoScalerServiceImpl.class);

    @Autowired
    private SqsService sqsService;

    @Autowired
    private Ec2Service ec2Service;

    private void setInstanceNames(List<Instance> newInstances, List<Instance> oldInstances) {
        // Tag instances using the names remaining in ALL_APP_INSTANCE_NAMES
        Set<String> usedNames = getInstanceNames(oldInstances);
        Set<String> remainingNames = new LinkedHashSet<>(Constants.ALL_APP_INSTANCE_NAMES);
        remainingNames.removeAll(usedNames); // this is how set difference works in Java!

//        logger.debug("all names      : " + Constants.ALL_APP_INSTANCE_NAMES);
        logger.debug("used names     : " + usedNames);
        logger.debug("remaining names: " + remainingNames);

        Iterator<String> namesIterator = remainingNames.iterator();
        for (Instance instance : newInstances) {
            ec2Service.setInstanceName(instance, namesIterator.next());
        }
    }

    private Set<String> getInstanceNames(List<Instance> instances) {
        Set<String> names = new HashSet<>();
        for (Instance instance : instances) {
            for (Tag tag : instance.getTags()) {
                if (tag.getKey().equals("Name")) {
                    names.add(tag.getValue());
                }
            }
        }
        return names;
    }

    @Override
    @Scheduled(fixedDelay = Constants.POLL_INTERVAL)
    public void handleScaling() {
        logger.info("Checking if scaling is needed..");
        List<Instance> existingInstances = ec2Service.getAppInstances();
        int numMsgs = sqsService.getNumMsgs();
        int numAppInstances = existingInstances.size();
        int numInstancesRequired = numMsgs - numAppInstances;
        int numInstancesCanStart = Constants.MAX_APP_INSTANCES - numAppInstances;

        logger.info("numMsgs              = " + numMsgs);
        logger.info("numAppInstances      = " + numAppInstances);
        logger.info("numInstancesRequired = " + numInstancesRequired);
        logger.info("numInstancesCanStart = " + numInstancesCanStart);

        // Start instances if have more messages than the instances and we still have capacity
        if (numInstancesRequired > 0 && numInstancesCanStart > 0) {
            int numInstancesToStart = Math.min(numInstancesRequired, numInstancesCanStart);
            logger.info("numInstancesToStart  = " + numInstancesToStart);
            List<Instance> instancesStarted = ec2Service.startInstances(numInstancesToStart);
            setInstanceNames(instancesStarted, existingInstances);
        }
    }

}

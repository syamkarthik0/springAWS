// The EC2Service class is responsible for managing Amazon EC2 instances by interacting with the AWS SDK in a Spring Boot application (creating, Activating, deleting VM's)

package com.example.awsec2api.service;

import com.example.awsec2api.dto.VMInfo;
import com.example.awsec2api.dto.VMListResponse;
import com.example.awsec2api.exception.EC2Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EC2Service {

    private static final Logger logger = LoggerFactory.getLogger(EC2Service.class);

    private final Ec2Client ec2Client;

    @Autowired
    public EC2Service(Ec2Client ec2Client) {
        this.ec2Client = ec2Client;
    }

    public String createVM() {
        try {
            RunInstancesRequest runRequest = RunInstancesRequest.builder()
                    .imageId("ami-0ebfd941bbafe70c6")
                    .instanceType(InstanceType.T2_MICRO)
                    .maxCount(1)
                    .minCount(1)
                    .build();

            RunInstancesResponse response = ec2Client.runInstances(runRequest);
            String instanceId = response.instances().get(0).instanceId();
            logger.info("Created new EC2 instance with ID: {}", instanceId);
            return instanceId;
        } catch (Ec2Exception e) {
            logger.error("Error creating EC2 instance: {}", e.getMessage());
            throw new EC2Exception("Failed to create EC2 instance", e);
        }
    }

    public VMListResponse getActiveVMs() {
        try {
            DescribeInstancesRequest request = DescribeInstancesRequest.builder().build();
            DescribeInstancesResponse response = ec2Client.describeInstances(request);

            List<VMInfo> activeInstances = response.reservations().stream()
                    .flatMap(reservation -> reservation.instances().stream())
                    .filter(instance -> instance.state().name() == InstanceStateName.RUNNING)
                    .map(instance -> new VMInfo(instance.instanceId(), instance.state().name().toString()))
                    .collect(Collectors.toList());

            logger.info("Retrieved {} active EC2 instances", activeInstances.size());
            return new VMListResponse(activeInstances);
        } catch (Ec2Exception e) {
            logger.error("Error retrieving active EC2 instances: {}", e.getMessage());
            throw new EC2Exception("Failed to retrieve active EC2 instances", e);
        }
    }

    
    public String deleteVM(String instanceId) {
        try {
            TerminateInstancesRequest terminateRequest = TerminateInstancesRequest.builder()
                    .instanceIds(instanceId)
                    .build();

            TerminateInstancesResponse response = ec2Client.terminateInstances(terminateRequest);
            logger.info("Terminated EC2 instance with ID: {}", instanceId);
            return "Successfully terminated instance with ID: " + instanceId;
        } catch (Ec2Exception e) {
            logger.error("Error terminating EC2 instance: {}", e.getMessage());
            throw new EC2Exception("Failed to terminate EC2 instance with ID: " + instanceId, e);
        }
    }
}
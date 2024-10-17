// This DTO is used to return a list of VMs to the client.

package com.example.awsec2api.dto;

import java.util.List;

public class VMListResponse {
    private List<VMInfo> instances;

    public VMListResponse(List<VMInfo> instances) {
        this.instances = instances;
    }

    public List<VMInfo> getInstances() {
        return instances;
    }

    public void setInstances(List<VMInfo> instances) {
        this.instances = instances;
    }
}
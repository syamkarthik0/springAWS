package com.example.awsec2api.controller;

import com.example.awsec2api.dto.VMCreationResponse;
import com.example.awsec2api.dto.VMListResponse;
import com.example.awsec2api.exception.EC2Exception;
import com.example.awsec2api.service.EC2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ec2")
@Tag(name = "EC2 Controller", description = "Endpoints for managing EC2 instances")
public class EC2Controller {

    private final EC2Service ec2Service;

    @Autowired
    public EC2Controller(EC2Service ec2Service) {
        this.ec2Service = ec2Service;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new EC2 instance", description = "Creates a new EC2 instance and returns its ID")
    public ResponseEntity<VMCreationResponse> createVM() {
        String instanceId = ec2Service.createVM();
        return ResponseEntity.ok(new VMCreationResponse(instanceId));
    }

    @GetMapping("/active")
    @Operation(summary = "List active EC2 instances", description = "Returns a list of all active EC2 instances")
    public ResponseEntity<VMListResponse> getActiveVMs() {
        VMListResponse activeVMs = ec2Service.getActiveVMs();
        return ResponseEntity.ok(activeVMs);
    }

    @DeleteMapping("/delete/{instanceId}")
    @Operation(summary = "Delete an EC2 instance", description = "Deletes an EC2 instance given its ID")
    public ResponseEntity<String> deleteVM(@PathVariable String instanceId) {
        try {
            String result = ec2Service.deleteVM(instanceId);
            return ResponseEntity.ok(result);
        } catch (EC2Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
    
}
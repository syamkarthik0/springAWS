// This is a custom exception class that is thrown when there is an error related to EC2(such as starting, stopping, or managing EC2 instances).

package com.example.awsec2api.exception;

public class EC2Exception extends RuntimeException {
    public EC2Exception(String message) {
        super(message);
    }

    public EC2Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
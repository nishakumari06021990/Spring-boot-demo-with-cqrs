package com.example.crud.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmployeeConsumer {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeConsumer.class);

    @KafkaListener(topics = "employee-create", groupId = "employee-group")
    public void consumeCreateMessage(String message) {
        // Process create operation (e.g. create a new Employee record in the DB)
        logger.info("Kafka Employee created: {} ", message);
    }

    @KafkaListener(topics = "employee-update", groupId = "employee-group")
    public void consumeUpdateMessage(String message) {
        // Process update operation (e.g. update existing Employee record in DB)
        logger.info("Kafka Employee Updated:  {}" , message);
    }

    @KafkaListener(topics = "employee-delete", groupId = "employee-group")
    public void consumeDeleteMessage(String message) {
        // Process delete operation (e.g. delete Employee record from DB)
        logger.info("Kafka Employee Deleted:  {}" , message);
    }
}

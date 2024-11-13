package com.example.crud.service;

import com.example.crud.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmployeeProducer {

    private static final String CREATE_TOPIC = "employee-create";
    private static final String UPDATE_TOPIC = "employee-update";
    private static final String DELETE_TOPIC = "employee-delete";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendCreateMessage(Employee employee) {
        System.out.println("Inside Producer");
        kafkaTemplate.send(CREATE_TOPIC, String.valueOf(employee.getEmployeeId()), employee.toString());
    }

    public void sendUpdateMessage(Employee employee) {
        kafkaTemplate.send(UPDATE_TOPIC, String.valueOf(employee.getEmployeeId()), employee.toString());
    }

    public void sendDeleteMessage(long employeeId) {
        kafkaTemplate.send(DELETE_TOPIC, String.valueOf(employeeId), String.valueOf(employeeId));
    }
}

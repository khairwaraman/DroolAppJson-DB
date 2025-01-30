package com.drool.model;

import lombok.Data;

@Data
public class Rule {
    private String name;
    private String condition;
    private String action;
}
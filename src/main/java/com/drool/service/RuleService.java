package com.drool.service;


// Change this import

import com.drool.model.Rule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleService {

    private KieContainer kieContainer;
    private final ObjectMapper objectMapper;

    public RuleService() {
        this.objectMapper = new ObjectMapper();
    }

    @PostConstruct
    public void init() throws Exception {
        // Read rules from JSON file
        ClassPathResource resource = new ClassPathResource("rules/discount-rules.json");
        List<Rule> rules = objectMapper.readValue(resource.getInputStream(),
                new TypeReference<List<Rule>>() {});

        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        // Rest of the code remains the same
        String drlContent = convertToDRL(rules);
        kieFileSystem.write("src/main/resources/rules/generated-rules.drl", drlContent);

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();

        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Error building rules: " + kieBuilder.getResults());
        }

        KieModule kieModule = kieBuilder.getKieModule();
        kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
    }

    // Rest of the methods remain the same
    // ...
    // Method to convert JSON rules to DRL format
    private String convertToDRL(List<Rule> rules) {
        StringBuilder drlBuilder = new StringBuilder();

        // Add package and imports
        drlBuilder.append("package rules;\n");
        drlBuilder.append("import com.drool.entities.Order;\n\n");

        // Convert each rule to DRL format
        for (Rule rule : rules) {
            drlBuilder.append(String.format(
                    "rule \"%s\"\n" +
                            "    when\n" +
                            "        %s\n" +
                            "    then\n" +
                            "        %s\n" +
                            "end\n\n",
                    rule.getName(),
                    rule.getCondition(),
                    rule.getAction()
            ));
        }

        return drlBuilder.toString();
    }

    public KieSession createKieSession() {
        return kieContainer.newKieSession();
    }
}
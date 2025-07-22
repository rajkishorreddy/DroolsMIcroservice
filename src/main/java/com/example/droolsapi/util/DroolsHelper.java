package com.example.droolsapi.util;

import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class DroolsHelper {

    private static final String RULES_DIR = "src/main/resources/rules/";

    public static KieSession buildKieSession(List<String> ruleNames) throws Exception {
        KieServices kieServices = KieServices.Factory.get();

        KieFileSystem kfs = kieServices.newKieFileSystem();

        for (String rule : ruleNames) {
            File drlFile = new File(RULES_DIR + rule + ".drl");
            if (!drlFile.exists()) {
                throw new RuntimeException("Rule file not found: " + rule);
            }
            String content = Files.readString(drlFile.toPath());
            kfs.write("src/main/resources/" + rule + ".drl", content);
        }

        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Rule build errors: " + results.getMessages());
        }

        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        return kieContainer.newKieSession();
    }
}

package com.ufrpe.vmapi.utils;

import com.ufrpe.vmapi.models.VmConfig;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Component
public class VmService {
    private String currDirectory;
    private String resourcesPath;
    private String vmsPath;

    public VmService() {
        this.initConfig();

    }

    private void initConfig() {
        currDirectory = System.getProperty("user.dir");
        resourcesPath = currDirectory + "/src/main/resources";
        vmsPath = resourcesPath + "/vms";
    }

    public String startVm(VmConfig vmConfig) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();

        String scriptPath = resourcesPath + "/scripts/start-vm.sh";

        processBuilder.command(
                scriptPath,
                vmConfig.getName()
        );
        System.out.println(processBuilder.command());


        // Execute cmd
        Process process = processBuilder.start();
        StringBuilder output = new StringBuilder();
        BufferedReader reader = getReader(process.getInputStream());

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }

        int exitVal = process.waitFor();
        if (exitVal != 0) {
            System.out.println("Failed!");
            System.out.println(exitVal);
            throw new Error(String.format("Failed with exit code: %d. Output: %s", exitVal, output.toString()));
        }

        return output.toString();
    }


    public String executeCreate(VmConfig vmConfig) throws IOException, InterruptedException {
        
        ProcessBuilder processBuilder = new ProcessBuilder();

        String scriptPath = resourcesPath + "/scripts/create-vm.sh";

        processBuilder.command(
                scriptPath,
                vmConfig.getName(),
                String.valueOf(vmConfig.getRam()),
                String.valueOf(vmConfig.getDisk()),
                vmConfig.getOsType(),
                vmsPath
        );
        System.out.println(processBuilder.command());


        // Execute cmd
        Process process = processBuilder.start();
        StringBuilder output = new StringBuilder();
        BufferedReader reader = getReader(process.getInputStream());

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }

        int exitVal = process.waitFor();
        if (exitVal != 0) {
            System.out.println("Failed!");
            System.out.println(exitVal);
            throw new Error(String.format("Failed with exit code: %d. Output: %s", exitVal, output.toString()));
        }

        return output.toString();
    }
    public String executeClone(VmConfig vmConfig) throws InterruptedException, IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();

        String scriptPath = resourcesPath + "/scripts/clone-vm.sh";
        String newName = vmConfig.getName() + "-clone_" + new Timestamp(System.currentTimeMillis()).getNanos();
        processBuilder.command(
                scriptPath,
                vmsPath,
                vmConfig.getName(),
                newName
                );
        System.out.println(processBuilder.command());


        // Execute cmd
        Process process = processBuilder.start();
        StringBuilder output = new StringBuilder();
        BufferedReader reader = getReader(process.getInputStream());

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("output: " + line);
            output.append(line + "\n");
        }

        return output.toString();
    }

    public List<String> listVms() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();

        String scriptPath = resourcesPath + "/scripts/list-vms.sh";

        processBuilder.command(
                scriptPath
        );
        System.out.println(processBuilder.command());


        // Execute cmd
        Process process = processBuilder.start();
        List<String> vms = new ArrayList<>();
        BufferedReader reader = getReader(process.getInputStream());

        String line;
        while ((line = reader.readLine()) != null) {
            String vmName = Arrays.asList(line
                    .replace("\"", "")
                    .split(" ")).get(0);
            vms.add(vmName);
        }

        int exitVal = process.waitFor();
        if (exitVal != 0) {
            System.out.println("Failed!");
            System.out.println(exitVal);
            throw new Error(String.format("Failed with exit code: %d", exitVal));
        }

        return vms;
    }

    public BufferedReader getReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

}

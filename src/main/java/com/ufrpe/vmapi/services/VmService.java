package com.ufrpe.vmapi.services;

import com.ufrpe.vmapi.models.VmConfig;
import com.ufrpe.vmapi.utils.CmdUtil;
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
    private String scriptsPath;

    public VmService() {
        this.initConfig();

    }

    private void initConfig() {
        currDirectory = System.getProperty("user.dir");
        resourcesPath = currDirectory + "/src/main/resources";
        vmsPath = resourcesPath + "/vms";
        scriptsPath = resourcesPath + "/scripts";
    }

    public String startVm(VmConfig vmConfig) throws IOException, InterruptedException {
        String startPath = scriptsPath + "/start-vm.sh";

        return CmdUtil.executeCmd(
                startPath,
                vmConfig.getName(),
                scriptsPath
        );
    }


    public String executeCreate(VmConfig vmConfig) throws IOException, InterruptedException {
        String createPath = scriptsPath + "/create-vm.sh";

        return CmdUtil.executeCmd(
                createPath,
                vmConfig.getName(),
                String.valueOf(vmConfig.getRam()),
                String.valueOf(vmConfig.getDisk()),
                vmConfig.getOsType(),
                vmsPath
        );
    }
    public String executeClone(VmConfig vmConfig) throws InterruptedException, IOException {
        String clonePath = scriptsPath + "/clone-vm.sh";
        String newName = vmConfig.getName() + "-clone_" + new Timestamp(System.currentTimeMillis()).getNanos();

        return CmdUtil.executeCmd(
                clonePath,
                vmsPath,
                vmConfig.getName(),
                newName
        );
    }

    public List<String> listVms() throws IOException, InterruptedException {
        String listPath = scriptsPath + "/list-vms.sh";
        String response = CmdUtil.executeCmd(
                listPath
        );

        List<String> vmsWithUUID = Arrays.asList(response.split("\n"));
        List<String> finalVms = new ArrayList<String>();

        for (String line : vmsWithUUID) {
            String vmName = Arrays.asList(line
                    .replace("\"", "")
                    .split(" ")).get(0);
            finalVms.add(vmName);
        }


        return finalVms;
    }
}

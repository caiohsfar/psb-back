package com.ufrpe.vmapi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CmdUtil {
    public static String executeCmd(String... args) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command(args);
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

    public static BufferedReader getReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}

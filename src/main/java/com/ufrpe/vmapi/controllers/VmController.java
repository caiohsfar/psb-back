package com.ufrpe.vmapi.controllers;

import com.ufrpe.vmapi.models.VmConfig;
import com.ufrpe.vmapi.utils.VmService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Timed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class VmController {
    VmService vmService;

    public VmController(VmService vmService) {
        this.vmService = vmService;
    }

    @PostMapping(path="/create", produces=MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> create(@RequestBody VmConfig newVM){
        Map<String, String> response = new HashMap<String, String>();
        try {
            String message = vmService.executeCreate(newVM);
            response.put("message", message);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/clone")
    ResponseEntity<Object> clone(@RequestBody VmConfig vmConfig) {
        Map<String, String> response = new HashMap<String, String>();
        try {
            String message = vmService.executeClone(vmConfig);
            response.put("message", message);
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);

        }
    }
    @PostMapping("/start")
    ResponseEntity<Object> starat(@RequestBody VmConfig vmConfig) {
        Map<String, String> response = new HashMap<String, String>();
        try {
            String message = vmService.startVm(vmConfig);
            response.put("message", message);
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);

        }
    }

    @GetMapping("/list")
    ResponseEntity<Object> listVms() {
        try {
            List<String> vms = vmService.listVms();
            return ResponseEntity.ok().body(vms);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
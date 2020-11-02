package com.ufrpe.vmapi.controllers;

import com.ufrpe.vmapi.models.VmConfig;
import com.ufrpe.vmapi.services.VmService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vms")
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
    ResponseEntity<Object> start(@RequestBody VmConfig vmConfig) {
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
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            List<String> vms = vmService.listVms();
            return ResponseEntity.ok().body(vms);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(response);
        }
    }
}
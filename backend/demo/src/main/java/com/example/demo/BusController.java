package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BusController {

    @GetMapping
    @RequestMapping(value = "/api/get/nearestStops")
    public void getNearestStops() {
        
    }

    @GetMapping
    @RequestMapping(value = "/api/get/stop")
    public void getStop() {

    }

    @GetMapping
    @RequestMapping(value = "/api/get/route")
    public void getRoute() {

    }

    @GetMapping
    @RequestMapping(value = "/api/get/snapshot")
    public void getSnapshot() {

    }


}
//accept user commands and return JSON

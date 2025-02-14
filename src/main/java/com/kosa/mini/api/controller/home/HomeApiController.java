package com.kosa.mini.api.controller.home;

import com.kosa.mini.api.dto.home.HomeStoreDTO;
import com.kosa.mini.api.service.home.HomeApiApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HomeApiController {
    @Autowired
    private HomeApiApiServiceImpl service;

    @GetMapping({"/", "/home"})
    public List<HomeStoreDTO> goHome(){
        return service.home();
    }
}

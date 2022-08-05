 
package com.example.demo.controller;

import com.example.demo.entity.JwtRequest;
import com.example.demo.entity.JwtResponse;
import com.example.demo.service.JwtAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@CrossOrigin
public class JwtController
{

    @Autowired
    private JwtAuthenticationService jwtService;

    @PostMapping({ "/authenticate" })
    public JwtResponse createJwtToken (
        @RequestBody
        JwtRequest jwtRequest) throws Exception
    {
        Logger.getLogger(JwtController.class.getName())
              .info("Authenticating user: " + jwtRequest.getUserName());
        return jwtService.createJwtToken(jwtRequest);
    }
}

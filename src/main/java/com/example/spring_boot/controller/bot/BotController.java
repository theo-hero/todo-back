package com.example.spring_boot.controller.bot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_boot.dto.LinkTelegramRequest;
import com.example.spring_boot.model.AppUser;
import com.example.spring_boot.model.BotCode;
import com.example.spring_boot.model.TelegramLink;
import com.example.spring_boot.repository.BotCodeRepository;
import com.example.spring_boot.repository.TaskRepository;
import com.example.spring_boot.repository.TelegramLinkRepository;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/bot")
public class BotController {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    BotCodeRepository codeRepository;

    @Autowired
    TelegramLinkRepository linkRepository;

    @GetMapping("/test")
    public List<String> testToken() {
        return taskRepository.findDescriptionsByUserId(2L);
    }

    @PostMapping("/link")
    public ResponseEntity<?> linkTelegramProfile(@RequestBody LinkTelegramRequest req) throws Exception {
        System.out.println(req.getCode() + " " + req.getTelegramId());
        BotCode codeObj = codeRepository.findByCodeAndUsedFalse(req.getCode())
                .orElseThrow(() -> new Exception("Code not found"));
        
        if (codeObj.getExpiresAt().isBefore(Instant.now())) return ResponseEntity.badRequest().body("Code expired");

        AppUser user = codeObj.getUser();
        TelegramLink link = TelegramLink.builder()
                .telegramId(req.getTelegramId())
                .linkedAt(Instant.now())
                .user(user)
                .build();

        System.out.println(link.getTelegramId());

        linkRepository.save(link);

        codeObj.setUsed(true);
        codeRepository.save(codeObj);
        
        return ResponseEntity.ok().build();
    }
}

package com.example.players.controller;

import com.example.players.dto.PlayerDTO;
import com.example.players.service.PlayerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api")
@RestController
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("players")
    public Page<PlayerDTO> players(@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
        return playerService.getPlayers(pageable);
    }

    @GetMapping("players/{playerID}")
    public ResponseEntity<PlayerDTO> playerById(@PathVariable Long playerID) {
        return playerService.getPlayer(playerID)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

package com.example.players.service;

import com.example.players.dto.PlayerDTO;

import java.util.Optional;

public interface PlayerService {
    Iterable<PlayerDTO> getPlayers();
    Optional<PlayerDTO> getPlayer(Long id);
}

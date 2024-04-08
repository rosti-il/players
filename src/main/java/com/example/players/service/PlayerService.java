package com.example.players.service;

import com.example.players.dto.PlayerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PlayerService {
    Page<PlayerDTO> getPlayers(Pageable pageable);
    Optional<PlayerDTO> getPlayer(Long id);
}

package com.example.players.service;

import com.example.players.dto.PlayerDTO;
import com.example.players.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Iterable<PlayerDTO> getPlayers() {
        return () -> StreamSupport.stream(playerRepository.findAll().spliterator(), false)
                .map(PlayerDTO::new)
                .iterator();
    }

    @Override
    public Optional<PlayerDTO> getPlayer(Long id) {
        return playerRepository.findById(id)
                .map(PlayerDTO::new);
    }
}

package com.example.players.service;

import com.example.players.dto.PlayerDTO;
import com.example.players.repository.PlayerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Page<PlayerDTO> getPlayers(Pageable pageable) {
        var playersPage = playerRepository.findAll(pageable);
        var playersList = playersPage
                .stream()
                .map(PlayerDTO::new)
                .toList();
        return new PageImpl<>(playersList, pageable, playersPage.getTotalElements());
    }

    @Override
    public Optional<PlayerDTO> getPlayer(Long id) {
        return playerRepository.findById(id)
                .map(PlayerDTO::new);
    }
}

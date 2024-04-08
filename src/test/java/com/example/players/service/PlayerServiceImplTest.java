package com.example.players.service;

import com.example.players.dto.PlayerDTO;
import com.example.players.model.Player;
import com.example.players.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.example.players.Helper.EASY_RANDOM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private final Player playerOne = EASY_RANDOM.nextObject(Player.class);
    private final Player playerTwo = EASY_RANDOM.nextObject(Player.class);

    @BeforeEach
    void beforeEach() {
        when(playerRepository.findAll(any())).thenReturn(new PageImpl<>(List.of(playerOne, playerTwo)));
        when(playerRepository.findById(playerOne.getId())).thenReturn(Optional.of(playerOne));
        when(playerRepository.findById(playerTwo.getId())).thenReturn(Optional.of(playerTwo));
    }

    @Test
    void getPlayersShouldReturnAllPlayers() {
        Iterable<PlayerDTO> players = playerService.getPlayers(PageRequest.ofSize(Integer.MAX_VALUE));
        assertEquals(Stream.of(playerOne, playerTwo).map(PlayerDTO::new).toList(), StreamSupport.stream(players.spliterator(), false).toList());
    }

    @Test
    void getPlayerShouldReturnPlayerForGivenId() {
        Optional<PlayerDTO> playerDTO = playerService.getPlayer(playerOne.getId());
        assertTrue(playerDTO.isPresent());
        assertEquals(new PlayerDTO(playerOne), playerDTO.get());

        playerDTO = playerService.getPlayer(playerTwo.getId());
        assertTrue(playerDTO.isPresent());
        assertEquals(new PlayerDTO(playerTwo), playerDTO.get());
    }
}

package com.example.players.repository;

import com.example.players.model.Player;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.BeanInitializationException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    PlayerRepository playerRepository;

    @Captor
    ArgumentCaptor<Player> playerCaptor;

    private final Set<Long> playerSet = new HashSet<>();

    @BeforeEach
    void beforeEach() {
        when(playerRepository.save(any()))
                .then(call -> {
                    playerSet.add(((Player) call.getArgument(0)).getId());
                    return call.getArgument(0);
                });
        when(playerRepository.existsById(any()))
                .then(call -> playerSet.contains(call.<Long>getArgument(0)));
    }

    @Test
    void shouldInitializePlayerData() {
        var dataInitializer = new DataInitializer("src/test/resources/players.csv");

        dataInitializer.postProcessAfterInitialization(playerRepository, "playerRepository");
        verify(playerRepository, times(2)).save(playerCaptor.capture());

        var capturedPlayers = playerCaptor.getAllValues();
        capturedPlayers.forEach(player -> Assertions.assertThat(player).hasNoNullFieldsOrProperties());

        // sanity of the test input data, not a real test
        Assertions.assertThat(capturedPlayers).doesNotHaveDuplicates();
    }

    @Test
    void shouldThrowBecauseOfDuplication() {
        var dataInitializer = new DataInitializer("src/test/resources/players-duplicates.csv");
        var exception = assertThrowsExactly(BeanInitializationException.class,
                () -> dataInitializer.postProcessAfterInitialization(playerRepository, "playerRepository"));
        assertEquals("Invalid initial data CSV file. Duplication(s) of player IDs found: [1, 2]", exception.getMessage());
    }
}

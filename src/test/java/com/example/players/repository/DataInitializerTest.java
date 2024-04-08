package com.example.players.repository;

import com.example.players.annotation.SpringBootTestWithTestProfile;
import com.example.players.model.Player;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTestWithTestProfile
class DataInitializerTest {

    @Autowired
    DataInitializer dataInitializer;

    @MockBean
    PlayerRepository playerRepository;

    @Captor
    ArgumentCaptor<Player> playerCaptor;

    @Test
    void shouldInitializePlayerData() {
        dataInitializer.postProcessAfterInitialization(playerRepository, "playerRepository");
        verify(playerRepository, times(2)).save(playerCaptor.capture());

        var capturedPlayers = playerCaptor.getAllValues();
        capturedPlayers.forEach(player -> Assertions.assertThat(player).hasNoNullFieldsOrProperties());

        Assertions.assertThat(capturedPlayers).doesNotHaveDuplicates();
    }
}

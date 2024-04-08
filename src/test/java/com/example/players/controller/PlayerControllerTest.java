package com.example.players.controller;

import com.example.players.annotation.WebMvcTestWithTestProfile;
import com.example.players.dto.PlayerDTO;
import com.example.players.model.Player;
import com.example.players.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static com.example.players.Helper.EASY_RANDOM;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTestWithTestProfile(PlayerController.class)
class PlayerControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    private final PlayerDTO playerOne = new PlayerDTO(EASY_RANDOM.nextObject(Player.class));
    private final PlayerDTO playerTwo = new PlayerDTO(EASY_RANDOM.nextObject(Player.class));

    @BeforeEach
    void beforeEach() {
        when(playerService.getPlayers()).thenReturn(Arrays.asList(playerOne, playerTwo));
        when(playerService.getPlayer(playerOne.getId())).thenReturn(Optional.of(playerOne));
        when(playerService.getPlayer(playerTwo.getId())).thenReturn(Optional.of(playerTwo));
    }

    @Test
    void testPlayers() throws Exception {
        PlayerDTO playerOne = new PlayerDTO(EASY_RANDOM.nextObject(Player.class));
        PlayerDTO playerTwo = new PlayerDTO(EASY_RANDOM.nextObject(Player.class));
        when(playerService.getPlayers()).thenReturn(Arrays.asList(playerOne, playerTwo));

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is(objectMapper.convertValue(playerOne, Map.class))))
                .andExpect(jsonPath("$[1]", is(objectMapper.convertValue(playerTwo, Map.class))));

        verify(playerService, times(1)).getPlayers();
    }

    @Test
    void testPlayerOneById() throws Exception {
        mockMvc.perform(get("/api/players/%s".formatted(playerOne.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(objectMapper.convertValue(playerOne, Map.class))));

        verify(playerService, times(1)).getPlayer(any());
    }

    @Test
    void testPlayerTwoById() throws Exception {
        mockMvc.perform(get("/api/players/%s".formatted(playerTwo.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(objectMapper.convertValue(playerTwo, Map.class))));

        verify(playerService, times(1)).getPlayer(any());
    }

    @Test
    void testPlayerByNotExistingId() throws Exception {
        mockMvc.perform(get("/api/players/%s".formatted(playerOne.getId() ^ playerTwo.getId())))
                .andExpect(status().isNotFound());

        verify(playerService, times(1)).getPlayer(any());
    }
}

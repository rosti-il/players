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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
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
        when(playerService.getPlayers(PageRequest.of(0, 1))).thenReturn(new PageImpl<>(List.of(playerOne)));
        when(playerService.getPlayers(PageRequest.of(1, 1))).thenReturn(new PageImpl<>(List.of(playerTwo)));
        // TODO find the way to change this default magic number 2000 to Integer.MAX_VALUE for consistency
        when(playerService.getPlayers(PageRequest.ofSize(2000))).thenReturn(new PageImpl<>(List.of(playerOne, playerTwo)));
        when(playerService.getPlayer(playerOne.getId())).thenReturn(Optional.of(playerOne));
        when(playerService.getPlayer(playerTwo.getId())).thenReturn(Optional.of(playerTwo));
    }

    @Test
    void testPlayers() throws Exception {
        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0]", is(objectMapper.convertValue(playerOne, Map.class))))
                .andExpect(jsonPath("$.content[1]", is(objectMapper.convertValue(playerTwo, Map.class))));

        verify(playerService, times(1)).getPlayers(any());
    }

    @Test
    void testPlayersPagination() throws Exception {
        mockMvc.perform(get("/api/players?page=1&size=1"))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0]", is(objectMapper.convertValue(playerTwo, Map.class))));

        verify(playerService, times(1)).getPlayers(any());
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

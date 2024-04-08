package com.example.players.dto;

import com.example.players.model.Player;

public class PlayerDTO {
    private final Long id;
    private final String name;
    private final Integer age;
    private final String gender;
    private final String country;

    public PlayerDTO(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.age = player.getAge();
        this.gender = player.getGender();
        this.country = player.getCountry();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }
}

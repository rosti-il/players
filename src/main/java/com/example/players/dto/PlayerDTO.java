package com.example.players.dto;

import com.example.players.model.Player;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDTO playerDTO = (PlayerDTO) o;
        return Objects.equals(id, playerDTO.id) && Objects.equals(name, playerDTO.name) && Objects.equals(age, playerDTO.age) && Objects.equals(gender, playerDTO.gender) && Objects.equals(country, playerDTO.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, gender, country);
    }

    @Override
    public String toString() {
        return "PlayerDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}

package com.example.players.repository;

import com.example.players.model.Player;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Component
public class DataInitializer implements BeanPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final String csvPlayersFile;

    public DataInitializer(@Value("${players.csv-file}") String csvPlayersFile) {
        this.csvPlayersFile = csvPlayersFile;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof PlayerRepository playerRepository) {
            log.info("Initializing data from {} file.", new File(csvPlayersFile).getAbsoluteFile());

            var mapper = new CsvMapper();
            var schema = CsvSchema.emptySchema().withHeader();

            // LinkedList adds faster but when failing we're in no hurry and memory is more important
            var duplications = new ArrayList<Long>();

            try (InputStream inputStream = new FileInputStream(csvPlayersFile)) {
                MappingIterator<Player> playerMappingIterator = mapper.readerFor(Player.class)
                        .with(schema)
                        .readValues(inputStream);

                for (var player : (Iterable<Player>) () -> playerMappingIterator) {
                    if (playerRepository.existsById(player.getId())) {
                        duplications.add(player.getId());
                    } else {
                        playerRepository.save(player);
                    }
                }
            } catch (IOException e) {
                throw new BeanInitializationException(e.getMessage(), e);
            }

            if (!duplications.isEmpty()) {
                throw new BeanInitializationException("Invalid initial data CSV file. Duplication(s) of player IDs found: " + duplications);
            }
        }
        return bean;
    }
}

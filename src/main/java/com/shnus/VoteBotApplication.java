package com.shnus;

import com.shnus.VoteBot.VotePollingBot;
import com.shnus.VoteBot.VotePollingBotRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@SpringBootApplication
public class VoteBotApplication implements ApplicationRunner {

    @Autowired
    private VotePollingBotRegister register;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(VoteBotApplication.class);
        app.setWebEnvironment(false);
        app.run();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        register.run();
    }
}

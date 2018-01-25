package com.shnus;

import com.shnus.MyBots.VoteBot.VotePollingBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BotApplication implements ApplicationRunner {

    @Autowired
    private LongPollingBotRegister register;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BotApplication.class);
        app.setWebEnvironment(false);
        app.run();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        register.run(VotePollingBot.class);
    }
}

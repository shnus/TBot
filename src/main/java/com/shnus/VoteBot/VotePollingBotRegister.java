package com.shnus.VoteBot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.util.ResourceBundle;


@Component
public class VotePollingBotRegister {

    private String BOT_TOKEN;


    public void run() {
        BOT_TOKEN = ResourceBundle.getBundle("voteBot").getString("token");
        try {
            ApiContextInitializer.init();

            TelegramBotsApi botsApi = new TelegramBotsApi();

            botsApi.registerBot(new VotePollingBot(BOT_TOKEN));
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}

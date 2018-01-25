package com.shnus;

import com.shnus.MyBots.VoteBot.VotePollingBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;


@Component
public class PollingBotRegister {

    private String BOT_TOKEN;

    public void run(Class<? extends TelegramLongPollingBot> pollingBot) {
        BOT_TOKEN = ResourceBundle.getBundle(pollingBot.getSimpleName()).getString("token");
        try {
            ApiContextInitializer.init();

            TelegramBotsApi botsApi = new TelegramBotsApi();

            botsApi.registerBot(pollingBot.getConstructor(String.class).newInstance(BOT_TOKEN));
        } catch (TelegramApiRequestException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

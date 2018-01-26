package com.shnus.MyBots.VoteBot.utils.tokenUtils;

import com.shnus.MyBots.VoteBot.model.Voting;

import java.security.SecureRandom;
import java.util.Map;

public class TokenService {

    private static String createToken() {
        SecureRandom random = new SecureRandom();
        long longToken = Math.abs(random.nextLong());
        String stringToken = Long.toString(longToken, 16);
        while (stringToken.length() < 16) {
            stringToken.concat("a");
        }
        return stringToken;
    }

    public static String createUniqueToken(Map<String, Voting> votes) {
        String token = createToken();
        if (votes.get(token) == null) {
            return token;
        } else {
            return createUniqueToken(votes);
        }
    }


}

package com.shnus.MyBots.VoteBot.utils.tokenUtils;

public enum TokenVerificationsStatus {

    //The format of token is wrong. For example, length.
    TOKEN_INCORRECT_FORMAT,

    //Token format is right, but there is no corresponding vote.
    TOKEN_NON_EXIST,

    //Token is correspond to existing voting.
    TOKEN_EXIST

}

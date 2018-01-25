package com.shnus.MyBots.VoteBot.utils.tokenServices;

public enum  TokenVerificationsStatus {
    
    //The format of token is wrong. For example, length.
    INCORRECT_FORMAT,


    //Token format is right, but there is no corresponding vote.
    NON_EXIST,

    //Token is correspond to existing voting.
    EXIST

}

package com.shnus.MyBots.VoteBot.utils.voteUtils;

public enum VoteStatus {

    //Vote is in creation stage. Waiting for
    VOTE_WAIT_FOR_QUESTION,

    //Vote is in creation stage. Waiting for first option.
    VOTE_NO_ANY_OPTIONS,

    //Vote is in creation stage. Waiting for next option or finishing.
    VOTE_WAIT_FOR_NEXT_OPTION,

    ACTIVE_VOTE

}

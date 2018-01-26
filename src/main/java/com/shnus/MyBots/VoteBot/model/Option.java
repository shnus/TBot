package com.shnus.MyBots.VoteBot.model;

import java.io.Serializable;

public class Option implements Serializable {

    private String option;
    private int votes;
    private int order;

    public Option(String option, int order) {
        this.option = option;
        this.order = order;
        votes = 0;
    }

    public String getOption() {
        return option;
    }

    public int getOrder() {
        return order;
    }

    public int getVotes() {
        return votes;
    }

    public void addVote() {
        votes++;
    }
}

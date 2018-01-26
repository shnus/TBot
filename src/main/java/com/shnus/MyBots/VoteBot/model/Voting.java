package com.shnus.MyBots.VoteBot.model;

import com.shnus.MyBots.VoteBot.utils.voteUtils.VoteStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Voting implements Serializable {

    private String question;

    private List<Option> options;

    private VoteStatus status;

    public Voting() {
        status = VoteStatus.VOTE_WAIT_FOR_QUESTION;
        options = new ArrayList<>();
    }

    public void setOption(String option) {
        if (options.isEmpty()) {
            status = VoteStatus.VOTE_WAIT_FOR_NEXT_OPTION;
        }
        options.add(new Option(option, options.size() + 1));
    }

    public void done() {
        status = VoteStatus.ACTIVE_VOTE;
    }

    public boolean vote(int i) {
        if (i <= options.size() && i > 0) {
            options.get(i - 1).addVote();
            return true;
        } else {
            return false;
        }
    }

    public VoteStatus getStatus() {
        return status;
    }

    public Iterator<Option> getIterator() {
        return options.iterator();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
        status = VoteStatus.VOTE_NO_ANY_OPTIONS;
    }

    public int getOptionsCount() {
        return options.size();
    }


    public long getOptionsVotesSum() {
        return options.stream().mapToLong(Option::getVotes).sum();
    }
}

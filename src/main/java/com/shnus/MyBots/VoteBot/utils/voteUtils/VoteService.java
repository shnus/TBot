package com.shnus.MyBots.VoteBot.utils.voteUtils;

import com.shnus.MyBots.VoteBot.model.Option;
import com.shnus.MyBots.VoteBot.model.Voting;
import com.shnus.MyBots.VoteBot.utils.tokenUtils.TokenVerificationsStatus;

import java.util.Iterator;
import java.util.Map;

public class VoteService {

    public static VoteStatus getStatus(
            Map<String, Voting> votes,
            Map<String, String> chatToken,
            String curChatId
    ) {
        String token = chatToken.get(curChatId);
        if (token == null) return null;
        Voting voting = votes.get(token);
        if (voting == null) return null;
        return voting.getStatus();
    }

    public static boolean isChatOwner(
            Map<String, String> chatUser,
            String curChatId, String curUserId
    ) {
        String userId = chatUser.get(curChatId);
        return curUserId.compareTo(userId) == 0;
    }

    public static TokenVerificationsStatus isTokenExist(
            Map<String, Voting> votes,
            String token
    ) {
        if (token.length() != 16) {
            return TokenVerificationsStatus.TOKEN_INCORRECT_FORMAT;
        }

        if (votes.get(token) == null) {
            return TokenVerificationsStatus.TOKEN_NON_EXIST;
        } else {
            return TokenVerificationsStatus.TOKEN_EXIST;
        }
    }

    public static String verifyArgs(String[] arguments) {
        if (arguments.length == 0) {
            return null;
        } else {
            return String.join(" ", arguments);
        }
    }

    public static String addVoteToDb(
            Map<String, Voting> votes, Map<String, String> chatToken,
            Map<String, String> chatUser, String token, String userName,
            String curChatId, String curUserId
    ) {
        votes.put(token, new Voting());
        chatToken.put(curChatId, token);
        chatUser.put(curChatId, curUserId);
        StringBuilder message = new StringBuilder()
                .append(userName)
                .append(", your vote create command is approved. ")
                .append("Your vote token is " + token)
                .append(". Use it to share this voting. ")
                .append("Please, send the question for this vote with /vote command.");
        return message.toString();
    }

    public static String addQuestion(
            Map<String, Voting> votes, Map<String, String> chatToken,
            String curChatId, String question, String userName
    ) {
        String token = chatToken.get(curChatId);
        Voting voting = votes.get(token);
        System.out.println(voting.getStatus());
        voting.setQuestion(question);
        votes.put(token, voting);
        StringBuilder message = new StringBuilder()
                .append(userName)
                .append(", question is approved. ")
                .append("Please, send the first option for this vote with /vote command.");
        return message.toString();
    }

    public static String addOption(
            Map<String, Voting> votes, Map<String, String> chatToken,
            String curChatId, String option, String userName
    ) {
        String token = chatToken.get(curChatId);
        Voting voting = votes.get(token);
        if (option.toLowerCase().compareTo("done") == 0) {
            voting.done();
            votes.put(token, voting);
            StringBuilder message = new StringBuilder()
                    .append("Voting is successfully created. Use /vote command with numerical argument to vote.")
                    .append(" Number should correspond to option order.");
            return message.toString();
        } else {
            voting.setOption(option);
            votes.put(token, voting);
            StringBuilder message = new StringBuilder()
                    .append(userName)
                    .append(", option is approved. ")
                    .append("Please, send the next option for this vote with /vote command")
                    .append(" or /vote with done argument to finish.");
            return message.toString();
        }
    }

    public static String results(
            Map<String, Voting> votes,
            Map<String, String> chatToken,
            String curChatId
    ) {
        String token = chatToken.get(curChatId);
        Voting voting = votes.get(token);
        Long sumOfVotes = voting.getOptionsVotesSum();
        StringBuilder result = new StringBuilder();
        result.append("Token: " + token + "\n");
        result.append("Question: " + voting.getQuestion() + "\n");
        Iterator iterator = voting.getIterator();
        while (iterator.hasNext()) {
            Option option = (Option) iterator.next();
            double percent;
            if (sumOfVotes == 0) {
                percent = 0;
            } else {
                percent = (double) option.getVotes() / sumOfVotes * 100;
            }
            result.append(option.getOrder() + " - ")
                    .append(option.getVotes() + " votes ")
                    .append("(" + (long) percent + "%) - ")
                    .append(option.getOption() + "\n");
        }
        return String.valueOf(result);
    }

    public static UserVoteStatus vote(
            Map<String, Voting> votes, Map<String, String> chatToken,
            Map<String, String> voterOption, String curChatId,
            String curUserId, String voteId
    ) {
        String token = chatToken.get(curChatId);
        Voting voting = votes.get(token);
        int voteNumber;
        try {
            voteNumber = Integer.valueOf(voteId);
        } catch (NumberFormatException exception) {
            return UserVoteStatus.WRONG_NUMBER_FORMAT;
        }
        if (voteNumber < 1 || voteNumber > voting.getOptionsCount()) {
            return UserVoteStatus.WRONG_NUMBER_CONFINES;
        } else {
            String voteNum = voterOption.get(curUserId + token);
            if (voteNum == null) {
                voterOption.put(curUserId + token, voteId);
                voting.vote(voteNumber);
                votes.put(token, voting);
                return UserVoteStatus.SUCCESS_VOTE;
            } else {
                return UserVoteStatus.ALREADY_VOTED;
            }
        }
    }

    public static String addVoteByToken(
            Map<String, String> chatToken, Map<String, String> chatUser,
            String token, String userName, String curChatId, String curUserId
    ) {
        chatToken.put(curChatId, token);
        chatUser.put(curChatId, curUserId);
        StringBuilder message = new StringBuilder()
                .append(userName)
                .append(", your vote create command is approved. ")
                .append("Your vote token is " + token)
                .append(". Use /vote command to make your choice. ");

        return message.toString();

    }
}

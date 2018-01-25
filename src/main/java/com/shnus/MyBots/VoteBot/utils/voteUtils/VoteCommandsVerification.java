package com.shnus.MyBots.VoteBot.utils.voteUtils;

import java.util.Map;

public class VoteCommandsVerification {

    public static VerificationsStatus verifyCreateVoteCommand(Map<String, String> chatToken,
                                                           Map<String, String> chatUser,
                                                           String curChatId, String curUserId){

        String token = chatToken.get(curChatId);
        if(token==null){
            return VerificationsStatus.SUCCESS_NO_VOTE;
        }
        if (token.compareTo("null")!=0) {
            if (chatUser.get(curChatId).compareTo(curUserId) == 0) {
                return VerificationsStatus.SUCCESS_USER;
            } else {
                return VerificationsStatus.WRONG_USER;
            }
        } else {
            return VerificationsStatus.SUCCESS_NO_VOTE;
        }
    }

    public static VerificationsStatus verifyVoteCommand(Map<String, String> chatToken,
                                                              Map<String, String> chatUser,
                                                              String curChatId, String curUserId){
        String token = chatToken.get(curChatId);
        if(token==null){
            return VerificationsStatus.SUCCESS_NO_VOTE;
        }
        if (token.compareTo("null")!=0) {
            if (chatUser.get(curChatId).compareTo(curUserId) == 0) {
                return VerificationsStatus.SUCCESS_USER;
            } else {
                return VerificationsStatus.SUCCESS;
            }
        } else {
            return VerificationsStatus.VOTE_NON_EXIST;
        }
    }

    public static VerificationsStatus verifyEndVoteCommand(Map<String, String> chatToken,
                                                         Map<String, String> chatUser,
                                                         String curChatId, String curUserId){
        String token = chatToken.get(curChatId);
        if(token==null){
            return VerificationsStatus.SUCCESS_NO_VOTE;
        }
        if (token.compareTo("null")!=0) {
            if (chatUser.get(curChatId).compareTo(curUserId) == 0) {
                return VerificationsStatus.SUCCESS;
            } else {
                return VerificationsStatus.WRONG_USER;
            }
        } else {
            return VerificationsStatus.VOTE_NON_EXIST;
        }
    }

    public static VerificationsStatus verifyResultsCommand(Map<String, String> chatToken,
                                                           Map<String, String> chatUser,
                                                           String curChatId, String curUserId){
        String token = chatToken.get(curChatId);
        if(token==null){
            return VerificationsStatus.SUCCESS_NO_VOTE;
        }
        if (token.compareTo("null")!=0) {
            return VerificationsStatus.SUCCESS;
        } else {
            return VerificationsStatus.VOTE_NON_EXIST;
        }
    }

}

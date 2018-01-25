package com.shnus.MyBots.VoteBot;

import com.shnus.MyBots.VoteBot.model.Voting;
import com.shnus.MyBots.VoteBot.utils.tokenUtils.TokenService;
import com.shnus.MyBots.VoteBot.utils.tokenUtils.TokenVerificationsStatus;
import com.shnus.MyBots.VoteBot.utils.voteUtils.*;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;

import java.util.Map;
import java.util.logging.Logger;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

public class VotePollingBot extends AbilityBot {

    private static Logger log = Logger.getLogger(VotePollingBot.class.getName());

    public VotePollingBot(String token) {
        super(token, "voteBossBot");
    }


    @Override
    public int creatorId() {
        return 0;
    }

    public Ability vote() {
        return Ability
                .builder()
                .name("vote")
                .info("This command help to configure new voting or vote")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    Map<String, Voting> votes = db.getMap("votes");
                    Map<String, String> chatUser = db.getMap("chatUser");
                    Map<String, String> chatToken = db.getMap("chatToken");
                    Map<String, String> voterOption = db.getMap("voterOption");
                    String curChatId = String.valueOf(ctx.chatId());
                    String curUserId = String.valueOf(ctx.user().id());

                    log.info("User " + curUserId + " wants to vote or configure vote in chat " + curChatId);

                    VerificationsStatus verifyStatus =
                            VoteCommandsVerification.verifyVoteCommand(chatToken, chatUser, curChatId, curUserId);


                    if (verifyStatus == VerificationsStatus.VOTE_NON_EXIST) {
                        log.info("User " + curUserId + " is trying to vote in chat " + curChatId + ", but voting is not exist");
                    } else {

                        VoteStatus voteStatus = VoteService.getStatus(votes, chatToken, curChatId);

                        if (voteStatus == VoteStatus.ACTIVE_VOTE) {
                            UserVoteStatus userVoteStatus = VoteService.vote(votes, chatToken, voterOption, curChatId, curUserId, ctx.firstArg());
                            if(userVoteStatus == UserVoteStatus.SUCCESS_VOTE){
                                log.info(curUserId+" successfully voted in chat "+curChatId);
                                sender.send(ctx.user().fullName()+" successfully voted for option "+ctx.firstArg(),ctx.chatId());
                            } else if(userVoteStatus == UserVoteStatus.ALREADY_VOTED) {
                                log.info(curUserId + " try to vote in chat " + curChatId + " but already voted");
                                sender.send(ctx.user().fullName() + " already voted. No tricks here!", ctx.chatId());
                            } else {
                                log.info(curUserId+" unsuccessfully tryied to vote in chat "+curChatId);
                                sender.send(ctx.user().fullName()+", wrong format option. Please, try again.",ctx.chatId());
                            }
                        } else if (VoteService.isChatOwner(chatUser, curChatId, curUserId)) {
                            log.info(voteStatus.toString());
                            if (voteStatus == VoteStatus.VOTE_WAIT_FOR_QUESTION) {
                                String question;
                                if ((question = VoteService.verifyArgs(ctx.arguments())) != null) {
                                    sender.send(VoteService.addQuestion(votes, chatToken, curChatId, question, ctx.user().fullName()), ctx.chatId());
                                    log.info("Waiting for first option from user " + curUserId + " in chat " + curChatId);
                                } else {
                                    sender.send("Please, send no empty question.", ctx.chatId());
                                    log.info("Waiting for question again (wrong format) from user " + curUserId + " in chat " + curChatId);
                                }
                            } else {
                                String option;
                                if ((option = VoteService.verifyArgs(ctx.arguments())) != null) {
                                    sender.send(VoteService.addOption(votes, chatToken, curChatId, option, ctx.user().fullName()), ctx.chatId());
                                    voteStatus =  VoteService.getStatus(votes, chatToken, curChatId);
                                    if(voteStatus==VoteStatus.ACTIVE_VOTE) {
                                        log.info("Vote finally created and in ACTIVE_STATUS by user " + curUserId + " in chat " + curChatId);
                                    } else {
                                        log.info("Waiting for next option from user " + curUserId + " in chat " + curChatId);
                                    }
                                } else {
                                    sender.send("Please, send no empty option.", ctx.chatId());
                                    log.info("Waiting for option again (wrong format) from user " + curUserId + " in chat " + curChatId);
                                }
                            }
                        }
                    }

                })
                .build();
    }

    public Ability createVote() {
        return Ability
                .builder()
                .name("newvote")
                .info("This command create new voting")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    Map<String, Voting> votes = db.getMap("votes");
                    Map<String, String> chatUser = db.getMap("chatUser");
                    Map<String, String> chatToken = db.getMap("chatToken");
                    String curChatId = String.valueOf(ctx.chatId());
                    String curUserId = String.valueOf(ctx.user().id());

                    log.info("User " + curUserId + " wants to create voting in chat " + curChatId);

                    VerificationsStatus verifyStatus =
                            VoteCommandsVerification.verifyCreateVoteCommand(chatToken, chatUser, curChatId, curUserId);


                    if (verifyStatus == VerificationsStatus.WRONG_USER) {
                        log.info("User " + curUserId + " is not the owner of current voting in chat " + curChatId);
                        sender.send("The voting can be created only by owner of current vote - " + ctx.user().fullName(), ctx.chatId());
                    } else {
                        if (verifyStatus == VerificationsStatus.SUCCESS_USER) {
                            sender.send(VoteService.results(votes, chatToken, curChatId), ctx.chatId());
                        }
                        if (ctx.arguments().length > 0) {
                            if (VoteService.isTokenExist(votes, ctx.firstArg())== TokenVerificationsStatus.TOKEN_NON_EXIST) {
                                log.info("Trying to create voting with non existing token in chat " + curChatId);
                                sender.send("Voting with this token not found", ctx.chatId());
                            } else if(VoteService.isTokenExist(votes, ctx.firstArg())== TokenVerificationsStatus.TOKEN_INCORRECT_FORMAT){
                                log.info("Trying to create voting with incorrect token in chat " + curChatId);
                                sender.send("Token format is incorrect", ctx.chatId());
                            } else {
                                sender.send(VoteService.addVoteByToken(chatToken, chatUser, ctx.firstArg(), ctx.user().fullName(), curChatId, curUserId), ctx.chatId());
                                log.info("Voting was successfully created by token in chat " + curChatId);
                            }
                        } else {

                            log.info("Creating new vote for chat " + curChatId);
                            String token = TokenService.createUniqueToken(votes);
                            sender.send(VoteService.addVoteToDb(votes, chatToken, chatUser, token, ctx.user().fullName(), curChatId, curUserId), ctx.chatId());
                            log.info("Waiting for question from user " + curUserId + " in chat " + curChatId);
                        }
                    }
                })
                .build();
    }



    public Ability results() {
        return Ability
                .builder()
                .name("results")
                .info("This command show current results")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    Map<String, Voting> votes = db.getMap("votes");
                    Map<String, String> chatUser = db.getMap("chatUser");
                    Map<String, String> chatToken = db.getMap("chatToken");
                    String curChatId = String.valueOf(ctx.chatId());
                    String curUserId = String.valueOf(ctx.user().id());

                    log.info("results command by user " + curUserId + " in chat " + curChatId);

                    VerificationsStatus verifyStatus =
                            VoteCommandsVerification.verifyResultsCommand(chatToken, chatUser, curChatId, curUserId);

                    VoteStatus voteStatus = VoteService.getStatus(votes, chatToken, curChatId);

                    if (verifyStatus == VerificationsStatus.SUCCESS && voteStatus == VoteStatus.ACTIVE_VOTE) {
                        log.info("User "+curUserId+ " from chat " + curChatId + " wants existing voting results.");
                        sender.send(VoteService.results(votes, chatToken, curChatId), ctx.chatId());
                    } else {
                        log.info("User "+curUserId+ " from chat " + curChatId + " wants non existing voting results.");
                        sender.send("No any active votings in this chat.", ctx.chatId());
                    }
                })
                .build();
    }

    public Ability endVote() {
        return Ability
                .builder()
                .name("endvote")
                .info("This command delete voting for current chat")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> {
                    Map<String, String> chatUser = db.getMap("chatUser");
                    Map<String, String> chatToken = db.getMap("chatToken");
                    String curChatId = String.valueOf(ctx.chatId());
                    String curUserId = String.valueOf(ctx.user().id());

                    log.info("endVote command by user " + curUserId + " in chat " + curChatId);

                    VerificationsStatus verifyStatus =
                            VoteCommandsVerification.verifyEndVoteCommand(chatToken, chatUser, curChatId, curUserId);

                    if (verifyStatus == VerificationsStatus.WRONG_USER) {
                        log.info("User " + curUserId + " is not the owner of current voting in chat " + curChatId);
                        sender.send("The voting can be deleted only by creator", ctx.chatId());
                    } else if(verifyStatus == VerificationsStatus.VOTE_NON_EXIST){
                        log.info("No voting in chat " + curChatId);
                        sender.send("Don't have any active votings in this chat", ctx.chatId());
                    } else {
                        chatToken.put(curChatId, "null");
                        chatUser.put(curChatId, "null");
                        log.info("Chat: " + curChatId + " deleted voting.");
                        sender.send("Current voting is successfully deleted", ctx.chatId());
                    }
                })
                .build();
    }


    @Override
    public String getBotUsername() {
        return "voteBossBot";
    }

}

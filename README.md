This is a telegram bot which allow to create votes and share them by tokens.
Three main commands: newvote, results, endvote.

newvote command create new vote. If chat already has other vote, it will be replaced by new. But this command is work only for
a creator of old vote. Other users can create new vote if chat does not have any votings. It is allowed only one bot per chat. After creating newvote, bot give for this vote unique token. Other chats can open votings by command "/newvote token" and all this votings will be united.

results command show current results of voting and also give all information about current vote(question, options, token).

endvote command delete current vote. Current vote can be deleted only by creator.

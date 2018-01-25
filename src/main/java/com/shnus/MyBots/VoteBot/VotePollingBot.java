package com.shnus.MyBots.VoteBot

import org.telegram.abilitybots.api.bot.AbilityBot
import org.telegram.abilitybots.api.objects.Ability

import org.telegram.abilitybots.api.objects.Locality.ALL
import org.telegram.abilitybots.api.objects.Privacy.PUBLIC

class VotePollingBot(token: String) : AbilityBot(token, "voteBossBot") {


    override fun creatorId(): Int {
        return 0
    }

    fun noCommand(): Ability {
        return Ability
                .builder()
                .name(AbilityBot.DEFAULT)
                .info("shit")
                .locality(ALL)
                .privacy(PUBLIC)
                .action { ctx ->
                    println(ctx.user().id().toString() + " " + ctx.chatId())
                    sender.send("shit", ctx.chatId()!!)
                }
                .build()
    }

    fun sayHelloWorld(): Ability {
        return Ability
                .builder()
                .name("hello")
                .info("says hello world!")
                .locality(ALL)
                .privacy(PUBLIC)
                .action { ctx ->
                    println(ctx.user().id().toString() + " " + ctx.chatId())
                    sender.send("Hello world!", ctx.chatId()!!)
                }
                .build()
    }

    fun createVote(): Ability {
        return Ability
                .builder()
                .name("newVote")
                .info("This command create new voting")
                .locality(ALL)
                .privacy(PUBLIC)
                .action { ctx ->
                    val statusMap = db.getMap<String, String>("status")
                    println(ctx.user().id().toString() + " " + ctx.chatId())
                    sender.send("Hello world!", ctx.chatId()!!)
                }
                .build()
    }

    fun endVote(): Ability {
        return Ability
                .builder()
                .name("endVote")
                .info("This command delete voting for current chat")
                .locality(ALL)
                .privacy(PUBLIC)
                .action { ctx ->
                    val endMap = db.getMap<String, String>("tokens")
                    if (endMap[ctx.chatId().toString()] != null) {
                        endMap.put(ctx.chatId().toString(), null)
                    }
                    println(ctx.user().id().toString() + " " + ctx.chatId())
                    sender.send("Hello world!", ctx.chatId()!!)
                }
                .build()
    }

    fun test(): Ability {
        return Ability
                .builder()
                .name("test")
                .info("just test")
                .locality(ALL)
                .privacy(PUBLIC)
                .action { ctx ->
                    val message = ctx.firstArg()
                    sender.send(message, ctx.chatId()!!)
                }
                .build()
    }

    fun useDatabaseToCountPerUser(): Ability {
        return Ability.builder()
                .name("count")
                .info("Increments a counter per user")
                .privacy(PUBLIC)
                .locality(ALL)
                .input(0)
                .action { ctx ->
                    val countMap = db.getMap<String, String>("testStrings")
                    val userId = ctx.user().id().toString()
                    val s: String
                    if (countMap.containsKey(userId))
                        s = countMap[userId] + "nusrat"
                    else
                        s = "nusrat"
                    countMap.put(userId, s)
                    val message = String.format("%s, your count is now *%s*!", ctx.user().shortName(), s)
                    sender.send(message, ctx.chatId()!!)
                }
                .build()
    }

    override fun getBotUsername(): String {
        return "voteBossBot"
    }

}

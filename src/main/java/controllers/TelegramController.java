package controllers;

import mapper.LambdaMessage;
import mapper.TLMapper;
import messenger.AndrewTelegramBot;
import messenger.MessagesReceiver;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;


@Path("/tl")
public class TelegramController {
    private Deque<Update> messages = new ArrayDeque<>();
    private MessagesReceiver messagesReceiver;
    private final TLMapper tlMapper;
    private AndrewTelegramBot andrewTelegramBot;

    public TelegramController(MessagesReceiver messagesReceiver, AndrewTelegramBot andrewTelegramBot) {
        this.messagesReceiver = messagesReceiver;
        tlMapper = new TLMapper();
        this.andrewTelegramBot = andrewTelegramBot;
    }

    @POST
    @Path("/webhook")
    public Response receive(Update update) throws IOException {
        messages.push(update);
        LambdaMessage lambdaMessage = tlMapper.map(update);
        messagesReceiver.handle(lambdaMessage);
        return Response.ok("OK").build();
    }

    @GET
    @Path("/messages")
    @Produces(MediaType.APPLICATION_JSON)
    public Queue<Update> messages() {
        return messages;
    }
}

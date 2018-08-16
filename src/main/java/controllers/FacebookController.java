package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerVerificationException;
import mapper.FbMapper;
import mapper.LambdaMessage;
import messenger.MessagesReceiver;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.Queue;

/**
 * @author by AlexBlokh, 12/8/17 (aleksandrblokh@gmail.com)
 */
@Path("/fb")
public class FacebookController {
    private Deque<JsonNode> messages = new ArrayDeque<>();
    private Messenger messenger;
    private MessagesReceiver messagesReceiver;
    private FbMapper fbMapper;

    public FacebookController(Messenger messenger, MessagesReceiver messagesReceiver) {
        this.messenger = messenger;
        this.messagesReceiver = messagesReceiver;
        this.fbMapper = new FbMapper();
    }

    @GET
    @Path("/webhook")
    public Response webhook(@QueryParam("hub.mode") String mode,
                            @QueryParam("hub.verify_token") String token,
                            @QueryParam("hub.challenge") String challenge) {

        if (mode != null && token != null) {
            return Response.status(200).entity(challenge).build();
        }

        return Response.status(403).build();
    }

    @POST
    @Path("/webhook")
    public Response receive(JsonNode body) throws IOException {
        try {
            messenger.onReceiveEvents(body.toString(), Optional.empty(), event -> {
                LambdaMessage lambdaMessage = fbMapper.map(event);
                messagesReceiver.handle(lambdaMessage);
            });
        } catch (MessengerVerificationException e) {
            e.printStackTrace();
        }
        messages.push(body);
        return Response.ok("OK").build();
    }

    @GET
    @Path("/messages")
    @Produces(MediaType.APPLICATION_JSON)
    public Queue<JsonNode> messages() {
        return messages;
    }
}

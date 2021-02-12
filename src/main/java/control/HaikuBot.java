package control;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import haiku.HaikuGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HaikuBot implements HaikuListener {

    private final static Logger log = LoggerFactory.getLogger("HaikuMain");

    private String token;
    private HaikuWindow window;

    private GatewayDiscordClient gateway;

    public static void main(String[] args) {
        HaikuBot main = new HaikuBot();
        main.start();
    }

    public void start() {
        window = new HaikuWindow();
        window.setup();

        window.postMessage("Attempting to load haiku lines from config directory...");
        HaikuGen.loadLines(window);

        if (HaikuGen.willProbablyNotWork()) {
            window.postMessage("WARNING: No lines loaded for one or more sections. The bot will not function properly!");
        } else {
            window.postMessage("Done!");
        }

        postIntroMessage(window);
        window.registerListener(this);
    }

    public void connect() {
        Thread discordThread = new Thread(() -> {
            DiscordClient client = DiscordClient.create(token);
            gateway = client.login().block();

            gateway.on(MessageCreateEvent.class)
                    .map(MessageCreateEvent::getMessage)
                    .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                    .filter(message -> message.getContent().startsWith("!haiku"))
                    .flatMap(Message::getChannel)
                    .flatMap(channel -> channel.createMessage(HaikuGen.generateRandomHaiku()))
                    .onErrorContinue((throwable, o) -> { })
                    .subscribe();

            window.postMessage("Login successful!");

            gateway.onDisconnect().block();
        });

        discordThread.setUncaughtExceptionHandler((t, e) -> {
            log.error("Exception occurred while bot running", e);
            window.postMessage("Invalid bot token: " + token + "!");
            token = null;
        });
        discordThread.start();
    }

    public void disconnect(boolean finalDeath) {
        if (gateway != null) {
            gateway.logout().block();
            gateway = null;
            if (!finalDeath) {
                window.postMessage("Disconnected successfully!");
            }
        }
    }

    @Override
    public void notifyMessage(String message) {
        if (message.equalsIgnoreCase("help")) {
            postCommandHelp(window);
        } else if (message.equalsIgnoreCase("reload")) {
            window.postMessage("Attempting to reload haiku lines from config directory...");
            if (HaikuGen.willProbablyNotWork()) {
                window.postMessage("WARNING: No lines loaded for one or more sections. The bot will not function properly!");
            } else {
                window.postMessage("Done!");
            }
        } else {
            if (token == null) {
                window.postMessage("Attempting to login with token: " + message);
                this.token = message;
                connect();
            } else {
                if (message.equalsIgnoreCase("connect")) {
                    if (gateway != null) {
                        window.postMessage("Bot is already connected!");
                    } else {
                        connect();
                    }
                } else if (message.equalsIgnoreCase("disconnect")) {
                    if (gateway == null) {
                        window.postMessage("Bot is not connected!");
                    } else {
                        disconnect(false);
                    }
                }
            }
        }
    }

    @Override
    public void notifyShutdown() {
        disconnect(true);
    }

    private void postIntroMessage(HaikuWindow window) {
        window.postMessage("======= HAIKU BOT =======");
        window.postMessage("For help, use the 'help' command.");
        window.postMessage("To start, please input a bot token.");
    }

    private void postCommandHelp(HaikuWindow window) {
        postLine(window);
        window.postMessage("help: display this again");
        window.postMessage("reload: attempt to reload haiku lines from config directory");
        window.postMessage("connect: reconnect the bot if it is disconnected");
        window.postMessage("disconnect: disconnect the bot, if connected");
        postLine(window);
    }

    private void postLine(HaikuWindow window) {
        window.postMessage("=======================");
    }
}

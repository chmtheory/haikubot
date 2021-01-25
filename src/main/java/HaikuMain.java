import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class HaikuMain {

    private final static Logger log = LoggerFactory.getLogger("HaikuMain");
    private final DiscordClient client;
    private final HaikuConfig config;

    public static void main(String[] args) {
        String token = null;
        try {
            token = HaikuUtil.getClientSecret("token");

        } catch (IOException ex) {
            log.error("Unexpected error while retrieving client secret!", ex);
        }

        HaikuConfig hc = null;
        try {
            hc = HaikuConfig.loadConfigFromDirectory("config");
        } catch (IOException ex) {
            log.error("Unexpected error while retrieving configuration files!", ex);
        }

        if (token != null && hc != null) {
            HaikuMain main = new HaikuMain(token, hc);
            main.connect();
        }
    }

    public HaikuMain(String token, HaikuConfig config) {
        client = DiscordClient.create(token);
        this.config = config;
    }

    public void connect() {
        final GatewayDiscordClient gateway = client.login().block();
        final Snowflake selfId = gateway.getSelfId();

        gateway.on(MessageCreateEvent.class).subscribe(event -> {
            Message message = event.getMessage();
            if (message.getUserMentionIds().contains(selfId)) {
                if (message.getContent().contains(config.getCommandWord())) {
                    event.getMessage().getChannel().block().createEmbed(embed -> {
                        embed.setDescription(generateHaiku());
                    }).block();
                }
            }
        });

        gateway.onDisconnect().block();
    }

    private String generateHaiku() {
        return String.format("%s\n%s\n%s", config.getRandomFirstLine(), config.getRandomSecondLine(), config.getRandomThirdLine());
    }



}

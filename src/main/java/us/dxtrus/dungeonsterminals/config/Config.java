package us.dxtrus.dungeonsterminals.config;

import de.exlll.configlib.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.dxtrus.commons.config.AutoReload;
import us.dxtrus.dungeonsterminals.DungeonsTerminals;
import us.dxtrus.dungeonsterminals.managers.LogManager;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

@SuppressWarnings("FieldMayBeFinal")
@Getter
@Configuration
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Config {
    private static Config instance;

    private static final String CONFIG_HEADER = """
            #########################################
            #            Terminals Config           #
            #########################################
            """;

    private String terminalComplete = "&aTerminal Complete!";
    private String terminalFailed = "&aTerminal Failed!";

    @Comment("In milliseconds")
    private long terminalFailCooldown = 6000;

    private String cooldownMessage = "&cTerminal on cooldown! &7(Wait: %s seconds)";

    private Terminals terminals = new Terminals();

    @Getter
    @Configuration
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Terminals {
//        private Snake snake = new Snake();
//
//        @Getter
//        @Configuration
//        @NoArgsConstructor(access = AccessLevel.PRIVATE)
//        public static class Snake {
//            private String title = "Follow the Path";
//            private String name = "Snake";
//
//            @Comment("How long the player has to complete the path.")
//            private int previewSeconds = 5;
//        }

        private Memorize memorize = new Memorize();

        @Getter
        @Configuration
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Memorize {
            private String title = "Memorize the Items";
            private String name = "Memorize";

            @Comment("How long the player can see the correct items for.")
            private int previewSeconds = 3;
        }

        private Switches switches = new Switches();

        @Getter
        @Configuration
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Switches {
            private String title = "Turn all the switches on";
            private String name = "Switch";
            @Comment("How long the player has to turn all the switches on.")
            private int timeAllowed = 3;

            private OnButton onButton = new OnButton();

            @Getter
            @Configuration
            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            public static class OnButton {
                private String name = "&a&lON";
                private List<String> lore = List.of("&7Click to turn off");
            }

            private OffButton offButton = new OffButton();

            @Getter
            @Configuration
            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            public static class OffButton {
                private String name = "&c&lOFF";
                private List<String> lore = List.of("&7Click to turn on");
            }

            @Comment("The percentage of whether the switch should start on or off.")
            private Chances chances = new Chances();

            @Getter
            @Configuration
            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            public static class Chances {
                private double first = 20.0;
                private double second = 35.0;
                private double third = 50.0;
                private double forth = 15.0;
                private double fifth = 40.0;
            }
        }
    }

    private Commands commands = new Commands();

    @Getter
    @Configuration
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Commands {
        private String terminalCreated = "&aCreated %s terminal at %s in dungeon %s";
        private String terminalRemoved = "&aRemoved %s terminal at %s in dungeon %s";

        private String invalidUsageMain = "&cUsage: &f/terminals <add|remove>";
        private String invalidUsageAdd = "&cUsage: /terminals add <id> <type>";
        private String invalidType = "&cInvalid Type!";
        private String mustBeEditing = "&cYou must be editing a dungeon!";
        private String mustBeLookingAtBlock = "&cYou must be looking at a block!";
        private String terminalExistsAtThisLoc = "&cThere is already a terminal at this location!";
        private String terminalExistsWithThisId = "&cThere is already a terminal with this ID!";
        private String noTerminalAtLoc = "&cThere is no terminal at this location!";
        private String noTerminalWithId = "&cThere is no terminal with this ID!";
    }

    private static final YamlConfigurationProperties PROPERTIES = YamlConfigurationProperties.newBuilder()
            .charset(StandardCharsets.UTF_8)
            .setNameFormatter(NameFormatters.LOWER_KEBAB_CASE)
            .header(CONFIG_HEADER).build();

    private static Path getConfigDirectory() {
        return DungeonsTerminals.getInstance().getDataFolder().toPath();
    }

    public static void reload() {
        instance = YamlConfigurations
                .load(getConfigDirectory().resolve("config.yml"), Config.class, PROPERTIES);
        LogManager.info("Configuration automatically reloaded from disk.");
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = YamlConfigurations
                    .update(getConfigDirectory().resolve("config.yml"), Config.class, PROPERTIES);
            AutoReload.watch(getConfigDirectory(), "config.yml", Config::reload);
        }
        return instance;
    }
}

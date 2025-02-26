package com.spektrsoyuz.pillar.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.config.ConfigManager;
import com.spektrsoyuz.pillar.config.ConfigPlaceholder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings({"UnstableApiUsage"})
public final class SingleGamemodeCommands {

    private final ConfigManager config;

    public SingleGamemodeCommands(final PillarPlugin plugin, final Commands registrar) {
        this.config = plugin.getConfigManager();

        registrar.register(getSingleCommand("gma", GameMode.ADVENTURE), "Set gamemode to Adventure");
        registrar.register(getSingleCommand("gmc", GameMode.ADVENTURE), "Set gamemode to Creative");
        registrar.register(getSingleCommand("gms", GameMode.ADVENTURE), "Set gamemode to Survival");
        registrar.register(getSingleCommand("gmsp", GameMode.ADVENTURE), "Set gamemode to Spectator");
    }

    public LiteralCommandNode<CommandSourceStack> getSingleCommand(final String label, final GameMode gameMode) {
        return Commands.literal(label)
                .requires(stack -> {
                    CommandSender sender = stack.getSender();
                    return sender.hasPermission("pillar.command.gamemode") && sender instanceof Player;
                })
                .executes(context -> setGamemode(context, gameMode))
                .then(Commands.argument("target", ArgumentTypes.player())
                        .executes(context -> setGamemodeOther(context, gameMode)))
                .build();
    }

    private int setGamemode(final CommandContext<CommandSourceStack> context, final GameMode gameMode) {
        final Player player = (Player) context.getSource().getSender();
        final String gameModeString = StringUtils.capitalize(gameMode.name().toLowerCase());

        player.setGameMode(gameMode);
        player.sendMessage(config.getMessage("command-broadcast", new ConfigPlaceholder("gamemode", gameModeString)));
        return Command.SINGLE_SUCCESS;
    }

    private int setGamemodeOther(final CommandContext<CommandSourceStack> context, final GameMode gameMode) {
        final CommandSender sender = context.getSource().getSender();
        final String gameModeString = StringUtils.capitalize(gameMode.name().toLowerCase());
        final PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);

        try {
            final Player target = targetResolver.resolve(context.getSource()).getFirst();
            target.setGameMode(gameMode);
            target.sendMessage(config.getMessage("command-broadcast", new ConfigPlaceholder("gamemode", gameModeString)));
            sender.sendMessage(config.getMessage("command-broadcast-other",
                    new ConfigPlaceholder("gamemode", gameModeString),
                    new ConfigPlaceholder("target", target.getName())));
        } catch (CommandSyntaxException e) {
            sender.sendMessage(Component.text(e.getMessage(), NamedTextColor.RED));
        }
        return Command.SINGLE_SUCCESS;
    }
}

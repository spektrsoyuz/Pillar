package com.spektrsoyuz.pillar.command.server;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.spektrsoyuz.pillar.PillarPlugin;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

@SuppressWarnings({"UnstableApiUsage"})
public class BroadcastCommand {

    private final PillarPlugin plugin;

    public BroadcastCommand(final PillarPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(final Commands registrar) {
        final LiteralCommandNode<CommandSourceStack> node = Commands.literal("broadcast")
                .requires(stack -> stack.getSender().hasPermission("pillar.command.broadcast"))
                .executes(context -> {
                    final CommandSender sender = context.getSource().getSender();
                    sender.sendMessage(Component.text("/" + context.getInput() + " <message>", NamedTextColor.RED));
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(this::broadcast))
                .build();

        registrar.register(node);
    }

    private int broadcast(final CommandContext<CommandSourceStack> context) {
        final String message = context.getArgument("message", String.class);
        final Component component = plugin.config().getMessage("command.broadcast", null, Map.of("message", message));
        plugin.getServer().broadcast(component);
        return Command.SINGLE_SUCCESS;
    }
}

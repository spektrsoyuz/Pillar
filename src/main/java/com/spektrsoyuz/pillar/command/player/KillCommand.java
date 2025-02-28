/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.PillarUtils;
import com.spektrsoyuz.pillar.config.ConfigPlaceholder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings({"UnstableApiUsage"})
public final class KillCommand {

    // Constructor
    public KillCommand(final PillarPlugin plugin, final Commands registrar) {
        final LiteralCommandNode<CommandSourceStack> node = Commands.literal("kill")
                .requires(stack -> {
                    final CommandSender sender = stack.getSender();
                    return sender.hasPermission(PillarUtils.PERMISSION_COMMAND_KILL) && sender instanceof Player;
                })
                .executes(context -> {
                    final Player player = (Player) context.getSource().getSender();
                    player.setHealth(0);
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("target", ArgumentTypes.player())
                        .executes(context -> {
                            final CommandSender sender = context.getSource().getSender();
                            final PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);

                            try {
                                final List<Player> targets = targetResolver.resolve(context.getSource());
                                for (Player target : targets) {
                                    target.setHealth(0);
                                    sender.sendMessage(plugin.getConfigManager().getMessage("command-kill", new ConfigPlaceholder("target", target.getName())));
                                    if (target.hasPermission(PillarUtils.PERMISSION_COMMAND_KILL_SEE)) {
                                        target.sendMessage(plugin.getConfigManager().getMessage("command-kill-see", new ConfigPlaceholder("killer", sender.getName())));
                                    }
                                }
                            } catch (CommandSyntaxException e) {
                                sender.sendMessage(Component.text(e.getMessage(), NamedTextColor.RED));
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .build();

        registrar.register(node, "Kill a player");
    }
}

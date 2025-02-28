/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.command.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.PillarUtils;
import com.spektrsoyuz.pillar.player.PillarPlayer;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings({"UnstableApiUsage"})
public final class BackCommand {

    // Constructor
    public BackCommand(final PillarPlugin plugin, final Commands registrar) {
        final LiteralCommandNode<CommandSourceStack> node = Commands.literal("back")
                .requires(stack -> {
                    final CommandSender sender = stack.getSender();
                    return sender.hasPermission(PillarUtils.PERMISSION_COMMAND_BACK) && sender instanceof Player;
                })
                .executes(context -> {
                    final Player player = (Player) context.getSource().getSender();
                    final PillarPlayer pillarPlayer = plugin.getPillarPlayerManager().getPlayer(player);

                    player.teleport(pillarPlayer.getBackLocation());
                    player.sendMessage(plugin.getConfigManager().getMessage("command-back"));
                    return Command.SINGLE_SUCCESS;
                })
                .build();

        registrar.register(node, "Return to your previous position");
    }
}

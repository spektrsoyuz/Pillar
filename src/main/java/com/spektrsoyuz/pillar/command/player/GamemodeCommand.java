/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.command.player;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.PillarUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.GameMode;

@SuppressWarnings({"UnstableApiUsage"})
public final class GamemodeCommand {

    // Constructor
    public GamemodeCommand(final PillarPlugin plugin, final Commands registrar) {
        final LiteralCommandNode<CommandSourceStack> node = Commands.literal("gamemode")
                .requires(stack -> stack.getSender().hasPermission(PillarUtils.PERMISSION_COMMAND_GAMEMODE))
                .then(Commands.argument("gamemode", ArgumentTypes.gameMode())
                        .requires(stack -> stack.getSender().hasPermission(PillarUtils.PERMISSION_COMMAND_GAMEMODE_OTHER))
                        .executes(context -> PillarUtils.setGamemode(plugin, context, context.getArgument("gamemode", GameMode.class)))
                        .then(Commands.argument("target", ArgumentTypes.player())
                                .executes(context -> PillarUtils.setGamemodeOther(plugin, context, context.getArgument("gamemode", GameMode.class)))))
                .build();

        registrar.register(node, "Set gamemode");
    }
}

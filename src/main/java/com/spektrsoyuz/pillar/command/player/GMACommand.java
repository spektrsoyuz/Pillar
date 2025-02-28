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
public final class GMACommand {

    // Constructor
    public GMACommand(final PillarPlugin plugin, final Commands registrar) {
        final LiteralCommandNode<CommandSourceStack> node = Commands.literal("gma")
                .requires(stack -> stack.getSender().hasPermission(PillarUtils.PERMISSION_COMMAND_GAMEMODE))
                .executes(context -> PillarUtils.setGamemode(plugin, context, GameMode.ADVENTURE))
                .then(Commands.argument("target", ArgumentTypes.player())
                        .requires(stack -> stack.getSender().hasPermission(PillarUtils.PERMISSION_COMMAND_GAMEMODE_OTHER))
                        .executes(context -> PillarUtils.setGamemodeOther(plugin, context, GameMode.ADVENTURE)))
                .build();

        registrar.register(node, "Set gamemode to Adventure");
    }
}

/*
 * Pillar
 * Created by SpektrSoyuz
 * All Rights Reserved
 */
package com.spektrsoyuz.pillar.command.item;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.PillarUtils;
import com.spektrsoyuz.pillar.config.ConfigPlaceholder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings({"UnstableApiUsage"})
public final class ItemCommand {

    // Constructor
    public ItemCommand(final PillarPlugin plugin, final Commands registrar) {
        final LiteralCommandNode<CommandSourceStack> node = Commands.literal("item")
                .requires(stack -> {
                    final CommandSender sender = stack.getSender();
                    return sender.hasPermission(PillarUtils.PERMISSION_COMMAND_ITEM) && sender instanceof Player;
                })
                .then(Commands.argument("item", ArgumentTypes.itemStack())
                        .executes(context -> {
                            final Player player = (Player) context.getSource().getSender();
                            final ItemStack itemStack = context.getArgument("item", ItemStack.class);

                            final String itemName = PlainTextComponentSerializer.plainText().serialize(itemStack.displayName());

                            player.give(itemStack);
                            player.sendMessage(plugin.getConfigManager().getMessage("command-item",
                                    new ConfigPlaceholder("item", itemName),
                                    new ConfigPlaceholder("amount", "1")));
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                .executes(context -> {
                                    final Player player = (Player) context.getSource().getSender();
                                    final ItemStack itemStack = context.getArgument("item", ItemStack.class);
                                    final int amount = context.getArgument("amount", Integer.class);

                                    final String itemName = PlainTextComponentSerializer.plainText().serialize(itemStack.displayName());
                                    itemStack.setAmount(amount);

                                    player.give(itemStack);
                                    player.sendMessage(plugin.getConfigManager().getMessage("command-item",
                                            new ConfigPlaceholder("item", itemName),
                                            new ConfigPlaceholder("amount", String.valueOf(amount))));
                                    return Command.SINGLE_SUCCESS;
                                })))
                .build();

        registrar.register(node, "Summon an item");
    }
}

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

import java.util.List;

@SuppressWarnings({"UnstableApiUsage"})
public final class ItemCommand {

    private final PillarPlugin plugin;

    // Constructor
    public ItemCommand(final PillarPlugin plugin, final Commands registrar) {
        this.plugin = plugin;

        final LiteralCommandNode<CommandSourceStack> node = Commands.literal("item")
                .requires(stack -> {
                    final CommandSender sender = stack.getSender();
                    return sender.hasPermission(PillarUtils.PERMISSION_COMMAND_ITEM) && sender instanceof Player;
                })
                .then(Commands.argument("item", ArgumentTypes.itemStack())
                        .executes(context -> {
                            final Player player = (Player) context.getSource().getSender();
                            final ItemStack itemStack = context.getArgument("item", ItemStack.class);

                            return giveItem(player, itemStack, 1);
                        })
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
                                .executes(context -> {
                                    final Player player = (Player) context.getSource().getSender();
                                    final ItemStack itemStack = context.getArgument("item", ItemStack.class);
                                    final int amount = context.getArgument("amount", Integer.class);

                                    return giveItem(player, itemStack, amount);
                                })))
                .build();

        registrar.register(node, "Summon an item", List.of("i"));
    }

    // Method to give a player an item
    private int giveItem(final Player player, final ItemStack itemStack, int amount) {
        final String itemName = PlainTextComponentSerializer.plainText().serialize(itemStack.displayName());
        int remainingAmount = amount;

        while (remainingAmount > 0) {
            int stackAmount = Math.min(remainingAmount, 64);
            ItemStack stack = itemStack.clone();
            stack.setAmount(stackAmount);
            player.give(stack);
            remainingAmount -= stackAmount;
        }

        player.sendMessage(plugin.getConfigManager().getMessage("command-item",
                new ConfigPlaceholder("item", itemName),
                new ConfigPlaceholder("amount", String.valueOf(amount))));
        return Command.SINGLE_SUCCESS;
    }
}

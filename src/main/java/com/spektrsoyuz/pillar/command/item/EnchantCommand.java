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
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@SuppressWarnings({"UnstableApiUsage"})
public final class EnchantCommand {

    private final PillarPlugin plugin;

    // Constructor
    public EnchantCommand(final PillarPlugin plugin, final Commands registrar) {
        this.plugin = plugin;

        final LiteralCommandNode<CommandSourceStack> node = Commands.literal("enchant")
                .requires(stack -> {
                    final CommandSender sender = stack.getSender();
                    return sender.hasPermission(PillarUtils.PERMISSION_COMMAND_ENCHANT) && sender instanceof Player;
                })
                .then(Commands.argument("enchantment", ArgumentTypes.resource(RegistryKey.ENCHANTMENT))
                        .executes(context -> {
                            final Player player = (Player) context.getSource().getSender();
                            final Enchantment enchantment = context.getArgument("enchantment", Enchantment.class);
                            final ItemStack itemStack = player.getInventory().getItemInMainHand();

                            return enchantItem(player, itemStack, enchantment, 1);
                        })
                        .then(Commands.argument("level", IntegerArgumentType.integer(1))
                                .executes(context -> {
                                    final Player player = (Player) context.getSource().getSender();
                                    final Enchantment enchantment = context.getArgument("enchantment", Enchantment.class);
                                    final ItemStack itemStack = player.getInventory().getItemInMainHand();
                                    final int level = context.getArgument("level", Integer.class);

                                    return enchantItem(player, itemStack, enchantment, level);
                                })))
                .build();

        registrar.register(node, "Enchant an item");
    }

    private int enchantItem(final Player player, final ItemStack itemStack, final Enchantment enchantment, final int level) {
        final String itemName = PlainTextComponentSerializer.plainText().serialize(itemStack.displayName());
        final String enchantmentName = PlainTextComponentSerializer.plainText().serialize(enchantment.displayName(level));

        if (itemStack.isEmpty()) {
            player.sendMessage(plugin.getConfigManager().getMessage("command-enchant-air"));
        } else {
            final Component successMessage = plugin.getConfigManager().getMessage("command-enchant",
                    new ConfigPlaceholder("item", itemName),
                    new ConfigPlaceholder("enchantment", enchantmentName));
            final Component failMessage = plugin.getConfigManager().getMessage("command-enchant-fail");

            try {
                itemStack.addEnchantment(enchantment, level);
                player.sendMessage(successMessage);
            } catch (IllegalArgumentException ex) {
                if (player.hasPermission(PillarUtils.PERMISSION_COMMAND_ENCHANT_UNSAFE)) {
                    itemStack.addUnsafeEnchantment(enchantment, level);
                    player.sendMessage(successMessage);
                } else {
                    player.sendMessage(failMessage);
                }
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}

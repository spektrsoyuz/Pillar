package com.spektrsoyuz.pillar.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.spektrsoyuz.pillar.PillarPlugin;
import com.spektrsoyuz.pillar.exception.PlayerNotFoundException;
import com.spektrsoyuz.pillar.player.PillarPlayer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
@SuppressWarnings({"UnstableApiUsage"})
public class PillarPlayerArgumentType implements CustomArgumentType.Converted<PillarPlayer, String> {

    private final PillarPlugin plugin;

    public PillarPlayerArgumentType(final PillarPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public PillarPlayer convert(String nativeType) throws CommandSyntaxException {
        final PillarPlayer pillarPlayer = plugin.getPillarPlayerManager().getPlayer(nativeType);

        if (pillarPlayer != null) {
            return pillarPlayer;
        }
        throw new PlayerNotFoundException(nativeType).create();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (final PillarPlayer pillarPlayer : plugin.getPillarPlayerManager().getPlayers()) {
            if (pillarPlayer.getUsername().toLowerCase().startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(pillarPlayer.getUsername());
            }
        }
        return builder.buildFuture();
    }
}

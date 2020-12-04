/*
 * Copyright 2020 Jade Krabbe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.jadethecat.tag;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.jadethecat.tag.tag.CustomTagManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TagAPI implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("TagAPI");

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(literal("listtag").then(argument("tag_type", word())
                    .suggests(new TagTypeSuggestionProvider())
                    .then(argument("tag", string())
                            .suggests(new TagSuggestionProvider()).executes(context -> {
                                String tagType = getString(context, "tag_type");
                                CustomTagManager tagManager;
                                if (ServerTagManagerHolder.getTagManager() instanceof CustomTagManager)
                                    tagManager = (CustomTagManager) ServerTagManagerHolder.getTagManager();
                                else
                                    return 0;
                                StringBuilder sbuild = new StringBuilder();
                                switch (tagType) {
                                    case "item":
                                        Tag<Item> tag = tagManager.getItems().getTag(new Identifier(getString(context, "tag")));
                                        sbuild.append("The following items are in the tag:\n");
                                        for (Item item : tag.values())
                                            sbuild.append(Registry.ITEM.getId(item).toString()).append("\n");
                                        context.getSource().sendFeedback(new LiteralText(sbuild.toString()), false);
                                        return Command.SINGLE_SUCCESS;
                                    case "block":
                                        Tag<Block> tag2 = tagManager.getBlocks().getTag(new Identifier(getString(context, "tag")));
                                        sbuild.append("The following blocks are in the tag:\n");
                                        for (Block block : tag2.values())
                                            sbuild.append(Registry.BLOCK.getId(block).toString()).append("\n");
                                        context.getSource().sendFeedback(new LiteralText(sbuild.toString()), false);
                                        return Command.SINGLE_SUCCESS;
                                    case "entity_type":
                                        Tag<EntityType<?>> tag3 = tagManager.getEntityTypes().getTag(new Identifier(getString(context, "tag")));
                                        sbuild.append("The following entityTypes are in the tag:\n");
                                        for (EntityType<?> entityType : tag3.values())
                                            sbuild.append(Registry.ENTITY_TYPE.getId(entityType).toString()).append("\n");
                                        context.getSource().sendFeedback(new LiteralText(sbuild.toString()), false);
                                        return Command.SINGLE_SUCCESS;
                                    case "biome":
                                        Tag<Biome> tag4 = tagManager.getBiomes().getTag(new Identifier(getString(context, "tag")));
                                        sbuild.append("The following biomes are in the tag:\n");
                                        for (Biome biome : tag4.values())
                                            sbuild.append(BuiltinRegistries.BIOME.getId(biome).toString()).append("\n");
                                        context.getSource().sendFeedback(new LiteralText(sbuild.toString()), false);
                                        return Command.SINGLE_SUCCESS;
                                    case "enchantment":
                                        Tag<Enchantment> tag5 = tagManager.getEnchantments().getTag(new Identifier(getString(context, "tag")));
                                        sbuild.append("The following enchantments are in the tag:\n");
                                        for (Enchantment enchantment : tag5.values())
                                            sbuild.append(Registry.ENCHANTMENT.getId(enchantment).toString()).append("\n");
                                        context.getSource().sendFeedback(new LiteralText(sbuild.toString()), false);
                                        return Command.SINGLE_SUCCESS;
                                    case "block_entity_type":
                                        Tag<BlockEntityType<?>> tag6 = tagManager.getBlockEntityTypes().getTag(new Identifier(getString(context, "tag")));
                                        sbuild.append("The following blockEntityTypes are in the tag:\n");
                                        for (BlockEntityType<?> blockEntityType : tag6.values())
                                            sbuild.append(Registry.BLOCK_ENTITY_TYPE.getId(blockEntityType).toString()).append("\n");
                                        context.getSource().sendFeedback(new LiteralText(sbuild.toString()), false);
                                        return Command.SINGLE_SUCCESS;
                                    default:
                                        context.getSource().sendFeedback(new LiteralText("Unknown tag type"), false);
                                        return Command.SINGLE_SUCCESS;
                                }
                            })).executes(context -> {
                        String tagType = getString(context, "tag_type");
                        CustomTagManager tagManager;
                        if (ServerTagManagerHolder.getTagManager() instanceof CustomTagManager)
                            tagManager = (CustomTagManager) ServerTagManagerHolder.getTagManager();
                        else
                            return 0;
                        StringBuilder sbuild = new StringBuilder();
                        switch (tagType) {
                            case "item":
                                sbuild.append("The following item tags exist:\n");
                                for (Identifier id : tagManager.getItems().getTags().keySet())
                                    sbuild.append(id.toString()).append("\n");
                                context.getSource().sendFeedback(new LiteralText(sbuild.toString()), false);
                                return Command.SINGLE_SUCCESS;
                            case "block":
                                sbuild.append("The following block tags exist:\n");
                                for (Identifier id : tagManager.getBlocks().getTags().keySet())
                                    sbuild.append(id.toString()).append("\n");
                                context.getSource().sendFeedback(new LiteralText(sbuild.toString()), false);
                                return Command.SINGLE_SUCCESS;
                            case "entity_type":
                                sbuild.append("The following entityType tags exist:\n");
                                for (Identifier id : tagManager.getEntityTypes().getTags().keySet())
                                    sbuild.append(id.toString()).append("\n");
                                context.getSource().sendFeedback(new LiteralText(sbuild.toString()), false);
                                return Command.SINGLE_SUCCESS;
                            case "biome":
                                sbuild.append("The following biome tags exist:\n");
                                for (Identifier id : tagManager.getBiomes().getTags().keySet())
                                    sbuild.append(id.toString()).append("\n");
                                context.getSource().sendFeedback(new LiteralText(sbuild.toString()), false);
                                return Command.SINGLE_SUCCESS;
                            case "enchantment":
                                sbuild.append("The following enchantment tags exist:\n");
                                for (Identifier id : tagManager.getEnchantments().getTags().keySet())
                                    sbuild.append(id.toString()).append("\n");
                                context.getSource().sendFeedback(new LiteralText(sbuild.toString()), false);
                                return Command.SINGLE_SUCCESS;
                            case "block_entity_type":
                                sbuild.append("The following blockEntityType tags exist:\n");
                                for (Identifier id : tagManager.getBlockEntityTypes().getTags().keySet())
                                    sbuild.append(id.toString()).append("\n");
                                context.getSource().sendFeedback(new LiteralText(sbuild.toString()), false);
                                return Command.SINGLE_SUCCESS;
                            default:
                                context.getSource().sendFeedback(new LiteralText("Unknown tag type"), false);
                                return Command.SINGLE_SUCCESS;
                        }
                    })));
        });
    }

    static class TagTypeSuggestionProvider implements SuggestionProvider<ServerCommandSource> {

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            builder.suggest("item").suggest("block").suggest("entity_type").suggest("biome").suggest("enchantment").suggest("block_entity_type");
            return builder.buildFuture();
        }
    }

    static class TagSuggestionProvider implements SuggestionProvider<ServerCommandSource> {

        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            String tagType = getString(context, "tag_type");
            CustomTagManager tagManager;
            if (ServerTagManagerHolder.getTagManager() instanceof CustomTagManager)
                tagManager = (CustomTagManager) ServerTagManagerHolder.getTagManager();
            else
                return builder.buildFuture();
            switch (tagType) {
                case "item":
                    for (Identifier id : tagManager.getItems().getTags().keySet())
                        builder.suggest('"' + id.toString() + '"');
                    break;
                case "block":
                    for (Identifier id : tagManager.getBlocks().getTags().keySet())
                        builder.suggest('"' + id.toString() + '"');
                    break;
                case "entity_type":
                    for (Identifier id : tagManager.getEntityTypes().getTags().keySet())
                        builder.suggest('"' + id.toString() + '"');
                    break;
                case "biome":
                    for (Identifier id : tagManager.getBiomes().getTags().keySet())
                        builder.suggest('"' + id.toString() + '"');
                    break;
                case "enchantment":
                    for (Identifier id : tagManager.getEnchantments().getTags().keySet())
                        builder.suggest('"' + id.toString() + '"');
                    break;
                case "block_entity_type":
                    for (Identifier id : tagManager.getBlockEntityTypes().getTags().keySet())
                        builder.suggest('"' + id.toString() + '"');
                    break;
                default:
                    break;
            }
            return builder.buildFuture();
        }
    }

}

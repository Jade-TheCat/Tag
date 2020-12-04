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

package me.jadethecat.tag.mixin;

import com.google.common.collect.Multimap;
import me.jadethecat.tag.tag.CustomTagManager;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.tag.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Mixin(TagManagerLoader.class)
public class TagManagerLoaderMixin {
    @Shadow
    @Final
    private TagGroupLoader<Block> blocks;
    @Shadow
    @Final
    private TagGroupLoader<Item> items;
    @Shadow
    @Final
    private TagGroupLoader<Fluid> fluids;
    @Shadow
    @Final
    private TagGroupLoader<EntityType<?>> entityTypes;
    @Shadow
    private TagManager tagManager;

    private TagGroupLoader<Biome> biomeLoader;
    private TagGroupLoader<Enchantment> enchantmentLoader;
    private TagGroupLoader<Feature<?>> featureLoader;
    private TagGroupLoader<BlockEntityType<?>> blockEntityTypeLoader;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectIntoInit(CallbackInfo ci) {
        this.biomeLoader = new TagGroupLoader<>(BuiltinRegistries.BIOME::getOrEmpty, "tags/biomes", "biome");
        this.enchantmentLoader = new TagGroupLoader<>(Registry.ENCHANTMENT::getOrEmpty, "tags/enchantments", "enchantment");
        this.featureLoader = new TagGroupLoader<>(Registry.FEATURE::getOrEmpty, "tags/features", "feature");
        this.blockEntityTypeLoader = new TagGroupLoader<>(Registry.BLOCK_ENTITY_TYPE::getOrEmpty, "tags/block_entity_types", "block_entity_type");
    }

    /*
    If anyone has a better way to do this, please make a PR.
     */
    @Inject(method = "Lnet/minecraft/tag/TagManagerLoader;reload(Lnet/minecraft/resource/ResourceReloadListener$Synchronizer;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;Lnet/minecraft/util/profiler/Profiler;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
            , at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/tag/TagGroupLoader;prepareReload(Lnet/minecraft/resource/ResourceManager;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", ordinal = 3), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void addTags(ResourceReloadListener.Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor, CallbackInfoReturnable<CompletableFuture<Void>> ci, CompletableFuture<Map<Identifier, Tag.Builder>> completableFuture, CompletableFuture<Map<Identifier, Tag.Builder>> completableFuture2, CompletableFuture<Map<Identifier, Tag.Builder>> completableFuture3, CompletableFuture<Map<Identifier, Tag.Builder>> completableFuture4) {
        CompletableFuture<Map<Identifier, Tag.Builder>> biomeCompletableFuture = this.biomeLoader.prepareReload(manager,
                prepareExecutor);
        CompletableFuture<Map<Identifier, Tag.Builder>> enchantmentCompletableFuture =
                this.enchantmentLoader.prepareReload(manager, prepareExecutor);
        CompletableFuture<Map<Identifier, Tag.Builder>> featureCompletableFuture =
                this.featureLoader.prepareReload(manager, prepareExecutor);
        CompletableFuture<Map<Identifier, Tag.Builder>> blockEntityTypeCompletableFuture =
                this.blockEntityTypeLoader.prepareReload(manager, prepareExecutor);

        CompletableFuture var10000 = CompletableFuture.allOf(completableFuture, completableFuture2, completableFuture3,
                completableFuture4, biomeCompletableFuture, enchantmentCompletableFuture, featureCompletableFuture,
                blockEntityTypeCompletableFuture);
        synchronizer.getClass();
        ci.setReturnValue(var10000.thenCompose(synchronizer::whenPrepared).thenAcceptAsync((void_) -> {
            TagGroup<Block> tagGroup = this.blocks.applyReload((Map) completableFuture.join());
            TagGroup<Item> tagGroup2 = this.items.applyReload((Map) completableFuture2.join());
            TagGroup<Fluid> tagGroup3 = this.fluids.applyReload((Map) completableFuture3.join());
            TagGroup<EntityType<?>> tagGroup4 = this.entityTypes.applyReload((Map) completableFuture4.join());
            TagGroup<Biome> biomeTagGroup = this.biomeLoader.applyReload((Map) biomeCompletableFuture.join());
            TagGroup<Enchantment> enchantmentTagGroup = this.enchantmentLoader.applyReload((Map) enchantmentCompletableFuture.join());
            TagGroup<Feature<?>> featureTagGroup = this.featureLoader.applyReload((Map) featureCompletableFuture.join());
            TagGroup<BlockEntityType<?>> blockEntityTypeTagGroup = this.blockEntityTypeLoader.applyReload((Map) blockEntityTypeCompletableFuture.join());
            TagManager tagManager = new CustomTagManager(TagManager.create(tagGroup, tagGroup2, tagGroup3, tagGroup4),
                    biomeTagGroup, enchantmentTagGroup, featureTagGroup, blockEntityTypeTagGroup);
            Multimap<Identifier, Identifier> multimap = RequiredTagListRegistry.getMissingTags(tagManager);
            if (!multimap.isEmpty()) {
                throw new IllegalStateException("Missing required tags: " + (String) multimap.entries().stream().map((entry) -> {
                    return entry.getKey() + ":" + entry.getValue();
                }).sorted().collect(Collectors.joining(",")));
            } else {
                ServerTagManagerHolder.setTagManager(tagManager);
                this.tagManager = tagManager;
            }
        }, applyExecutor));
    }
}

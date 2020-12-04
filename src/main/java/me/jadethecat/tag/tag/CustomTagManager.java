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

package me.jadethecat.tag.tag;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tag.TagGroup;
import net.minecraft.tag.TagManager;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;

public class CustomTagManager implements TagManager {
    public static CustomTagManager EMPTY = new CustomTagManager(TagManager.EMPTY, TagGroup.createEmpty(), TagGroup.createEmpty(), TagGroup.createEmpty(), TagGroup.createEmpty());

    private final TagManager vanillaManager;

    private final TagGroup<Biome> biomes;


    private final TagGroup<Enchantment> enchantments;
    private final TagGroup<Feature<?>> features;
    private final TagGroup<BlockEntityType<?>> blockEntityTypes;

    public CustomTagManager(TagManager vanillaManager, TagGroup<Biome> biomeTagGroup, TagGroup<Enchantment> enchantmentTagGroup,
                            TagGroup<Feature<?>> featureTagGroup, TagGroup<BlockEntityType<?>> blockEntityTypeTagGroup) {
        this.vanillaManager = vanillaManager;
        this.biomes = biomeTagGroup;
        this.enchantments = enchantmentTagGroup;
        this.features = featureTagGroup;
        this.blockEntityTypes = blockEntityTypeTagGroup;
    }

    public TagGroup<Biome> getBiomes() {
        return biomes;
    }

    public TagGroup<Enchantment> getEnchantments() {
        return enchantments;
    }

    public TagGroup<Feature<?>> getFeatures() {
        return features;
    }

    public TagGroup<BlockEntityType<?>> getBlockEntityTypes() {
        return blockEntityTypes;
    }

    @Override
    public TagGroup<Block> getBlocks() {
        return vanillaManager.getBlocks();
    }

    @Override
    public TagGroup<Item> getItems() {
        return vanillaManager.getItems();
    }

    @Override
    public TagGroup<Fluid> getFluids() {
        return vanillaManager.getFluids();
    }

    @Override
    public TagGroup<EntityType<?>> getEntityTypes() {
        return vanillaManager.getEntityTypes();
    }
}

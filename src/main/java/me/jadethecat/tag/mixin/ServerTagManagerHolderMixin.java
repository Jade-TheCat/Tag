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

import me.jadethecat.tag.tag.*;
import net.minecraft.tag.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.stream.Collectors;

@Mixin(ServerTagManagerHolder.class)
public class ServerTagManagerHolderMixin {
    @Shadow
    private static volatile TagManager tagManager = new CustomTagManager(TagManager.create(TagGroup.create((Map) BlockTags.getRequiredTags().stream().collect(Collectors.toMap(Tag.Identified::getId, (identified) -> {
        return identified;
    }))), TagGroup.create((Map) ItemTags.getRequiredTags().stream().collect(Collectors.toMap(Tag.Identified::getId, (identified) -> {
        return identified;
    }))), TagGroup.create((Map) FluidTags.getRequiredTags().stream().collect(Collectors.toMap(Tag.Identified::getId, (identified) -> {
        return identified;
    }))), TagGroup.create((Map) EntityTypeTags.getRequiredTags().stream().collect(Collectors.toMap(Tag.Identified::getId, (identified) -> {
        return identified;
    })))), TagGroup.create((Map) BiomeTags.getRequiredTags().stream().collect(Collectors.toMap(Tag.Identified::getId, (identified) -> {
        return identified;
    }))), TagGroup.create((Map) EnchantmentTags.getRequiredTags().stream().collect(Collectors.toMap(Tag.Identified::getId, (identified) -> {
        return identified;
    }))), TagGroup.create((Map) FeatureTags.getRequiredTags().stream().collect(Collectors.toMap(Tag.Identified::getId, (identified) -> {
        return identified;
    }))), TagGroup.create((Map) BlockEntityTypeTags.getRequiredTags().stream().collect(Collectors.toMap(Tag.Identified::getId, (identified) -> {
        return identified;
    }))));
}

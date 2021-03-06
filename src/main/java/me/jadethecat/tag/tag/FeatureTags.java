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

import net.minecraft.tag.RequiredTagList;
import net.minecraft.tag.RequiredTagListRegistry;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroup;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.Feature;

import java.util.List;

public class FeatureTags {
    protected static final RequiredTagList<Feature<?>> REQUIRED_TAGS = RequiredTagListRegistry.register(new Identifier("feature"), tagManager -> {
        if (tagManager instanceof CustomTagManager)
            return ((CustomTagManager) tagManager).getFeatures();
        else
            return TagGroup.createEmpty();
    });

    public static Tag.Identified<Feature<?>> register(String id) {
        return REQUIRED_TAGS.add(id);
    }

    public static TagGroup<Feature<?>> getTagGroup() {
        return REQUIRED_TAGS.getGroup();
    }

    public static List<? extends Tag.Identified<Feature<?>>> getRequiredTags() {
        return REQUIRED_TAGS.getTags();
    }
}

package net.jidb.to.base.level.storage;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public abstract class SavedDataTypeConstructor {

    public static <T extends SavedData> SavedDataType<T> createType(Identifier id, Supplier<T> constructor, Codec<T> codec, @Nullable DataFixTypes dataFixType) {
        return new SavedDataType<>(id, constructor, codec, dataFixType);
    }

}

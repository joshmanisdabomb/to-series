package net.jidb.to.base.level.storage;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Factory for saved data types.
 * The saved data type constructor is Java-side because Kotlin infers dataFixType as non-nullable, even though it should be.
 */
public abstract class SavedDataTypeConstructor {

    /**
     * Creates a saved data type for the given identifier.
     *
     * @param <T> the kind of saved data the type describes.
     * @param id the registry identifier the saved data is stored under.
     * @param constructor supplies a fresh instance when no saved data exists yet.
     * @param codec serialises and deserialises the saved data.
     * @param dataFixType the data fixer category to upgrade old saves with, or null to skip fixing.
     * @return the constructed saved data type.
     */
    public static <T extends SavedData> SavedDataType<T> createType(Identifier id, Supplier<T> constructor, Codec<T> codec, @Nullable DataFixTypes dataFixType) {
        return new SavedDataType<>(id, constructor, codec, dataFixType);
    }

}

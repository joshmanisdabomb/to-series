package net.jidb.to.base.client.pub.library

import com.mojang.serialization.MapCodec
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.color.item.ItemTintSource
import net.minecraft.resources.Identifier

open class ItemTintLibrary(modid: String) : SimpleLibrary<MapCodec<out ItemTintSource>>(modid) {

    operator fun <S : ItemTintSource> invoke(source: MapCodec<S>, id: Identifier? = null) = invoke({
        val source = it()
        ClientServices.platform.models.registerItemTint(id ?: this.id, source);
        { source }
    }, { source })

}

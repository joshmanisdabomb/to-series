package net.jidb.to.base.client.pub.library

import com.mojang.serialization.MapCodec
import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.resources.Identifier

open class SpecialModelLibrary(modid: String) : SimpleLibrary<MapCodec<out SpecialModelRenderer.Unbaked<*>>>(modid) {

    operator fun <R : SpecialModelRenderer.Unbaked<*>> invoke(model: MapCodec<R>, id: Identifier? = null) = invoke({
        val model = it()
        ClientServices.platform.models.registerSpecialModel(id ?: this.id, model);
        { model }
    }, { model })

}

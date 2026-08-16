package net.jidb.to.base.client.pub.library

import net.jidb.to.base.api.library.SimpleLibrary
import net.jidb.to.base.client.service.ClientServices
import net.minecraft.client.color.block.BlockTintSource
import net.minecraft.world.level.block.Block

open class BlockTintLibrary(modid: String) : SimpleLibrary<BlockTintSource>(modid) {

    operator fun <S : BlockTintSource> invoke(source: S, vararg blocks: Block) = invoke({
        val source = it()
        ClientServices.platform.models.registerBlockTint(this.id.namespace, source, *blocks);
        { source }
    }, { source })

}

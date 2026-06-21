package net.jidb.to.stars.entity

import net.jidb.to.base.pub.network.DistantSoundPayload
import net.jidb.to.base.service.Services
import net.jidb.to.stars.ToStarsMod
import net.jidb.to.stars.block.AtomicBombBlock
import net.jidb.to.stars.inventory.menu.AtomicBombMenu
import net.jidb.to.stars.level.NuclearExplosion
import net.minecraft.core.Direction
import net.minecraft.core.NonNullList
import net.minecraft.core.SectionPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.ContainerHelper
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.context.DirectionalPlaceContext
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FallingBlock
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.gamerules.GameRules
import net.minecraft.world.level.portal.TeleportTransition
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.Vec3
import kotlin.math.*

class AtomicBombEntity(type: EntityType<out AtomicBombEntity>, level: Level) : Entity(type, level), TraceableEntity {

    private var stacks = NonNullList.withSize(AtomicBombMenu.allSlots.size, ItemStack.EMPTY)
    private var owner: EntityReference<LivingEntity>? = null

    var ticketTimer: Long = 0
    var forceTickAfterTeleportToDuplicate: Boolean = false

    init {
        blocksBuilding = true
    }

    constructor(level: Level, x: Double, y: Double, z: Double, facing: Direction, stacks: NonNullList<ItemStack>, active: Boolean, owner: LivingEntity? = null) : this(ToStarsMod.entities.atomic_bomb, level) {
        setPos(x, y, z)
        xo = x
        yo = y
        zo = z
        this.stacks = stacks
        this.owner = EntityReference.of(owner)
        entityData[data_timer] = if (active) getFuseTime(getUraniumCount(stacks[2])) else -1
        yRot = facing.toYRot()
        if (active) {
            val r = level.random.nextDouble() * (Math.PI * 2).toFloat()
            setDeltaMovement(-sin(r) * 0.02, 0.2, -cos(r) * 0.02)
        }
        boundingBox = makeBoundingBox(position())
    }

    override fun tick() {
        val prevPos = position()

        applyGravity()
        move(MoverType.SELF, deltaMovement)
        applyEffectsFromBlocks()
        handlePortal()
        if (onGround()) {
            deltaMovement = deltaMovement.multiply(0.7, -0.5, 0.7)
        }

        val level = level()
        if (entityData[data_timer] != 0) {
            entityData[data_timer] -= 1
        }

        if (level is ServerLevel) {
            if (--ticketTimer <= 0 || SectionPos.blockToSectionCoord(position().x) != SectionPos.blockToSectionCoord(prevPos.x) || SectionPos.blockToSectionCoord(position().z) != SectionPos.blockToSectionCoord(prevPos.z)) {
                level.chunkSource.addTicketWithRadius(ToStarsMod.tickets.atomic_bomb, chunkPosition(), 2);
                ticketTimer = ToStarsMod.tickets.atomic_bomb.timeout() - 1
            }

            if (isAlive || forceTickAfterTeleportToDuplicate) {
                if (entityData[data_timer] == 0) {
                    explode(level)
                    discard()
                } else if (entityData[data_timer] >= 0) {
                    updateFluidInteraction()

                    if (entityData[data_timer] % 20 == 0) {
                        Services.platform.networking.sendToPlayersTrackingEntity(this, DistantSoundPayload(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(ToStarsMod.sounds.atomic_bomb_timer), SoundSource.BLOCKS, position().toVector3f(), 96f, 1.6f - (entityData[data_timer] / 1400f).coerceIn(0.0f, 1.0f).pow(0.25f).times(0.8f), 0))
                    }
                } else {
                    if (!onGround()) {
                        val pos = blockPosition()
                        if (entityData[data_timer] < -600 || (entityData[data_timer] < -100 && (pos.y <= level.minY || pos.y > level.maxY))) {
                            drop(level)
                            discard()
                        }
                    } else {
                        var mustPlace = false
                        var canPlace = true
                        for (i in -1..1) {
                            val pos = blockPosition().relative(direction.clockWise, i)
                            val state = level.getBlockState(pos)
                            if (state.`is`(Blocks.MOVING_PISTON)) {
                                mustPlace = false
                                break
                            }
                            val stateDown = level.getBlockState(pos.below())
                            if (!FallingBlock.isFree(stateDown)) mustPlace = true
                            if (!state.canBeReplaced(DirectionalPlaceContext(level, pos, Direction.DOWN, ItemStack.EMPTY, Direction.UP))) canPlace = false
                        }

                        if (mustPlace) {
                            if (canPlace) {
                                for (i in -1..1) {
                                    val pos = blockPosition().relative(direction.clockWise, i)
                                    level.setBlock(pos, ToStarsMod.blocks.atomic_bomb.defaultBlockState()
                                        .setValue(HorizontalDirectionalBlock.FACING, direction)
                                        .setValue(AtomicBombBlock.SEGMENT, when (i) {
                                            -1 -> AtomicBombBlock.AtomicBombSegment.HEAD
                                            1 -> AtomicBombBlock.AtomicBombSegment.TAIL
                                            else -> AtomicBombBlock.AtomicBombSegment.MIDDLE
                                        }), 3)
                                }
                                level.getBlockEntity(blockPosition(), ToStarsMod.blockEntities.atomic_bomb).ifPresent {
                                    it.setItems(stacks)
                                }
                                discard()
                                return
                            }
                            drop(level)
                            discard()
                        }
                    }
                }
            }
        }

        deltaMovement = deltaMovement.scale(0.98)
    }

    private fun explode(level: ServerLevel) {
        val owner = getOwner()
        val explosion = NuclearExplosion(level, owner, Explosion.getDefaultDamageSource(level, getOwner()), position().add(0.0, 0.5, 0.0), getExplosionStrength(getUraniumCount(stacks[2])).toFloat())
        explosion.run()

        if (owner is ServerPlayer) {
            ToStarsMod.advancementTriggers.atomic_bomb.trigger(owner, this)
        }
    }

    private fun drop(level: ServerLevel) {
        if (!level.gameRules.get(GameRules.ENTITY_DROPS)) return

        spawnAtLocation(level, ToStarsMod.blocks.atomic_bomb.asItem())
        for (stack in stacks) {
            spawnAtLocation(level, stack)
        }
    }

    override fun interact(player: Player, hand: InteractionHand, location: Vec3): InteractionResult {
        val level = this.level() as? ServerLevel
        if (level != null && entityData[data_timer] >= 0) {
            val stack = player.getItemInHand(hand)
            if (stack.`is`(Services.platform.tags.getCommonItem("tools/shear")!!)) {
                stack.hurtAndBreak(1, player, hand.asEquipmentSlot())
                level.playSound(null, x, y, z, ToStarsMod.sounds.atomic_bomb_cut, soundSource, 1.0f, this.random.nextFloat() * 0.2f + 0.9f)
                drop(level)
                discard()
            } else if (stack.`is`(Items.FLINT_AND_STEEL) && player.isCreative) {
                stack.hurtAndBreak(1, player, hand.asEquipmentSlot())
                level.playSound(null, x, y, z, SoundEvents.FLINTANDSTEEL_USE, soundSource, 1.0f, this.random.nextFloat() * 0.4f + 0.8f)
                explode(level)
                discard()
            }
        }
        return super.interact(player, hand, location)
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder.define(data_timer, -1)
    }

    override fun hurtServer(level: ServerLevel, source: DamageSource, amount: Float) = false

    override fun readAdditionalSaveData(input: ValueInput) {
        entityData[data_timer] = input.getShortOr("Fuse", -1)
        owner = EntityReference.read(input, "Owner")
        stacks.clear()
        ContainerHelper.loadAllItems(input, stacks)
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        output.putShort("Fuse", entityData[data_timer].toShort())
        EntityReference.store(owner, output, "Owner")
        ContainerHelper.saveAllItems(output, stacks)
    }

    override fun getOwner() = EntityReference.getLivingEntity(this.owner, this.level())

    override fun restoreFrom(entity: Entity) {
        super.restoreFrom(entity)
        if (entity is AtomicBombEntity) {
            owner = entity.owner
        }
    }

    override fun getDefaultGravity() = 0.04

    override fun isPickable() = !this.isRemoved

    override fun isAttackable() = false

    override fun getMovementEmission() = MovementEmission.NONE

    override fun teleport(teleportTransition: TeleportTransition): Entity? {
        val newDim = teleportTransition.newLevel().dimension()
        val oldDim = this.level().dimension()
        val entity = super.teleport(teleportTransition)
        if ((oldDim === Level.END || newDim === Level.END) && oldDim !== newDim) {
            forceTickAfterTeleportToDuplicate = true
        }
        return entity
    }

    override fun makeBoundingBox(position: Vec3) = super.makeBoundingBox(position)
        .inflate(direction.stepZ.absoluteValue.toDouble().times(0.98), 0.0, direction.stepX.absoluteValue.toDouble().times(0.98))

    companion object {
        val data_timer = SynchedEntityData.defineId(AtomicBombEntity::class.java, EntityDataSerializers.INT)

        fun getUraniumCount(uranium: ItemStack) = when (uranium.item) {
            ToStarsMod.blocks.enriched_uranium_block.asItem() -> uranium.count * 9
            ToStarsMod.items.enriched_uranium.asItem() -> uranium.count
            else -> 0
        }

        fun getExplosionStrength(uranium: Int): Int {
            if (uranium <= 0) return 0
            val percent = (uranium - 1) / 44.0
            return 20 + Mth.floor(sqrt(percent) * 120)
        }
        fun getFuseTime(uranium: Int): Int {
            if (uranium <= 0) return 0
            val percent = (uranium - 1) / 44.0
            return 160 + Mth.floor(percent.pow(1.15) * 124) * 10
        }
    }

}

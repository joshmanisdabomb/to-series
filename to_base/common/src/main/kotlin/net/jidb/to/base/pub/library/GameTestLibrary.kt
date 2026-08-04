package net.jidb.to.base.pub.library

import net.jidb.to.base.api.gametest.ToGameTest
import net.jidb.to.base.api.library.AdvancedLibraryBuilder
import net.jidb.to.base.api.library.AdvancedRegistryLibrary
import net.jidb.to.base.api.library.Library
import net.jidb.to.base.service.Services
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.gametest.framework.FunctionGameTestInstance
import net.minecraft.gametest.framework.GameTestEnvironments
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.gametest.framework.GameTestInstance
import net.minecraft.gametest.framework.TestData
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import java.util.function.Consumer

/**
 * [AdvancedRegistryLibrary] implementation that registers the game tests of a mod, and provides access to them in one place.
 *
 * A test is declared as a [net.jidb.to.base.api.gametest.ToGameTest] and comes out as the [Consumer] the game holds it as:
 * ```kotlin
 * object ExampleGameTestLibrary : GameTestLibrary(ExampleMod.modid) {
 *
 *     val example by this { ToGameTest { helper -> helper.succeed() } }
 *
 * }
 * ```
 *
 * That single declaration covers all three of the things a game test is to the game.
 * The function goes into [BuiltInRegistries.TEST_FUNCTION] as the library is built, which is the same path any other registry library takes and so works on either modloader.
 * The test instance is registered into the `test_instance` data pack registry by [bootstrap] on a generation run, and the structure it names is written by [net.jidb.to.base.data.pub.provider.GameTestStructureDataProvider] on the same run.
 *
 * A mod names its library on [net.jidb.to.base.api.mod.ToContentMod.gameTests] to have it built, and on [net.jidb.to.base.client.data.api.mod.ToContentDataMod.gameTests] to have it generated for.
 * Neither the instances nor the structures exist until data generation has been run, so a newly declared test does nothing in game until then.
 *
 * @param modid The mod ID associated with the library.
 * @since 1.0.0
 */
abstract class GameTestLibrary(modid: String) : AdvancedRegistryLibrary<ToGameTest, Consumer<GameTestHelper>>(modid), AdvancedLibraryBuilder<ToGameTest, Consumer<GameTestHelper>> {

    override val registry get() = BuiltInRegistries.TEST_FUNCTION

    /**
     * Every test declared against this library, indexed by the identifier it is registered under, in the order they were declared.
     * This is filled as the library is built, and is what [bootstrap] and the structure generator read back afterwards.
     *
     * @since 1.0.0
     */
    val tests: Map<Identifier, ToGameTest> get() = declared

    /**
     * The backing map of [tests], which only this library writes to.
     *
     * @since 1.0.0
     */
    private val declared = linkedMapOf<Identifier, ToGameTest>()

    /**
     * The structures that have to be generated for this library, i.e. those of every test that did not name one built by hand.
     *
     * @since 1.0.0
     */
    val structures: Map<Identifier, ToGameTest> get() = declared.filterValues { it.structure == null }

    override fun <J : ToGameTest> i(entry: Library<ToGameTest, Consumer<GameTestHelper>>.LibraryEntry<out ToGameTest, out Consumer<GameTestHelper>>, input: () -> J): () -> Consumer<GameTestHelper> {
        val id = getEntryIdentifier(entry)
        //Resolved here rather than in the registered supplier, since the test's scene is needed by the generators whether or not the game ever asks for its function.
        val test = input()
        declared[id] = test
        val function: Consumer<GameTestHelper> = Consumer { helper -> test.run(helper) }
        return Services.register(registry, id) { function }
    }

    /**
     * [LibraryEntry] delegate provider that declares a game test, so that entries can be defined without `::i`:
     * ```kotlin
     * val example by this { ToGameTest { helper -> helper.succeed() } }
     * ```
     *
     * @param initial The test being declared.
     * @return A [LibraryEntry] holding the function the game runs the test through.
     * @since 1.0.0
     */
    operator fun invoke(initial: (Library<ToGameTest, Consumer<GameTestHelper>>.LibraryEntry<ToGameTest, Consumer<GameTestHelper>>) -> ToGameTest) = invoke(::i, initial)

    /**
     * Registers a test instance for every test of this library into the `test_instance` data pack registry, which is what actually makes them run.
     * This is called by the data generator rather than by the game, since a test instance is data pack content and not something a mod holds at runtime.
     *
     * @param context The bootstrap context of the `test_instance` registry being generated.
     * @throws IllegalStateException If the default test environment is missing from the generation run.
     * @since 1.0.0
     */
    fun bootstrap(context: BootstrapContext<GameTestInstance>) {
        val environment = context.lookup(Registries.TEST_ENVIRONMENT).getOrThrow(GameTestEnvironments.DEFAULT_KEY)
        declared.forEach { (id, test) ->
            context.register(ResourceKey.create(Registries.TEST_INSTANCE, id), FunctionGameTestInstance(
                ResourceKey.create(Registries.TEST_FUNCTION, id),
                TestData(
                    environment,
                    test.getStructure(id),
                    test.maxTicks,
                    test.setupTicks,
                    test.required,
                    test.rotation,
                    test.manualOnly,
                    test.maxAttempts,
                    test.requiredSuccesses,
                    test.skyAccess,
                    test.padding,
                )
            ))
        }
    }

}

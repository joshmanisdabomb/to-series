package net.jidb.to.base.test

import io.mockk.mockk
import net.jidb.to.base.ToBaseMod
import net.jidb.to.base.api.platform.Platform
import net.jidb.to.base.api.side.Side
import net.jidb.to.base.service.EnvironmentService as BaseEnvironmentService

/**
 * [net.jidb.to.base.service.EnvironmentService] implementation for the unit tests, loaded through Java's service loader from `src/test/resources/META-INF/services`.
 *
 * [net.jidb.to.base.service.Services] loads every service it holds as soon as any one of them is asked for, so this has to exist even for a test that only wants [TestRegisterService].
 * Nothing loader-specific can work outside the game, so the platform and side are relaxed mocks: a test that reaches into one of their modules gets a stub rather than a crash, and a test that depends on what the stub does should be stubbing it itself.
 *
 * @since 0.8.0
 */
class TestEnvironmentService : BaseEnvironmentService() {

    override val platform: Platform = mockk(relaxed = true)

    override val environment = Environment.DEV

    override val side: Side = mockk(relaxed = true)

    override fun isModLoaded(modId: String) = modId in setOf("minecraft", ToBaseMod.MOD_ID)

    override fun getModVersion(modId: String) = if (isModLoaded(modId)) "0.0.0-test" else null

}

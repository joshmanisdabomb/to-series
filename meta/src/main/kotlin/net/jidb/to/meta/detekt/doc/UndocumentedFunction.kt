package net.jidb.to.meta.detekt.doc

import dev.detekt.api.Config
import dev.detekt.api.Configuration
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.Rule
import dev.detekt.api.config
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtSecondaryConstructor
import org.jetbrains.kotlin.psi.psiUtil.containingClassOrObject

/**
 * Requires KDoc on every function and secondary constructor, for any visibility.
 *
 * This is the Kotlin counterpart of the JavadocMethod rule in Checkstyle.
 * Detekt's comments>UndocumentedPublicFunction only ever looks at public and protected functions and does not exempt overrides.
 * The primary constructor is not a declaration of its own - its parameters are documented on the class, which is what MissingKDocTags checks.
 */
class UndocumentedFunction(config: Config) : Rule(config, "Functions must be documented with KDoc.") {

    @Configuration("a function that overrides a supertype declaration inherits its documentation")
    private val ignoreOverridden: Boolean by config(true)

    @Configuration("a function declared inside another function is not part of any API")
    private val ignoreLocalFunctions: Boolean by config(true)

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        if (ignoreLocalFunctions && function.isLocal) return
        if (KDocSupport.isExempt(function, ignoreOverridden)) return
        if (function.docComment != null) return

        report(
            Finding(
                Entity.atName(function),
                "${function.name} is missing KDoc.",
            ),
        )
    }

    override fun visitSecondaryConstructor(constructor: KtSecondaryConstructor) {
        super.visitSecondaryConstructor(constructor)

        if (constructor.docComment != null) return

        report(
            Finding(
                Entity.from(constructor),
                "The secondary constructor of ${constructor.containingClassOrObject?.name} is missing KDoc.",
            ),
        )
    }
}

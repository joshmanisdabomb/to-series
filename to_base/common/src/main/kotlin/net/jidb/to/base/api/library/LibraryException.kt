package net.jidb.to.base.api.library

/**
 * A custom exception class used for errors specific to a [Library] and other classes in [net.jidb.to.base.api.library].
 *
 * @constructor Creates a new instance of [LibraryException] with the specified message.
 * @param message The detail message, providing further context about the exception.
 * @since 0.0.3
 */
class LibraryException(message: String) : Exception(message)

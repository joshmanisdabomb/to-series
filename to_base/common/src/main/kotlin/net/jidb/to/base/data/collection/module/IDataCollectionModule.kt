package net.jidb.to.base.data.collection.module

import net.jidb.to.base.data.collection.DataCollection
import net.jidb.to.base.data.collection.event.DataCollectionEvent

interface IDataCollectionModule {

    fun process(collection: DataCollection<*>, event: DataCollectionEvent<*, *, *>): EventResult

    enum class EventResult {
        SUCCESS,
        PASS,
        SUCCESS_BLOCK
    }

}
package net.jidb.to.base.api.inventory.data

interface ContainerDataSchemaApplyFunction {

    operator fun <R> invoke(original: R): R

}
package com.company.ourfinances.view.utils.patterns

interface EntityNameResolver<T> {
    fun getEntityNameById(id: Long?): String
}

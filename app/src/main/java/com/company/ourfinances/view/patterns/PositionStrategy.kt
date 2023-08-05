package com.company.ourfinances.view.patterns

interface PositionStrategy <T> {
    fun getPosition(name: String?, itemList: List<T>): Int
}
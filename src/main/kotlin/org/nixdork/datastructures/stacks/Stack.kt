package org.nixdork.datastructures.stacks

@Suppress("UNCHECKED_CAST", "TooManyFunctions")
class Stack<T : Any?> {
    private var data: Array<Any?>

    var size: Int = 0
        private set

    constructor(items: Collection<T>) {
        data = emptyData
        items.forEach { item ->
            push(item)
        }
        size = data.size
    }

    constructor() {
        data = emptyData
        size = data.size
    }

    /**
     * Get the item at the top of the stack
     * without removing it from the stack
     */
    fun peek(): T? = peek(TOP)

    /**
     * Get the item at position `index` of the stack
     * without removing it from the stack
     */
    fun peek(index: Int): T? =
        if (isEmpty()) {
            null
        } else {
            data[normalizeIndex(index)] as T
        }

    /**
     * change the value of the item at the top of the stack
     * without adding a new item to the stack
     */
    fun poke(item: T) = poke(TOP, item)

    /**
     * change the value of the item at `index` position of the stack
     * without adding a new item to the stack
     */
    fun poke(index: Int, item: T) {
        data[normalizeIndex(index)] = item
    }

    /**
     * push new item T to the top of the stack
     */
    fun push(item: T) {
        val newData = nullData(data.size + DECINC)
        data.copyInto(newData, SHIFT_OFFSET, SHIFT_INDEX, data.size)
        data = newData
        data[TOP] = item
        size = data.size
    }

    /**
     * pop item T from the top of the stack
     */
    fun pop(): T? =
        if (isEmpty()) {
            null
        } else {
            val item = data[TOP]
            val newData = nullData(data.size - DECINC)
            data.copyInto(newData, UNSHIFT_OFFSET, UNSHIFT_INDEX, data.size)
            data = newData
            size = data.size
            item as T
        }

    /**
     * return the internal array that represents the stack
     */
    fun dump() = data

    /**
     * clears the stack
     */
    fun clear() {
        data = emptyData
        size = data.size
    }

    /**
     * return true if the stack is empty
     */
    fun isEmpty() = size == 0

    /**
     * return the stack as a string
     */
    override fun toString(): String =
        data.joinToString(", ", "[", "]")

    /**
     * return the stack as a truncated string
     */
    fun toString(truncate: Boolean = true): String =
        if (truncate) {
            data.joinToString(", ", "[", "]", TRUNCATE_AT, "...")
        } else {
            toString()
        }

    private fun normalizeIndex(index: Int): Int =
        when {
            index >= data.size -> data.size - 1
            index < 0 -> TOP
            else -> {
                if (data.isEmpty()) {
                    throw IndexOutOfBoundsException("Index $index doesn't exist in empty Stack")
                } else {
                    index
                }
            }
        }

    internal companion object {
        private const val TOP = 0
        private const val TRUNCATE_AT = 10
        private const val DECINC = 1
        private const val SHIFT_OFFSET = 1
        private const val SHIFT_INDEX = 0
        private const val UNSHIFT_OFFSET = 0
        private const val UNSHIFT_INDEX = 1

        private val emptyData = emptyArray<Any?>()

        internal fun nullData(capacity: Int): Array<Any?> = arrayOfNulls(capacity)
    }
}

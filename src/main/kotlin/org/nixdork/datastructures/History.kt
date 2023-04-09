package org.nixdork.datastructures

import org.nixdork.datastructures.stacks.Stack

@Suppress("UNCHECKED_CAST", "TooManyFunctions")
class History<T : Any?> {
    var fwd: Stack<T?>
    var bwd: Stack<T?>
    var cur: T?

    init {
        fwd = Stack()
        bwd = Stack()
        cur = null
        setSize()
    }

    var size: Int = 0
        private set

    /**
     * Tells if the current pointer can be moved forward in history.
     */
    val canMoveForward: Boolean
        get() = fwd.size > 0

    /**
     * Tells if the current pointer can be moved backward in history.
     */
    val canMoveBackward: Boolean
        get() = bwd.size > 0

    /**
     * Returns the current item in the history.
     */
    var current: T? = null
        get() = cur
        private set

    /**
     * Pushes the state of the current pointer into the backward stack
     * and sets the new current item. If the forward stack has items in
     * it then clear that too and set the size.
     */
    fun visit(item: T): T {
        if (canMoveForward) fwd.clear()
        if (cur != null) bwd.push(cur)
        cur = item
        setSize()
        return cur!!
    }

    /**
     * visits several items
     */
    fun visit(items: Collection<T>): T =
        items.forEach { item -> visit(item) }
            .let { cur!! }

    /**
     * Moves the current pointer one item forward in history
     * @return The new current item
     */
    fun next(): T? {
        if (this.canMoveForward) {
            bwd.push(cur)
            cur = fwd.pop()
        }
        return cur
    }

    /**
     * Moves the current pointer `steps` items forward in history
     * @return The new current item
     */
    fun next(steps: Int): T? =
        (1..steps).forEach {
            next()
        }.let {
            cur
        }

    /**
     * Moves the current pointer one item back in history
     * @return The new current item
     */
    fun previous(): T? {
        if (this.canMoveBackward) {
            fwd.push(cur)
            cur = bwd.pop()
        }
        return cur
    }

    /**
     * Moves the current pointer `steps` items back in history
     * @return The new current item
     */
    fun previous(steps: Int): T? =
        (1..steps).forEach {
            previous()
        }.let {
            cur
        }

    /**
     * Dumps the entire history in chronological order to an Array
     * Chrono order means LIFO
     * @return An array of history items
     */
    fun dump(): Array<Any?> =
        mutableListOf<T>().also { l ->
            if (canMoveForward) l.addAll(fwd.dump().reversed() as Collection<T>)
            if (cur != null) l.add(cur as T)
            if (canMoveBackward) l.addAll(bwd.dump().toList() as Collection<T>)
        }.toTypedArray()

    /**
     * clears the history
     */
    fun clear() {
        fwd.clear()
        bwd.clear()
        cur = null
        size = 0
    }

    /**
     * return true if the history is empty
     */
    fun isEmpty() = size == 0

    /**
     * return the history as a string
     */
    override fun toString(): String =
        bwd.dump().reversedArray().let { b ->
            listOf(
                b.joinToString(ARRAY_SEP, ARRAY_PREFIX, ARRAY_POSTFIX),
                cur,
                fwd.toString(),
            ).joinToString(HIST_SEP, HIST_PREFIX, HIST_POSTFIX)
        }

    private fun setSize() {
        size = bwd.size + fwd.size + if (cur == null) 0 else 1
    }

    companion object {
        private const val ARRAY_SEP = ", "
        private const val ARRAY_PREFIX = "["
        private const val ARRAY_POSTFIX = "]"
        private const val HIST_SEP = ",\n  "
        private const val HIST_PREFIX = "{\n  "
        private const val HIST_POSTFIX = "\n}"
    }
}

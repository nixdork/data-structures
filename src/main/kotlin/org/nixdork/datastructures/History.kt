package org.nixdork.datastructures

import org.nixdork.datastructures.stacks.Stack

@Suppress("UNCHECKED_CAST")
class History<T : Any?> {
    private var fwd: Stack<T?>
    private var bwd: Stack<T?>
    private var cur: T?

    init {
        fwd = Stack()
        bwd = Stack()
        cur = null
    }

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
     * Returns the current item in the history. Pushes the state of the
     * current pointer into the backward stack and sets the new current
     * item. If the forward stack has items in it then clear that too.
     * @param value An item of type T
     */
    var current: T?
        get() = cur
        set(value) {
            if (this.canMoveForward) {
                fwd.clear()
            }

            if (cur != null) {
                bwd.push(cur)
            }

            cur = value
        }

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
     * Dumps the entire history in chronological order to a List of T
     * Chrono order means LIFO
     * @return An ordered list of history items
     */
    fun dump(): List<T> =
        mutableListOf<T>().also { l ->
            if (canMoveForward) l.addAll(fwd.dump().reversed() as Collection<T>)
            if (cur != null) l.add(cur!!)
            if (canMoveBackward) l.addAll(bwd.dump().toList() as Collection<T>)
        }

    /**
     * return the history as a string
     */
    override fun toString(): String =
        bwd.dump().reversedArray().let { b ->
            listOf(
                b.joinToString(", ", "[", "]"),
                cur,
                fwd.toString()
            ).joinToString(", ", "{", "}")
        }
}
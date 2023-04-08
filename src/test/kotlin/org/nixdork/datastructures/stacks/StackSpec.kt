package org.nixdork.datastructures.stacks

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import java.time.OffsetDateTime
import java.time.ZoneOffset

internal data class Test(
    val message: String = "Hello, World!",
    val length: Long = 100L,
    val created: OffsetDateTime = OffsetDateTime.now(),
)

class StackSpec : FunSpec({
    context("constructors") {
        test("empty constructor initializes an empty stack") {
            val stack = Stack<Int>()
            stack.size shouldBe 0
            stack.dump() shouldBe emptyArray()
        }

        test("items constructor initializes stack with items") {
            val items = listOf("one", "two", "three")
            val stack = Stack(items)
            stack.size shouldBe 3
            stack.dump() shouldBe items.reversed()
        }
    }

    context("peeking + poking") {
        val items = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        val stack = Stack(items)

        test("peek returns top item") {
            stack.peek() shouldBe 12
        }

        test("peek with index returns item at index") {
            stack.peek(3) shouldBe 9
        }

        test("poke changes top item") {
            stack.poke(13)
            stack.peek() shouldBe 13
        }

        test("poke with index changes item at index") {
            stack.poke(3,27)
            stack.peek() shouldBe 13
            stack.peek(3) shouldBe 27
        }
    }

    context("pushing + popping") {
        val items = listOf("one", "two", "three", "four", "five")
        val stack = Stack(items)

        test("push adds item to the top of the stack") {
            stack.peek() shouldBe "five"
            stack.push("six")
            stack.peek() shouldBe "six"
        }

        test("pop removes item from the top of the stack") {
            stack.peek() shouldBe "six"
            val item = stack.pop()
            stack.peek() shouldBe "five"
            item shouldBe "six"
        }
    }

    context("dumping + clearing") {
        val items = listOf(0, 1, 2, 3, 4, 5)
        val stack = Stack(items)

        test("dump return internal data structure") {
            val data = stack.dump()
            data.size shouldBe stack.size
            data.shouldBeInstanceOf<Array<Any?>>()
        }

        test("isEmpty should return false when stack has items") {
            stack.isEmpty() shouldBe false
        }

        test("clear should empty the stack") {
            stack.size shouldBe 6
            stack.clear()
            stack.size shouldBe 0
        }

        test("isEmpty should return true when stack has no items") {
            stack.isEmpty() shouldBe true
        }
    }

    context("displaying") {
        val items = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        val stack = Stack(items)

        test("display the stack as a string") {
            stack.toString() shouldBe "[12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0]"
        }

        test("display the stack as a truncated string") {
            stack.toString(true) shouldBe "[12, 11, 10, 9, 8, 7, 6, 5, 4, 3, ...]"
        }
    }

    context("custom type") {
        val items = emptyList<Test>().toMutableList()
        (1..5).forEach { i ->
            items.add(i - 1, Test(length = i.toLong()))
        }
        val stack = Stack(items)

        test("execute most actions") {
            val item = Test(
                "wie gehtz, Welt!",
                100L,
                OffsetDateTime.of(
                    1990,
                    1,
                    1,
                    1,
                    1,
                    1,
                    1,
                    ZoneOffset.UTC
                )
            )

            stack.peek() shouldNotBeSameInstanceAs item
            stack.push(item)
            stack.peek() shouldBeSameInstanceAs item
            stack.poke(3, item)
            stack.peek(3) shouldBeSameInstanceAs stack.peek()
            val popped = stack.pop()
            stack.peek() shouldNotBeSameInstanceAs item
            stack.peek(2) shouldBeSameInstanceAs item
            popped shouldBeSameInstanceAs item
        }
    }

    context("bounds + overflow") {
        val items = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        val stack = Stack(items)

        test("negative index should return top of stack") {
            stack.peek(-1) shouldBe stack.peek()
            stack.poke(-1, 10)
            stack.peek(-1) shouldBe 10
        }

        test("index larger than size should return bottom of stack") {
            stack.peek(stack.size + 10) shouldBe stack.peek(stack.size)
            stack.poke(stack.size + 10, 20)
            stack.peek(stack.size) shouldBe 20
        }

        test("index into an empty stack should throw") {
            stack.clear()
            stack.isEmpty() shouldBe true
            stack.peek() shouldBe null
            shouldThrow<IndexOutOfBoundsException> { stack.poke(10) }
            stack.isEmpty() shouldBe true
        }
    }
})

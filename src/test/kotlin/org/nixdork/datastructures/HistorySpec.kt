package org.nixdork.datastructures

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class HistorySpec : FunSpec({
    context("visit + move") {
        val history = History<String>()

        test("visit should add items to history") {
            history.visit("https://github.com/")
            history.visit("https://google.com/")
            history.visit("https://microsoft.com/")
            history.visit("https://news.ycombinator.com/")
            history.visit("https://ibm.com/")

            history.current shouldBe "https://ibm.com/"
            history.canMoveBackward shouldBe true
            history.canMoveForward shouldBe false
            history.size shouldBe 5
        }

        test("previous should move backwards in history") {
            history.current shouldBe "https://ibm.com/"
            history.previous(2)
            history.current shouldBe "https://microsoft.com/"
            history.canMoveBackward shouldBe true
            history.canMoveForward shouldBe true
            history.size shouldBe 5
        }

        test("next should move forward in history") {
            history.current shouldBe "https://microsoft.com/"
            history.next(2)
            history.current shouldBe "https://ibm.com/"
            history.canMoveBackward shouldBe true
            history.canMoveForward shouldBe false
            history.size shouldBe 5
        }

        test("visiting new item while fws stack is not empty will clear fwd stack") {
            history.current shouldBe "https://ibm.com/"
            history.previous()
            history.current shouldBe "https://news.ycombinator.com/"
            history.visit("https://www.wikipedia.org/")
            history.current shouldBe "https://www.wikipedia.org/"
            history.canMoveBackward shouldBe true
            history.canMoveForward shouldBe false
            history.size shouldBe 5
        }
    }

    context("dump + clear + isEmpty") {
        val history = History<Int>()
        history.visit(listOf(1, 2, 3, 4, 5, 6))

        test("dump returns history as an array") {
            history.previous()
            history.visit(7)
            val dumped = history.dump<Int>()
            dumped shouldBe listOf(1, 2, 3, 4, 5, 7).reversed().toTypedArray()
            dumped.shouldBeInstanceOf<Array<Int>>()
        }

        test("clear should empty the history") {
            history.clear()
            history.isEmpty() shouldBe true
        }
    }
})

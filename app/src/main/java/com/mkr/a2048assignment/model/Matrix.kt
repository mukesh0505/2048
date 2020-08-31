package com.mkr.a2048assignment.model

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class Matrix {
    private var numbers: Array<IntArray>
    private var random: Random
    private var emptySpots: MutableList<Spot>
    private var mergeSpots: MutableSet<Spot>
    private var newSpot: Spot

    constructor() {
        random = Random()
        numbers = Array(N) { IntArray(
            N
        ) }
        for (i in 0 until N) {
            for (j in 0 until N) {
                numbers[i][j] =
                    EMPTY
            }
        }
        emptySpots = ArrayList()
        mergeSpots = HashSet()
        newSpot = Spot(0, 0)

        for (i in 0 until 2) {
            spawn(2)
        }
    }

    fun generateNew() {
        val v: Int = random.nextInt(2)
        if (v == 0) {
            spawn(2)
        } else {
            spawn(4)
        }
    }

    constructor(copy: Matrix) {
        random = copy.random
        numbers = Array(N) { IntArray(
            N
        ) }
        for (r in 0 until N) {
            for (c in 0 until N) {
                numbers[r][c] = copy.numbers[r][c]
            }
        }
        emptySpots = ArrayList()
        for (spot in copy.emptySpots) {
            emptySpots.add(spot)
        }
        mergeSpots = HashSet()
        for (spot in copy.mergeSpots) {
            mergeSpots.add(spot)
        }
        newSpot = Spot(copy.newSpot.r, copy.newSpot.c)
    }

    fun getSpot(r: Int, c: Int): Int {
        return numbers[r][c]
    }

    private fun spawn(n: Int) {
        collectEmptySpots()
        if (emptySpots.isNotEmpty()) {
            val i: Int = random.nextInt(emptySpots.size)
            newSpot = emptySpots[i]
            numbers[newSpot.r][newSpot.c] = n
        }
    }

    private fun collectEmptySpots() {
        emptySpots.clear()
        for (x in 0 until N) {
            for (y in 0 until N) {
                if (numbers[x][y] == EMPTY) {
                    emptySpots.add(Spot(x, y))
                }
            }
        }
    }

    val isStuck: Boolean
        get() {
            val copy = Matrix(this)
            var score = 0
            for (d in Direction.values()) {
                score += copy.swipe(d)
            }
            copy.collectEmptySpots()
            return score == 0 && copy.emptySpots.isEmpty()
        }

    private fun mergeLeft(row: Int): Int {
        var idx = 0
        var score = 0
        var merged: Boolean
        for (i in 0 until N) {
            if (numbers[row][i] != EMPTY) {
                var farthest = -1
                for (j in i + 1 until N) {
                    if (numbers[row][j] != EMPTY) {
                        farthest = j
                        break
                    }
                }
                merged = false
                if (farthest != -1) {
                    if (numbers[row][i] == numbers[row][farthest]) {
                        numbers[row][i] += numbers[row][farthest]
                        score += numbers[row][i]
                        numbers[row][farthest] = EMPTY
                        merged = true
                    }
                }
                numbers[row][idx] = numbers[row][i]
                if (merged) {
                    mergeSpots.add(Spot(row, idx))
                }
                if (idx != i) {
                    numbers[row][i] = EMPTY
                }
                idx++
            }
        }
        return score
    }

    private fun mergeRight(row: Int): Int {
        var idx = N - 1
        var score = 0
        var merged = false
        for (i in N - 1 downTo 0) {
            if (numbers[row][i] != EMPTY) {
                var farthest = -1
                for (j in i - 1 downTo 0) {
                    if (numbers[row][j] != EMPTY) {
                        farthest = j
                        break
                    }
                }
                merged = false
                if (farthest != -1) {
                    if (numbers[row][i] == numbers[row][farthest]) {
                        numbers[row][i] += numbers[row][farthest]
                        score += numbers[row][i]
                        numbers[row][farthest] = EMPTY
                        merged = true
                    }
                }
                numbers[row][idx] = numbers[row][i]
                if (merged) {
                    mergeSpots.add(Spot(row, idx))
                }
                if (idx != i) {
                    numbers[row][i] = EMPTY
                }
                idx--
            }
        }
        return score
    }

    private fun mergeUp(column: Int): Int {
        var idx = 0
        var score = 0
        var merged = false
        for (i in 0 until N) {
            if (numbers[i][column] != EMPTY) {
                var farthest = -1
                for (j in i + 1 until N) {
                    if (numbers[j][column] != EMPTY) {
                        farthest = j
                        break
                    }
                }
                merged = false
                if (farthest != -1) {
                    if (numbers[i][column] == numbers[farthest][column]) {
                        numbers[i][column] += numbers[farthest][column]
                        score = numbers[i][column]
                        numbers[farthest][column] =
                            EMPTY
                        merged = true
                    }
                }
                numbers[idx][column] = numbers[i][column]
                if (merged) {
                    mergeSpots.add(Spot(idx, column))
                }
                if (idx != i) {
                    numbers[i][column] = EMPTY
                }
                idx++
            }
        }
        return score
    }

    private fun mergeDown(column: Int): Int {
        var idx = N - 1
        var score = 0
        var merged: Boolean
        for (i in N - 1 downTo 0) {
            if (numbers[i][column] != EMPTY) {
                var farthest = -1
                for (j in i - 1 downTo 0) {
                    if (numbers[j][column] != EMPTY) {
                        farthest = j
                        break
                    }
                }
                merged = false
                if (farthest != -1) {
                    if (numbers[i][column] == numbers[farthest][column]) {
                        numbers[i][column] += numbers[farthest][column]
                        score = numbers[i][column]
                        numbers[farthest][column] = EMPTY
                        merged = true
                    }
                }
                numbers[idx][column] = numbers[i][column]
                if (merged) {
                    mergeSpots.add(Spot(idx, column))
                }
                if (idx != i) {
                    numbers[i][column] = EMPTY
                }
                idx--
            }
        }
        return score
    }

    fun isMergeSpot(r: Int, c: Int): Boolean {
        return mergeSpots.contains(Spot(r, c))
    }

    fun swipe(dir: Direction): Int {
        var totalScore = 0
        mergeSpots.clear()
        if (dir === Direction.DOWN || dir === Direction.UP) {
            (0 until N).forEach {
                totalScore += if (dir === Direction.DOWN) mergeDown(it) else mergeUp(it)
            }
        } else {
            (0 until N).forEach {
                totalScore += if (dir === Direction.LEFT) mergeLeft(it) else mergeRight(it)
            }
        }
        return totalScore
    }

    data class Spot(val r: Int, val c: Int)

    companion object {
        const val EMPTY = 0
        const val N = 4
    }
}
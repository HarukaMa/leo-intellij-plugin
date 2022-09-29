/*
 * Copyright (c) 2022 Haruka Ma
 * This file is part of Leo / Aleo IntelliJ plugin.
 *
 * Leo / Aleo IntelliJ plugin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Leo / Aleo IntelliJ plugin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Leo / Aleo IntelliJ plugin. If not, see <https://www.gnu.org/licenses/>.
 */
package im.mrx.leolanguage.leo

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.openapi.util.Key
import com.intellij.util.containers.Stack

@Suppress("UNUSED_PARAMETER")
object LeoParserUtil : GeneratedParserUtilBase() {
    private var ALLOW_CIRCUIT_EXPRESSION: Key<Boolean> = Key("LeoParserUtil.ALLOW_CIRCUIT_EXPRESSION")
    private var ALLOW_EXTERNAL_RECORD: Key<Boolean> = Key("LeoParserUtil.ALLOW_EXTERNAL_RECORD")

    private var DATA_STACK: Key<MutableMap<Key<*>, Stack<Boolean>>> = Key("LeoParserUtil.DATA_STACK")

    @JvmStatic
    fun checkCircuitExpressionAllowed(builder: PsiBuilder, level: Int): Boolean {
        return builder.getUserData(ALLOW_CIRCUIT_EXPRESSION) ?: true
    }

    @JvmStatic
    fun setAllowCircuitExpression(builder: PsiBuilder, level: Int, allow: Int): Boolean {
        builder.pushValue(ALLOW_CIRCUIT_EXPRESSION, allow == 1)
        return true
    }

    @JvmStatic
    fun resetAllowCircuitExpression(builder: PsiBuilder, level: Int): Boolean {
        builder.popValue(ALLOW_CIRCUIT_EXPRESSION)
        return true
    }

    @JvmStatic
    fun checkExternalRecordAllowed(builder: PsiBuilder, level: Int): Boolean {
        return builder.getUserData(ALLOW_EXTERNAL_RECORD) ?: false
    }

    @JvmStatic
    fun setAllowExternalRecord(builder: PsiBuilder, level: Int, allow: Int): Boolean {
        builder.pushValue(ALLOW_EXTERNAL_RECORD, allow == 1)
        return true
    }

    @JvmStatic
    fun resetAllowExternalRecord(builder: PsiBuilder, level: Int): Boolean {
        builder.popValue(ALLOW_EXTERNAL_RECORD)
        return true
    }

    private var PsiBuilder.userDataStacks: MutableMap<Key<*>, Stack<Boolean>>
        get() = getUserData(DATA_STACK) ?: mutableMapOf()
        set(value) = putUserData(DATA_STACK, value)

    private fun PsiBuilder.pushValue(key: Key<Boolean>, value: Boolean) {
        val stacks = userDataStacks
        val stack = stacks[key] ?: Stack<Boolean>()
        stack.push(value)
        stacks[key] = stack
        userDataStacks = stacks
        putUserData(key, value)
    }

    private fun PsiBuilder.popValue(key: Key<Boolean>) {
        val stacks = userDataStacks
        val stack = stacks[key] ?: return
        putUserData(key, stack.pop())
        userDataStacks = stacks
    }


}

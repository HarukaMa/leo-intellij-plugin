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
    private var ALLOW_STRUCT_EXPRESSION: Key<Boolean> = Key("LeoParserUtil.ALLOW_STRUCT_EXPRESSION")

    private var DATA_STACK: Key<MutableMap<Key<*>, Stack<Boolean>>> = Key("LeoParserUtil.DATA_STACK")

    @JvmStatic
    fun checkStructExpressionAllowed(builder: PsiBuilder, level: Int): Boolean {
        return builder.getUserData(ALLOW_STRUCT_EXPRESSION) ?: true
    }

    @JvmStatic
    fun setAllowStructExpression(builder: PsiBuilder, level: Int, allow: Int): Boolean {
        builder.pushValue(ALLOW_STRUCT_EXPRESSION, allow == 1)
        return true
    }

    @JvmStatic
    fun resetAllowStructExpression(builder: PsiBuilder, level: Int): Boolean {
        builder.popValue(ALLOW_STRUCT_EXPRESSION)
        return true
    }

    private var PsiBuilder.userDataStacks: MutableMap<Key<*>, Stack<Boolean>>
        get() = getUserData(DATA_STACK) ?: mutableMapOf()
        set(value) = putUserData(DATA_STACK, value)

    private fun PsiBuilder.pushValue(key: Key<Boolean>, value: Boolean) {
        val stacks = userDataStacks
        val stack = stacks[key] ?: Stack<Boolean>()
        stack.push(getUserData(ALLOW_STRUCT_EXPRESSION) ?: true)
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

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

object LeoParserUtil : GeneratedParserUtilBase() {
    private var ALLOW_CIRCUIT_EXPRESSION: Key<Boolean> = Key("LeoParserUtil.ALLOW_CIRCUIT_EXPRESSION")

    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    fun checkCircuitExpressionAllowed(builder: PsiBuilder, level: Int): Boolean {
        return builder.getUserData(ALLOW_CIRCUIT_EXPRESSION) ?: true
    }

    @JvmStatic
    fun setAllowCircuitExpression(builder: PsiBuilder, level: Int, allow: Int, parser: Parser): Boolean {
        val old = builder.getUserData(ALLOW_CIRCUIT_EXPRESSION)
        builder.putUserData(ALLOW_CIRCUIT_EXPRESSION, allow == 1)
        val result = parser.parse(builder, level)
        builder.putUserData(ALLOW_CIRCUIT_EXPRESSION, old)
        return result
    }
}
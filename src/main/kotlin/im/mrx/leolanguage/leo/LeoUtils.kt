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

import com.intellij.psi.util.elementType
import im.mrx.leolanguage.leo.psi.LeoFunctionDeclaration
import im.mrx.leolanguage.leo.psi.LeoTypedElement
import im.mrx.leolanguage.leo.psi.LeoTypes

object LeoUtils {
    fun functionIsProgram(function: LeoFunctionDeclaration): Boolean =
        function.annotationList.any { it.identifier.text == "program" }

    fun functionParameterListToString(function: LeoFunctionDeclaration): String {
        return function.functionParameters?.functionParameterList?.joinToString(", ") {
            val doc = "${it.name}: ${typeToString(it)}"
            if (functionIsProgram(function)) {
                var visibility = it.identifier.prevSibling
                while (visibility != null) {
                    if (visibility.elementType == LeoTypes.KEYWORD) {
                        break
                    }
                    visibility = visibility.prevSibling
                }
                return@joinToString "${visibility?.text ?: "private"} $doc"
            }
            return@joinToString doc
        } ?: ""
    }

    fun functionToDocString(function: LeoFunctionDeclaration): String =
        (if (functionIsProgram(function)) "@program<br>" else "") + "function ${function.name}(${
            functionParameterListToString(
                function
            )
        }) -> ${typeToString(function)}"


    fun <T : LeoTypedElement> typeToString(function: T): String {
        return function.namedType?.text ?: function.tupleType?.text ?: "?"
    }
}
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

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import im.mrx.leolanguage.leo.psi.*

object LeoUtils {
    fun functionIsTransition(function: LeoFunctionLikeDeclaration): Boolean =
        function is LeoTransitionDeclaration

    fun functionParameterListToTypeString(function: LeoFunctionLikeDeclaration): String {
        return function.functionParameterList.functionParameterList.joinToString(", ") {
            typeToString(it) ?: "?"
        }
    }

    fun functionParameterListToString(function: LeoFunctionLikeDeclaration): String {
        return function.functionParameterList.functionParameterList.joinToString(", ") {
            val doc = "${it.name}${typeToStringWithColon(it)}"
            if (functionIsTransition(function)) {
                return@joinToString "${getParameterVisibility(it)} $doc"
            }
            return@joinToString doc
        }
    }

    fun getParameterVisibility(parameter: LeoFunctionParameter): String {
        var visibility = parameter.identifier.prevSibling
        while (visibility != null) {
            if (visibility.elementType == LeoTypes.KEYWORD) {
                break
            }
            visibility = visibility.prevSibling
        }
        return visibility?.text ?: "private"
    }

    fun functionToDocString(function: LeoFunctionLikeDeclaration): String =
        "${(if (functionIsTransition(function)) "transition" else "function")} ${function.name}(${
            functionParameterListToString(
                function
            )
        })${typeToStringWithArrow(function)}"

    private fun namedTypeToString(type: LeoNamedType): String {
        type.locator?.let {
            return it.identifier?.text ?: "?"
        }
        return type.text
    }

    private fun tupleTypeToString(type: LeoTupleType): String {
        return "(" + type.children.joinToString(", ") {
            when (it) {
                is LeoTupleType -> tupleTypeToString(it)
                is LeoNamedType -> namedTypeToString(it)
                else -> ""
            }
        } + ")"
    }

    fun <T : LeoTypedElement> typeToString(element: T): String? {
        element.namedType?.let {
            return namedTypeToString(it)
        }
        element.tupleType?.let {
            return tupleTypeToString(it)
        }
        return null
    }

    fun <T : LeoTypedElement> typeToStringWithArrow(element: T): String {
        return typeToString(element)?.let { " -> $it" } ?: ""
    }

    fun <T : LeoTypedElement> typeToStringWithColon(element: T): String {
        return typeToString(element)?.let { ": $it" } ?: ""
    }

    fun <P : PsiElement, T : Class<P>> getProgramChildrenOfTypeInFile(file: PsiFile, type: T): List<P> {
        PsiTreeUtil.getChildOfType(file, LeoProgramDeclaration::class.java)?.let {
            return PsiTreeUtil.getChildrenOfTypeAsList(it.programBlock, type)
        } ?: return emptyList()
    }
}
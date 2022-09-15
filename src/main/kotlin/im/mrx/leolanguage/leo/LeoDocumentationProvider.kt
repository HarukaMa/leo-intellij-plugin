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

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup.DEFINITION_END
import com.intellij.lang.documentation.DocumentationMarkup.DEFINITION_START
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import im.mrx.leolanguage.leo.psi.*

class LeoDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        println(element.elementType)
        return when (element) {
            is LeoFunctionParameter -> generateDoc(element)
            is LeoCircuitComponentDeclaration -> generateDoc(element)
            is LeoVariableDeclaration -> generateDoc(element)
            is LeoCircuitDeclaration -> generateDoc(element)
            is LeoFunctionDeclaration -> generateDoc(element)
            else -> null
        }
    }

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
        return generateDoc(element, originalElement)
    }

    private fun generateDoc(element: LeoFunctionParameter): String {
        val doc = "${element.name}: ${element.namedType?.text ?: element.tupleType?.text ?: "?"}"
        val declaration = PsiTreeUtil.getParentOfType(element, LeoFunctionDeclaration::class.java)
        if (declaration?.annotationList?.any { it.identifier.text == "program" } == true) {
            var visibility = element.identifier.prevSibling
            while (visibility != null) {
                if (visibility.elementType == LeoTypes.KEYWORD) {
                    break
                }
                visibility = visibility.prevSibling
            }
            return generateMarkedUpDoc("${visibility?.text ?: "private"} $doc")
        }
        return generateMarkedUpDoc(doc)
    }

    private fun generateDoc(element: LeoCircuitDeclaration): String {
        return generateMarkedUpDoc("circuit ${element.name}")
    }

    private fun generateDoc(element: LeoCircuitComponentDeclaration): String {
        return generateMarkedUpDoc("${element.name}: ${element.namedType?.text ?: element.tupleType?.text ?: "?"}")
    }

    private fun generateDoc(element: LeoVariableDeclaration): String {
        return generateMarkedUpDoc("${element.name}: ${element.namedType?.text ?: element.tupleType?.text ?: "?"}")
    }

    private fun generateDoc(element: LeoFunctionDeclaration): String {
        val parameters =
            element.functionParameters?.functionParameterList?.joinToString(", ") { "${it.name}: ${it.namedType?.text ?: it.tupleType?.text ?: "?"}" }
                ?: ""
        val isProgram = element.annotationList.any { it.identifier.text == "program" }
        return generateMarkedUpDoc((if (isProgram) "@program<br>" else "") + "function ${element.name}($parameters) -> ${element.namedType?.text ?: element.tupleType?.text ?: "?"}")
    }

    private fun generateMarkedUpDoc(definition: String): String {
        return DEFINITION_START + definition + DEFINITION_END
    }


}
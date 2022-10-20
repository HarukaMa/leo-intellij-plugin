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

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.elementType
import im.mrx.leolanguage.leo.psi.LeoNamedElement
import im.mrx.leolanguage.leo.psi.LeoTypes.*

class LeoFindUsagesProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner {
        return DefaultWordsScanner(
            LeoLexerAdapter(),
            TokenSet.create(IDENTIFIER),
            TokenSet.create(BLOCK_COMMENT, LINE_COMMENT),
            TokenSet.create(
                ADDRESS_LITERAL, BOOLEAN_LITERAL, STRING_LITERAL, NUMERIC_LITERAL
            )
        )
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is LeoNamedElement
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return null
    }

    override fun getType(element: PsiElement): String {
        return when (element.elementType) {
            RECORD_DECLARATION -> "record"
            STRUCT_DECLARATION -> "struct"
            VARIABLE_DECLARATION -> "variable"
            FUNCTION_DECLARATION -> "function"
            TRANSITION_DECLARATION -> "transition"
            STRUCT_COMPONENT_DECLARATION -> "struct component"
            FUNCTION_PARAMETER -> "parameter"
            else -> "unknown type"
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return (element as? LeoNamedElement)?.name ?: ""
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return "TODO NodeText"
    }
}
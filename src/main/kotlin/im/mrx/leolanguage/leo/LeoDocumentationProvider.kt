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
import com.intellij.lang.documentation.DocumentationMarkup.*
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import im.mrx.leolanguage.leo.psi.*

class LeoDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        println(element.elementType)
        return when (element) {
            is LeoFunctionParameter -> generateDoc(element)
            is LeoStructComponentDeclaration -> generateDoc(element)
            is LeoVariableDeclaration -> generateDoc(element)
            is LeoStructDeclaration -> generateDoc(element)
            is LeoRecordDeclaration -> generateDoc(element)
            is LeoMappingDeclaration -> generateDoc(element)
            is LeoFunctionLikeDeclaration -> generateDoc(element)
            is LeoFinalizer -> generateDoc(element)
            else -> null
        }
    }

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
        return generateDoc(element, originalElement)
    }

    private fun generateDoc(element: LeoFunctionParameter): String {
        val doc = "${element.name}${LeoUtils.typeToStringWithColon(element)}"
        val declaration = PsiTreeUtil.getParentOfType(element, LeoFunctionLikeDeclaration::class.java)
        if (declaration?.annotationList?.any { it.identifier.text == "program" } == true) {
            var visibility = element.identifier.prevSibling
            while (visibility != null) {
                if (visibility.elementType == LeoTypes.KEYWORD) {
                    break
                }
                visibility = visibility.prevSibling
            }
            return generateMarkedUpDoc("${visibility?.text ?: "private"} $doc", element)
        }
        return generateMarkedUpDoc(doc, element)
    }

    private fun generateDoc(element: LeoStructDeclaration): String {
        return generateMarkedUpDoc("struct ${element.name}", element)
    }

    private fun generateDoc(element: LeoRecordDeclaration): String {
        return generateMarkedUpDoc("record ${element.name}", element)
    }

    private fun generateDoc(element: LeoMappingDeclaration): String {
        return generateMarkedUpDoc(element.text, element)
    }

    private fun generateDoc(element: LeoStructComponentDeclaration): String {
        return generateMarkedUpDoc("${element.name}${LeoUtils.typeToStringWithColon(element)}", element)
    }

    private fun generateDoc(element: LeoVariableDeclaration): String {
        return generateMarkedUpDoc("${element.name}${LeoUtils.typeToStringWithColon(element)}", element)
    }

    private fun generateDoc(element: LeoFunctionLikeDeclaration): String {
        return generateMarkedUpDoc(LeoUtils.functionToDocString(element), element)
    }

    private fun generateDoc(element: LeoFinalizer): String {
        return generateMarkedUpDoc(
            "finalize ${element.name}(${
                element.functionParameterList?.functionParameterList?.joinToString(", ") {
                    val doc = "${it.name}${LeoUtils.typeToStringWithColon(it)}"
                    return@joinToString "${LeoUtils.getParameterVisibility(it)} $doc"
                } ?: ""
            })${LeoUtils.typeToStringWithArrow(element)}",
            element
        )
    }

    private fun generateMarkedUpDoc(definition: String, element: PsiElement): String {
        val highlightedDefinition = HtmlSyntaxInfoUtil.getHtmlContent(
            element.containingFile,
            definition,
            null,
            EditorColorsManager.getInstance().schemeForCurrentUITheme,
            0,
            definition.length,
        )?.replace(Regex.fromLiteral("&lt;"), "<")?.replace(Regex.fromLiteral("&gt;"), ">")
        val comments = ArrayList<String>()
        var el = element
        while (el.prevSibling != null) {
            el = el.prevSibling
            if (el is PsiWhiteSpace) {
                if (el.text.count { it == '\n' } > 1) {
                    break
                }
                continue
            }
            if (el.elementType == LeoTypes.BLOCK_COMMENT) {
                comments.add(
                    0,
                    el.text
                        .replace(Regex("""^\s*/\*\s*"""), "")
                        .replace(Regex("""^\s*\*\s*"""), "")
                        .replace(Regex("""\s*$"""), "")
                        .replace(Regex("""\s*\*/$"""), "")
                )
                break
            } else if (el.elementType == LeoTypes.LINE_COMMENT) {
                comments.add(0, el.text.replace(Regex("""^\s*//\s*"""), ""))
                continue
            } else {
                break
            }
        }
        var doc = DEFINITION_START + highlightedDefinition + DEFINITION_END
        if (comments.isNotEmpty()) {
            doc += CONTENT_START + comments.joinToString("<br>") + CONTENT_END
        }
        return doc
    }

}
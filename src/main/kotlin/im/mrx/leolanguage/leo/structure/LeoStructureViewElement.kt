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

package im.mrx.leolanguage.leo.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.util.PsiTreeUtil
import im.mrx.leolanguage.aleo.AleoIcons
import im.mrx.leolanguage.leo.LeoUtils
import im.mrx.leolanguage.leo.psi.*
import javax.swing.Icon

class LeoStructureViewElement(private val element: NavigatablePsiElement) : StructureViewTreeElement {
    override fun getPresentation(): ItemPresentation {
        return element.presentation ?: object : ItemPresentation {
            override fun getPresentableText(): String? {
                if (element is LeoFunctionLikeDeclaration) {
                    return "${element.name}(${LeoUtils.functionParameterListToTypeString(element)})${
                        LeoUtils.typeToStringWithArrow(
                            element
                        )
                    }"
                }
                if (element is LeoStructComponentDeclaration) {
                    return "${element.name}${LeoUtils.typeToStringWithColon(element)}"
                }
                if (element is LeoMappingDeclaration) {
                    return "${element.name}: ${element.mappingTypeList.joinToString(" => ") { LeoUtils.typeToString(it) ?: "?" }}"
                }
                if (element is LeoProgramDeclaration) {
                    return element.programId?.text ?: "Program"
                }
                return element.name
            }

            override fun getIcon(unused: Boolean): Icon? {
                return when (element) {
                    is LeoRecordDeclaration -> AleoIcons.RECORD
                    is LeoStructDeclaration -> AleoIcons.STRUCT
                    is LeoFunctionLikeDeclaration -> {
                        if (LeoUtils.functionIsTransition(element)) {
                            return AleoIcons.TRANSITION
                        }
                        return AleoIcons.FUNCTION
                    }

                    is LeoStructComponentDeclaration -> AleoIcons.STRUCT_COMPONENT
                    is LeoMappingDeclaration -> AleoIcons.MAPPING
                    else -> null
                }
            }

            override fun getLocationString(): String? {
                if (element is LeoProgramDeclaration) {
                    return element.containingFile.presentation?.locationString
                }
                return null
            }
        }
    }

    override fun getChildren(): Array<TreeElement> {
        val elements = ArrayList<TreeElement>()
        if (element is LeoProgramDeclaration) {
            PsiTreeUtil.getChildrenOfType(element.programBlock, NavigatablePsiElement::class.java)?.forEach {
                elements.add(LeoStructureViewElement(it as NavigatablePsiElement))
            }
        }
        if (element is LeoStructLikeDeclaration) {
            PsiTreeUtil.getChildOfType(element, LeoStructComponentDeclarations::class.java)?.let {
                it.structComponentDeclarationList.forEach { declaration ->
                    elements.add(LeoStructureViewElement(declaration as NavigatablePsiElement))
                }
            }
        }
        return elements.toTypedArray()
    }

    override fun navigate(requestFocus: Boolean) {
        return element.navigate(requestFocus)
    }

    override fun canNavigate(): Boolean {
        return element.canNavigate()
    }

    override fun canNavigateToSource(): Boolean {
        return element.canNavigateToSource()
    }

    override fun getValue(): Any {
        return element
    }
}
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
import im.mrx.leolanguage.leo.psi.*
import javax.swing.Icon

class LeoStructureViewElement(private val element: NavigatablePsiElement) : StructureViewTreeElement {
    override fun getPresentation(): ItemPresentation {
        return element.presentation ?: object : ItemPresentation {
            override fun getPresentableText(): String? {
                return when (element) {
                    is LeoRecordDeclaration -> "record ${element.name}"
                    is LeoCircuitDeclaration -> "circuit ${element.name}"
                    is LeoFunctionDeclaration -> "function ${element.name}"
                    else -> element.name
                }
            }

            override fun getIcon(unused: Boolean): Icon? = null
        }
    }

    override fun getChildren(): Array<TreeElement> {
        val elements = ArrayList<TreeElement>()
        if (element is LeoFile) {
            PsiTreeUtil.getChildrenOfType(element, LeoDeclaration::class.java)?.forEach {
                if (it.firstChild is NavigatablePsiElement) {
                    elements.add(LeoStructureViewElement(it.firstChild as NavigatablePsiElement))
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
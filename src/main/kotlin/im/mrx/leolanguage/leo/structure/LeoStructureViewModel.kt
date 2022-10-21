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

import com.intellij.ide.structureView.StructureViewModel.ElementInfoProvider
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import im.mrx.leolanguage.leo.psi.LeoProgramDeclaration
import im.mrx.leolanguage.leo.psi.LeoStructLikeDeclaration

class LeoStructureViewModel(editor: Editor?, private val file: PsiFile) :
    TextEditorBasedStructureViewModel(editor, file), ElementInfoProvider {
    override fun getRoot(): StructureViewTreeElement {
        return LeoStructureViewElement(PsiTreeUtil.getChildOfType(file, LeoProgramDeclaration::class.java) ?: file)
    }

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?): Boolean =
        element?.value is PsiFile || element?.value is LeoProgramDeclaration

    override fun isAlwaysLeaf(element: StructureViewTreeElement?): Boolean {
        val value = element?.value
        return value !is PsiFile && value !is LeoProgramDeclaration && value !is LeoStructLikeDeclaration
    }
}
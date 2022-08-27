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

package im.mrx.leolanguage.leo.stub

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import im.mrx.leolanguage.leo.psi.LeoNamedElement
import im.mrx.leolanguage.leo.psi.LeoPsiFactory
import im.mrx.leolanguage.leo.psi.LeoTypes

abstract class LeoStubbedElementImpl<T : StubElement<PsiElement>> : StubBasedPsiElementBase<T>, LeoNamedElement {

    constructor(node: ASTNode) : super(node)

    // stub has the correct type, however it should be avoided
    // the issue is how the stub element is being extended by the psi element
    // TODO: help is needed to make it work
    @Suppress("UNCHECKED_CAST")
    constructor(stub: StubElement<PsiElement>, type: IStubElementType<*, *>) : super(stub as T, type)

    private fun nameIdentifier(): PsiElement? {
        return findChildByType(LeoTypes.IDENTIFIER)
    }

    override fun getName(): String? {
        return nameIdentifier()?.text
    }

    override fun setName(name: String): PsiElement {
        nameIdentifier()?.replace(LeoPsiFactory.instance.createIdentifier(project, name))
        return this
    }

    override fun getTextOffset(): Int {
        return nameIdentifier()?.textOffset ?: super.getTextOffset()
    }
}
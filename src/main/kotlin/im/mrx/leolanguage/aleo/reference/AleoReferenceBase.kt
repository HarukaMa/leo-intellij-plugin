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

package im.mrx.leolanguage.aleo.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import im.mrx.leolanguage.aleo.psi.AleoPsiFactory
import im.mrx.leolanguage.aleo.psi.AleoReferenceElement

abstract class AleoReferenceBase<T : AleoReferenceElement>(element: T) : PsiReferenceBase<T>(element) {

    override fun handleElementRename(newElementName: String): PsiElement {
        val identifier = element.referenceNameElement() ?: return element
        identifier.replace(AleoPsiFactory.instance.createIdentifier(element.project, newElementName))
        return element
    }

    override fun getRangeInElement(): TextRange {
        return ElementManipulators.getValueTextRange(element)
    }

}
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

package im.mrx.leolanguage.leo.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import im.mrx.leolanguage.leo.reference.LeoCircuitComponentReference

abstract class LeoCircuitComponentIdentifierImplMixin(node: ASTNode) : ASTWrapperPsiElement(node),
    LeoCircuitComponentIdentifier {

    override fun getReference(): PsiReference? {
        return LeoCircuitComponentReference(this)
    }

    override fun referenceNameElement(): PsiElement? {
        return this.identifier
    }

    override fun getTypeElement(): PsiElement? {
        when (parent) {
            is LeoCircuitComponentExpression -> {
                val expression = (parent as? LeoCircuitComponentExpression)?.expression as LeoPrimaryExpression
                val reference = expression.variableOrFreeConstant?.reference?.resolve() ?: return null
                for (child in reference.children) {
                    if (child is LeoNamedType) {
                        return child.reference?.resolve()
                    }
                }
            }

            is LeoCircuitComponentInitializer -> {
                val expression = parent.parent as? LeoCircuitExpression ?: return null
                return expression.circuitExpressionIdentifier.reference?.resolve()
            }
        }

        return null
    }

}
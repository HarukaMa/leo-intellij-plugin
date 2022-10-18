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

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import com.intellij.psi.util.PsiTreeUtil
import im.mrx.leolanguage.leo.LeoLanguage
import im.mrx.leolanguage.leo.psi.LeoCircuitComponentDeclaration
import im.mrx.leolanguage.leo.psi.LeoCircuitDeclaration
import im.mrx.leolanguage.leo.psi.LeoNamedElement
import im.mrx.leolanguage.leo.psi.LeoRecordDeclaration

class LeoSymbolStub<T : LeoNamedElement>(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
    override val name: String?
) : StubBase<T>(parent, elementType), LeoNamedStub

class LeoSymbolStubType<T : LeoNamedElement>(
    debugName: String,
    private val psiConstructor: (LeoSymbolStub<*>, IStubElementType<*, *>) -> T
) : IStubElementType<LeoSymbolStub<LeoNamedElement>, LeoNamedElement>(debugName, LeoLanguage.INSTANCE) {
    override fun getExternalId(): String {
        return "leo." + super.toString()
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): LeoSymbolStub<LeoNamedElement> {
        return LeoSymbolStub(parentStub, this, dataStream.readNameString())
    }

    override fun createStub(
        psi: LeoNamedElement,
        parentStub: StubElement<out PsiElement>?
    ): LeoSymbolStub<LeoNamedElement> {
        val name = if (psi is LeoCircuitComponentDeclaration) {
            "${
                (PsiTreeUtil.getParentOfType(psi, LeoCircuitDeclaration::class.java) ?: PsiTreeUtil.getParentOfType(
                    psi,
                    LeoRecordDeclaration::class.java
                ))?.name ?: ""
            }.${psi.name}"
        } else psi.name
        return LeoSymbolStub(parentStub, this, name)
    }

    override fun createPsi(stub: LeoSymbolStub<LeoNamedElement>): T {
        return psiConstructor(stub, this)
    }

    override fun indexStub(stub: LeoSymbolStub<LeoNamedElement>, sink: IndexSink) {
        stub.name?.let {
            sink.occurrence(LeoNamedElementIndex.KEY, it)
        }
    }

    override fun serialize(stub: LeoSymbolStub<LeoNamedElement>, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
    }
}
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

import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StubIndex
import com.intellij.util.Processors
import im.mrx.leolanguage.leo.psi.LeoNamedElement

object IndexUtils {

    fun getNamedElementFromFile(name: String, file: PsiFile): LeoNamedElement? {
        return StubIndex.getElements(
            LeoNamedElementIndex.KEY,
            name,
            file.project,
            GlobalSearchScope.fileScope(file),
            LeoNamedElement::class.java
        ).firstOrNull()
    }

    fun getNamedElementKeysFromFile(file: PsiFile): List<String> {
        val result = mutableListOf<String>()
        StubIndex.getInstance().processAllKeys(
            LeoNamedElementIndex.KEY,
            Processors.cancelableCollectProcessor(result), GlobalSearchScope.fileScope(file)
        )
        return result
    }

}
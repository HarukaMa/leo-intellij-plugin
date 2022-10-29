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

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import im.mrx.leolanguage.leo.LeoFileType
import im.mrx.leolanguage.leo.LeoUtils

object LeoPsiFactory {
    fun createIdentifier(project: Project, name: String): PsiElement {
        val file = PsiFileFactory.getInstance(project)
            .createFileFromText("temp.leo", LeoFileType.INSTANCE, "program temp.aleo { function ${name}() {} }")
        val declaration = LeoUtils.getProgramChildrenOfTypeInFile(file, LeoFunctionDeclaration::class.java).first()
        return declaration.identifier!!
    }

    fun createProgramId(project: Project, name: String): LeoProgramId {
        val file = PsiFileFactory.getInstance(project)
            .createFileFromText("temp.leo", LeoFileType.INSTANCE, "program $name.aleo {}")
        return PsiTreeUtil.findChildOfType(file, LeoProgramId::class.java)!!
    }
}
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

package im.mrx.leolanguage.aleo

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class AleoFileType : LanguageFileType(AleoLanguage.INSTANCE) {

    companion object {
        @Suppress("unused")
        val INSTANCE = AleoFileType()
    }

    override fun getName(): String {
        return "Aleo"
    }

    override fun getDescription(): String {
        return "Aleo instructions"
    }

    override fun getDefaultExtension(): String {
        return "aleo"
    }

    override fun getIcon(): Icon {
        return AleoIcons.FILE
    }

}
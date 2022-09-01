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

package im.mrx.leolanguage.aleo.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import im.mrx.leolanguage.aleo.AleoIcons
import javax.swing.Icon

class AleoColorSettingsPage : ColorSettingsPage {
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return arrayOf(
            AttributesDescriptor("Keyword", AleoSyntaxHighlighter.KEYWORD),
            AttributesDescriptor("Instruction name", AleoSyntaxHighlighter.INSTRUCTION_NAME),
            AttributesDescriptor("Identifier", AleoSyntaxHighlighter.IDENTIFIER),
            AttributesDescriptor("String", AleoSyntaxHighlighter.STRING),
            AttributesDescriptor("Number", AleoSyntaxHighlighter.NUMBER),
            AttributesDescriptor("Register", AleoSyntaxHighlighter.REGISTER),
            AttributesDescriptor("Comment", AleoSyntaxHighlighter.COMMENT),
            AttributesDescriptor("Address", AleoSyntaxHighlighter.ADDRESS),
            AttributesDescriptor("Visibility", AleoSyntaxHighlighter.ENTRY_VISIBILITY),
            AttributesDescriptor("Braces and Operators//Semicolon", AleoSyntaxHighlighter.SEMICOLON),
            AttributesDescriptor("Braces and Operators//Dot", AleoSyntaxHighlighter.DOT),
        )
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return "Aleo"
    }

    override fun getIcon(): Icon {
        return AleoIcons.FILE
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return AleoSyntaxHighlighter()
    }

    override fun getDemoText(): String {
        return """
        program test.aleo;
        
        function main:
            input r0 as u32.public;
            input r1 as u32.private;
            add r0 r1 into r2;
            output r2 as u32.private;
        """.trimIndent()
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey> {
        return mutableMapOf()
    }
}
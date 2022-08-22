package im.mrx.leolanguage.psi

import com.intellij.psi.tree.IElementType
import im.mrx.leolanguage.leo.LeoLanguage

class LeoElementType(name: String) : IElementType(name, LeoLanguage.INSTANCE) {
    override fun toString(): String {
        return "LeoElementType." + super.toString()
    }
}
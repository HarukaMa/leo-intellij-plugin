package im.mrx.leolanguage.psi

import com.intellij.psi.tree.IElementType
import im.mrx.leolanguage.LeoLanguage

class LeoTokenType(name: String) : IElementType(name, LeoLanguage.INSTANCE) {
//    override fun toString(): String {
//        return "LeoTokenType." + super.toString()
//    }
}
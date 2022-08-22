package im.mrx.leolanguage

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class LeoFileType : LanguageFileType(LeoLanguage.INSTANCE) {
    companion object {
        val INSTANCE = LeoFileType()
    }

    override fun getName(): String {
        return "Leo"
    }

    override fun getDescription(): String {
        return "Leo source code"
    }

    override fun getDefaultExtension(): String {
        return "leo"
    }

    override fun getIcon(): Icon {
        return AleoIcons.FILE
    }

}
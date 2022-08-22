package im.mrx.leolanguage.leo

import com.intellij.openapi.fileTypes.LanguageFileType
import im.mrx.leolanguage.aleo.AleoIcons
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
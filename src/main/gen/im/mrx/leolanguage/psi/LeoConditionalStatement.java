// This is a generated file. Not intended for manual editing.
package im.mrx.leolanguage.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LeoConditionalStatement extends PsiElement {

  @Nullable
  LeoBlock getBlock();

  @NotNull
  LeoBranch getBranch();

  @Nullable
  LeoConditionalStatement getConditionalStatement();

}

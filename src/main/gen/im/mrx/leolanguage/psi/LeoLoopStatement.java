// This is a generated file. Not intended for manual editing.
package im.mrx.leolanguage.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LeoLoopStatement extends PsiElement {

  @Nullable
  LeoBlock getBlock();

  @NotNull
  List<LeoExpression> getExpressionList();

  @Nullable
  LeoType getType();

  @Nullable
  PsiElement getIdentifier();

}

// This is a generated file. Not intended for manual editing.
package im.mrx.leolanguage.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static im.mrx.leolanguage.psi.LeoTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import im.mrx.leolanguage.psi.*;

public class LeoConsoleStatementImpl extends ASTWrapperPsiElement implements LeoConsoleStatement {

  public LeoConsoleStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LeoVisitor visitor) {
    visitor.visitConsoleStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LeoVisitor) accept((LeoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public LeoAssertCall getAssertCall() {
    return findChildByClass(LeoAssertCall.class);
  }

  @Override
  @Nullable
  public LeoAssertEqualCall getAssertEqualCall() {
    return findChildByClass(LeoAssertEqualCall.class);
  }

  @Override
  @Nullable
  public LeoAssertNotEqualCall getAssertNotEqualCall() {
    return findChildByClass(LeoAssertNotEqualCall.class);
  }

}

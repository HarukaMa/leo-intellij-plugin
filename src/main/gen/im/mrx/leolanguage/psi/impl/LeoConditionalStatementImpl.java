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

public class LeoConditionalStatementImpl extends ASTWrapperPsiElement implements LeoConditionalStatement {

  public LeoConditionalStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LeoVisitor visitor) {
    visitor.visitConditionalStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LeoVisitor) accept((LeoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public LeoBlock getBlock() {
    return findChildByClass(LeoBlock.class);
  }

  @Override
  @NotNull
  public LeoBranch getBranch() {
    return findNotNullChildByClass(LeoBranch.class);
  }

  @Override
  @Nullable
  public LeoConditionalStatement getConditionalStatement() {
    return findChildByClass(LeoConditionalStatement.class);
  }

}

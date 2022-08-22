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

public class LeoLoopStatementImpl extends ASTWrapperPsiElement implements LeoLoopStatement {

  public LeoLoopStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LeoVisitor visitor) {
    visitor.visitLoopStatement(this);
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
  public List<LeoExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LeoExpression.class);
  }

  @Override
  @Nullable
  public LeoType getType() {
    return findChildByClass(LeoType.class);
  }

  @Override
  @Nullable
  public PsiElement getIdentifier() {
    return findChildByType(IDENTIFIER);
  }

}

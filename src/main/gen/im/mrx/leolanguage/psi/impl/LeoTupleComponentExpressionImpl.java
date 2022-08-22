// This is a generated file. Not intended for manual editing.
package im.mrx.leolanguage.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static im.mrx.leolanguage.psi.LeoTypes.*;
import im.mrx.leolanguage.psi.*;

public class LeoTupleComponentExpressionImpl extends LeoExpressionImpl implements LeoTupleComponentExpression {

  public LeoTupleComponentExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull LeoVisitor visitor) {
    visitor.visitTupleComponentExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LeoVisitor) accept((LeoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public LeoExpression getExpression() {
    return findNotNullChildByClass(LeoExpression.class);
  }

  @Override
  @NotNull
  public PsiElement getNumeral() {
    return findNotNullChildByType(NUMERAL);
  }

}

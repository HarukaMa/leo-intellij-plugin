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

public class LeoAtomicLiteralImpl extends ASTWrapperPsiElement implements LeoAtomicLiteral {

  public LeoAtomicLiteralImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LeoVisitor visitor) {
    visitor.visitAtomicLiteral(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LeoVisitor) accept((LeoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getAddressLiteral() {
    return findChildByType(ADDRESS_LITERAL);
  }

  @Override
  @Nullable
  public PsiElement getBooleanLiteral() {
    return findChildByType(BOOLEAN_LITERAL);
  }

  @Override
  @Nullable
  public PsiElement getNumericLiteral() {
    return findChildByType(NUMERIC_LITERAL);
  }

  @Override
  @Nullable
  public PsiElement getStringLiteral() {
    return findChildByType(STRING_LITERAL);
  }

}

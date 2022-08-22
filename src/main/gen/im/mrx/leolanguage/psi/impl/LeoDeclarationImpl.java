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

public class LeoDeclarationImpl extends ASTWrapperPsiElement implements LeoDeclaration {

  public LeoDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LeoVisitor visitor) {
    visitor.visitDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LeoVisitor) accept((LeoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public LeoCircuitDeclaration getCircuitDeclaration() {
    return findChildByClass(LeoCircuitDeclaration.class);
  }

  @Override
  @Nullable
  public LeoFunctionDeclaration getFunctionDeclaration() {
    return findChildByClass(LeoFunctionDeclaration.class);
  }

  @Override
  @Nullable
  public LeoRecordDeclaration getRecordDeclaration() {
    return findChildByClass(LeoRecordDeclaration.class);
  }

  @Override
  @Nullable
  public PsiElement getBlockComment() {
    return findChildByType(BLOCK_COMMENT);
  }

  @Override
  @Nullable
  public PsiElement getEndOfLineComment() {
    return findChildByType(END_OF_LINE_COMMENT);
  }

}

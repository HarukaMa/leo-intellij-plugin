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

public class LeoCircuitComponentDeclarationsImpl extends ASTWrapperPsiElement implements LeoCircuitComponentDeclarations {

  public LeoCircuitComponentDeclarationsImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LeoVisitor visitor) {
    visitor.visitCircuitComponentDeclarations(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LeoVisitor) accept((LeoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<LeoCircuitComponentDeclaration> getCircuitComponentDeclarationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LeoCircuitComponentDeclaration.class);
  }

}

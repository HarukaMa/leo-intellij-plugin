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

public class LeoStatementImpl extends ASTWrapperPsiElement implements LeoStatement {

  public LeoStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LeoVisitor visitor) {
    visitor.visitStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LeoVisitor) accept((LeoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public LeoAssignmentStatement getAssignmentStatement() {
    return findChildByClass(LeoAssignmentStatement.class);
  }

  @Override
  @Nullable
  public LeoBlock getBlock() {
    return findChildByClass(LeoBlock.class);
  }

  @Override
  @Nullable
  public LeoConditionalStatement getConditionalStatement() {
    return findChildByClass(LeoConditionalStatement.class);
  }

  @Override
  @Nullable
  public LeoConsoleStatement getConsoleStatement() {
    return findChildByClass(LeoConsoleStatement.class);
  }

  @Override
  @Nullable
  public LeoConstantDeclaration getConstantDeclaration() {
    return findChildByClass(LeoConstantDeclaration.class);
  }

  @Override
  @Nullable
  public LeoLoopStatement getLoopStatement() {
    return findChildByClass(LeoLoopStatement.class);
  }

  @Override
  @Nullable
  public LeoReturnStatement getReturnStatement() {
    return findChildByClass(LeoReturnStatement.class);
  }

  @Override
  @Nullable
  public LeoVariableDeclaration getVariableDeclaration() {
    return findChildByClass(LeoVariableDeclaration.class);
  }

}

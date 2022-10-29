/*
 * Copyright (c) 2022 Haruka Ma
 * This file is part of Leo / Aleo IntelliJ plugin.
 *
 * Leo / Aleo IntelliJ plugin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Leo / Aleo IntelliJ plugin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Leo / Aleo IntelliJ plugin. If not, see <https://www.gnu.org/licenses/>.
 */

package im.mrx.leolanguage.leo.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import im.mrx.leolanguage.leo.LeoUtils
import im.mrx.leolanguage.leo.inspection.quickfix.ProgramIdFix
import im.mrx.leolanguage.leo.psi.*
import im.mrx.leolanguage.leo.psi.LeoTypes.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class LeoCompilerInspection : LocalInspectionTool() {

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor {
        return Visitor(holder)
    }
}

private class Visitor(private val holder: ProblemsHolder) : LeoVisitor() {
    override fun visitImportProgramId(o: LeoImportProgramId) {
        // Compiler #2
        run {
            val ref = o.reference?.resolve() != null
            if (!ref) {
                holder.registerProblem(
                    o,
                    "[ECMP0376002]: Attempted to import a file that does not exist `${o.text}`.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }

        // Parser #24
        run {
            val programId = o.programId
            val extension = programId.text.split(".").last()
            if (extension != "leo") {
                holder.registerProblem(
                    programId,
                    "[EPAR0370024]: Invalid import call to non-leo file.\n\nOnly imports of Leo `.leo` files are currently supported.",
                    ProblemHighlightType.GENERIC_ERROR,
                    TextRange(programId.text.indexOf('.') + 1, programId.text.length),
                )
            }
        }
    }

    override fun visitProgramDeclaration(o: LeoProgramDeclaration) {
        // Compiler #5
        run {
            val programName = o.programId?.text?.split(".")?.first() ?: return@run
            val file = o.containingFile.originalFile
            val scope = if (file.name == "main.leo" && (file.containingDirectory?.name ?: "") == "src") {
                val json = file.containingDirectory.parentDirectory?.findFile("program.json") ?: return@run
                Json.parseToJsonElement(json.text).jsonObject["program"]?.jsonPrimitive?.content?.split(".")
                    ?.first() ?: return@run
            } else {
                file.name.split(".").first()
            }
            if (programName != scope) {
                holder.registerProblem(
                    o.programId!!,
                    "[ECMP0376005]: The program scope name `${programName}` must match `${scope}`.",
                    ProblemHighlightType.GENERIC_ERROR,
                    ProgramIdFix(o.programId!!, scope)
                )
            }
        }

        // Parser #30
        run {
            val networkIdentifier = o.programId?.text?.split(".")?.last() ?: return@run
            if (networkIdentifier != "aleo") {
                holder.registerProblem(
                    o.programId!!,
                    "[EPAR0370030]: Invalid network identifier. The only supported identifier is `aleo`.",
                    ProblemHighlightType.GENERIC_ERROR,
                    TextRange(o.programId!!.text.indexOf('.') + 1, o.programId!!.text.length),
                )
            }
        }
    }

    override fun visitExpressionStatement(o: LeoExpressionStatement) {
        // Parser #21
        run {
            holder.registerProblem(o, "[EPAR0370021]: Expression statements are not supported.")
            ProblemHighlightType.GENERIC_ERROR
        }
    }

    override fun visitUnaryOperatorCall(o: LeoUnaryOperatorCall) {
        // Parser #22.1
        run {
            val operator = o.identifier.text
            if (operator !in listOf(
                    "abs",
                    "abs_wrapped",
                    "double",
                    "inv",
                    "neg",
                    "not",
                    "square",
                    "square_root"
                )
            ) {
                holder.registerProblem(
                    o,
                    "[EPAR0370022]: The type of `${o.expression.text}` has no associated function `$operator`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitBinaryOperatorCall(o: LeoBinaryOperatorCall) {
        // Parser #22.2
        run {
            val operator = o.identifier.text
            if (operator !in listOf(
                    "add",
                    "add_wrapped",
                    "and",
                    "div",
                    "div_wrapped",
                    "eq",
                    "gte",
                    "gt",
                    "lte",
                    "lt",
                    "mod",
                    "mul",
                    "mul_wrapped",
                    "nand",
                    "neq",
                    "nor",
                    "or",
                    "pow",
                    "pow_wrapped",
                    "rem",
                    "rem_wrapped",
                    "shl",
                    "shl_wrapped",
                    "shr",
                    "shr_wrapped",
                    "sub",
                    "sub_wrapped",
                    "xor"
                )
            ) {
                holder.registerProblem(
                    o,
                    "[EPAR0370022]: The type of `${o.expressionList.first().text}` has no associated function `$operator`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitAssociatedConstant(o: LeoAssociatedConstant) {
        // Parser #23
        // placeholder as the official compiler is crashing here
        run {}
    }

    override fun visitAnnotation(o: LeoAnnotation) {
        // Parser #25
        run {
            var child = o.firstChild
            while (child != null) {
                if (child is PsiWhiteSpace) {
                    holder.registerProblem(
                        o,
                        "[EPAR0370025]: Illegal spacing in the annotation declaration.\n\nRemove whitespace between the `@` symbol and the identifier.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                    break
                }
                child = child.nextSibling
            }
        }
    }

    private fun checkTYC3(o: PsiElement, type: String, actualType: String) {
        if (actualType != "pass") {
            if (actualType.contains('|')) {
                val types = actualType.split('|')
                types.filter { it != actualType }.forEach { registerTYC3Problem(o, type, it) }
            } else if (actualType != type) {
                registerTYC3Problem(o, type, actualType)
            }
        }
    }

    private fun registerTYC3Problem(value: PsiElement, expected: String, actual: String) {
        holder.registerProblem(
            value,
            "[ETYC0372003]: Expected type `${expected}` but type `${actual}` was found",
            ProblemHighlightType.GENERIC_ERROR
        )
    }

    override fun visitAssignmentStatement(o: LeoAssignmentStatement) {
        // Type checker #2
        run {
            val variable = o.expressionList.first().firstChild
            variable.reference?.resolve()?.let { resolved ->
                if (resolved is LeoConstantDeclaration) {
                    holder.registerProblem(
                        o,
                        "[ETYC0372002]: Cannot assign to const variable `${variable.text}`.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
        // Type checker #3
        run {

            val variable = o.expressionList.first().firstChild
            variable.reference?.resolve()?.let { resolved ->
                val element = resolved as? LeoTypedElement ?: return@run
                val type = LeoUtils.typeToString(element) ?: return@run
                val value = o.expressionList.last()
                val actualType = getExpressionType(value)
                checkTYC3(value, type, actualType)
            }
        }
    }

    override fun visitReturnStatement(o: LeoReturnStatement) {
        // Type checker #3
        run {
            val function = PsiTreeUtil.getParentOfType(o, LeoFunctionLikeDeclaration::class.java) ?: return@run
            val type = LeoUtils.typeToString(function) ?: return@run
            val actualType = getExpressionType(o.expression ?: return@run)
            checkTYC3(o.expression!!, type, actualType)
        }
    }

    override fun visitVariableDeclaration(o: LeoVariableDeclaration) {
        // Type checker #3
        run {
            val type = LeoUtils.typeToString(o) ?: return@run
            val actualType = getExpressionType(o.expression ?: return@run)
            checkTYC3(o.expression!!, type, actualType)
        }
    }

    override fun visitConstantDeclaration(o: LeoConstantDeclaration) {
        // Type checker #3
        run {
            val type = LeoUtils.typeToString(o) ?: return@run
            val actualType = getExpressionType(o.expression ?: return@run)
            checkTYC3(o.expression!!, type, actualType)
        }
    }

    override fun visitLoopStatement(o: LeoLoopStatement) {
        // Type checker #3
        run {
            val type = LeoUtils.typeToString(o) ?: return@run
            if (o.expressionList.size != 2) {
                return
            }
            val firstType = getExpressionType(o.expressionList[0])
            val secondType = getExpressionType(o.expressionList[1])
            checkTYC3(o.expressionList[0], type, firstType)
            checkTYC3(o.expressionList[1], type, secondType)
        }
    }

    override fun visitFreeFunctionCall(o: LeoFreeFunctionCall) {
        // Type checker #3
        run {
            val types = LeoUtils.functionParameterListToTypeString(
                (o.locator ?: o.functionIdentifier ?: return@run).reference?.resolve() as? LeoFunctionLikeDeclaration
                    ?: return@run
            ).split(", ")
            o.functionArguments.expressionList.forEachIndexed { index, expression ->
                val type = types.getOrNull(index) ?: return@forEachIndexed
                val actualType = getExpressionType(expression)
                checkTYC3(expression, type, actualType)
            }
        }
    }

    private fun getExpressionType(element: PsiElement): String {
        return when (element) {
            is LeoPrimaryExpression -> getPrimaryExpressionType(element)

            is LeoTupleComponentExpression -> getTupleComponentType(element)
            is LeoStructComponentExpression -> getStructComponentType(element)
            is LeoUnaryOperatorCall -> "pass" // TODO check types
            is LeoBinaryOperatorCall -> "pass" // TODO check types

            is LeoUnaryNotExpression -> getExpressionType(element.expression ?: return "pass")
            is LeoUnaryMinusExpression -> getExpressionType(element.expression ?: return "pass")

            is LeoExponentialExpression -> getExpressionType(element.expressionList.first())

            is LeoMultiplicativeMulExpression -> getExpressionType(element.expressionList.first())
            is LeoMultiplicativeDivExpression -> getExpressionType(element.expressionList.first())
            is LeoMultiplicativeModExpression -> getExpressionType(element.expressionList.first())

            is LeoAdditiveAddExpression -> getExpressionType(element.expressionList.first())
            is LeoAdditiveSubExpression -> getExpressionType(element.expressionList.first())

            is LeoShiftLeftExpression -> getExpressionType(element.expressionList.first())
            is LeoShiftRightExpression -> getExpressionType(element.expressionList.first())

            is LeoConjunctiveExpression -> getExpressionType(element.expressionList.first())

            is LeoDisjunctiveExpression -> getExpressionType(element.expressionList.first())

            is LeoExclusiveDisjunctiveExpression -> getExpressionType(element.expressionList.first())

            is LeoOrderingLessExpression -> "bool"
            is LeoOrderingGreaterExpression -> "bool"
            is LeoOrderingLessOrEqualExpression -> "bool"
            is LeoOrderingGreaterOrEqualExpression -> "bool"

            is LeoEqualityExpression -> "bool"
            is LeoInequalityExpression -> "bool"

            is LeoConditionalConjunctiveExpression -> "bool"

            is LeoConditionalDisjunctiveExpression -> "bool"

            is LeoConditionalTernaryExpression -> getTernaryType(element)

            else -> element.elementType.toString()
        }
    }

    private fun getTupleType(element: LeoTupleExpression): String {
        val types = element.expressionList.map { getExpressionType(it) }
        return "(${types.joinToString(", ")})"
    }

    private fun getPrimaryExpressionType(expression: LeoPrimaryExpression): String {
        return when (val exp = expression.firstChild) {
            is LeoStaticFunctionCall -> "pass" // TODO check types
            is LeoAssociatedConstant -> "pass" // TODO check types
            is LeoSelfExpression ->
                if (exp.identifier.text == "caller") {
                    "address"
                } else {
                    "pass"
                }

            is LeoFreeFunctionCall ->
                (exp.functionIdentifier?.reference?.resolve() as? LeoTypedElement)?.let {
                    LeoUtils.typeToString(it) ?: "unknown"
                } ?: "pass"

            is LeoTupleExpression -> getTupleType(exp)
            is LeoStructExpression -> exp.structExpressionIdentifier.reference?.resolve()?.let {
                exp.structExpressionIdentifier.text
            } ?: "pass"

            is LeoVariableOrFreeConstant -> exp.reference?.resolve()?.let {
                LeoUtils.typeToString(it as? LeoTypedElement ?: return@let "unknown") ?: "unknown"
            } ?: "pass"

            else ->
                if (exp is LeafPsiElement) {
                    getLiteralType(exp)
                } else {
                    exp.elementType.toString()
                }
        }
    }

    private fun getLiteralType(literal: PsiElement): String {
        return when (literal.elementType) {
            ADDRESS_LITERAL -> "address"
            BOOLEAN_LITERAL -> "bool"
            NUMERIC_LITERAL -> getNumericLiteralType(literal)
            else -> "unknown"
        }
    }

    private fun getNumericLiteralType(literal: PsiElement): String {
        val text = literal.text
        val regex = Regex("^[0-9]+([ui](?>8|16|32|64|128)|field|group|scalar)")
        val match = regex.find(text) ?: return "unknown"
        return match.groups[1]?.value ?: "unknown"
    }

    private fun getTupleComponentType(tuple: LeoTupleComponentExpression): String {
        val tupleIndex = tuple.numeral.text.toInt()
        val tupleType = getExpressionType(tuple.expression)
        if (tupleType.startsWith("(") && tupleType.endsWith(")")) {
            val types = tupleType.substring(1, tupleType.length - 1).split(",")
            if (tupleIndex < types.size) {
                return types[tupleIndex].trim()
            }
        }
        return "unknown"
    }

    private fun getStructComponentType(struct: LeoStructComponentExpression): String {
        return struct.structComponentIdentifier.reference?.resolve()?.let { resolved ->
            if (resolved is LeoTypedElement) {
                LeoUtils.typeToString(resolved) ?: "unknown"
            } else {
                "unknown"
            }
        } ?: "pass"
    }

    private fun getTernaryType(ternary: LeoConditionalTernaryExpression): String {
        if (ternary.expressionList.size != 3) {
            return "unknown"
        }
        val firstType = getExpressionType(ternary.expressionList[1])
        val secondType = getExpressionType(ternary.expressionList[2])
        return "$firstType|$secondType"
    }
}
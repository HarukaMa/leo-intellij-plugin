/*
 * Copyright (c) 2022-2023 Haruka Ma
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
import com.intellij.psi.TokenType.WHITE_SPACE
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
import java.math.BigInteger

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

    // TODO: TYC7 has more type checks

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

        // Parser #23
        run {
            val programId = o.programId
            val extension = programId.text.split(".").last()
            if (extension != "leo") {
                holder.registerProblem(
                    programId,
                    "[EPAR0370023]: Invalid import call to non-leo file.\n\nOnly imports of Leo `.leo` files are currently supported.",
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

        // Parser #29
        run {
            val networkIdentifier = o.programId?.text?.split(".")?.last() ?: return@run
            if (networkIdentifier != "aleo") {
                holder.registerProblem(
                    o.programId!!,
                    "[EPAR0370029]: Invalid network identifier. The only supported identifier is `aleo`.",
                    ProblemHighlightType.GENERIC_ERROR,
                    TextRange(o.programId!!.text.indexOf('.') + 1, o.programId!!.text.length),
                )
            }
        }
    }

    override fun visitAssociatedConstant(o: LeoAssociatedConstant) {
        // Parser #22
        // placeholder as the official compiler is crashing here
        run {}
    }

    override fun visitAnnotation(o: LeoAnnotation) {
        // Parser #24
        run {
            var child = o.firstChild
            while (child != null) {
                if (child is PsiWhiteSpace) {
                    holder.registerProblem(
                        o,
                        "[EPAR0370024]: Illegal spacing in the annotation declaration.\n\nRemove whitespace between the `@` symbol and the identifier.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                    break
                }
                child = child.nextSibling
            }
        }
        // Type checker #28
        run {
            holder.registerProblem(
                o,
                "[ETYC0372028]: Unknown annotation: `${o.text}`.",
                ProblemHighlightType.GENERIC_ERROR
            )
        }
    }

    override fun visitAssignmentStatement(o: LeoAssignmentStatement) {
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
            val function =
                PsiTreeUtil.getParentOfType(o, LeoFinalizer::class.java) ?: PsiTreeUtil.getParentOfType(
                    o,
                    LeoFunctionLikeDeclaration::class.java
                ) ?: return@run
            val type = LeoUtils.typeToString(function) ?: return@run
            val actualType = getExpressionType(o.expression ?: return@run)
            checkTYC3(o.expression!!, type, actualType)
        }
        // Type checker #35
        run {
            if (o.finalizeLiteral != null) {
                fun registerTYC35Problem() {
                    holder.registerProblem(
                        o,
                        "[ETYC0372035]: Cannot use a `finalize` statement without a `finalize` block.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }

                val transition = PsiTreeUtil.getParentOfType(o, LeoTransitionDeclaration::class.java)
                if (transition == null) {
                    registerTYC35Problem()
                } else {
                    transition.finalizer ?: registerTYC35Problem()
                }
            }
        }
        // Type checker #42
        run {
            val transition = PsiTreeUtil.getParentOfType(o, LeoTransitionDeclaration::class.java) ?: return@run
            val finalizer = transition.finalizer ?: return@run
            val params = finalizer.functionParameterList?.functionParameterList?.size ?: return@run
            val actualParams = o.functionArguments?.expressionList?.size ?: return@run
            if (params != actualParams) {
                holder.registerProblem(
                    o,
                    "[ETYC0372042]: `finalize` expected `$params` args, but got `$actualParams`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitVariableLikeDeclaration(o: LeoVariableLikeDeclaration) {
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
        // Type checker #3, #6
        run {
            val types = LeoUtils.functionParameterListToTypeString(
                (o.locator ?: o.functionIdentifier ?: return@run).reference?.resolve() as? LeoFunctionLikeDeclaration
                    ?: return@run
            ).split(", ").toMutableList()
            types.remove("")
            o.functionArguments.expressionList.forEachIndexed { index, expression ->
                val type = types.getOrNull(index) ?: return@forEachIndexed
                val actualType = getExpressionType(expression)
                checkTYC3(expression, type, actualType)
            }
            val args = types.size
            val actualArgs = o.functionArguments.expressionList.size
            if (args != actualArgs) {
                holder.registerProblem(
                    o,
                    "[ETYC0372006]: Call expected `${args}` args, but got `${actualArgs}`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
        // Type checker #5
        run {
            o.functionIdentifier?.let {
                if (it.text in listOf("assert", "assert_eq", "assert_neq")) {
                    return@let
                }
                it.reference?.resolve() ?: run {
                    holder.registerProblem(
                        it,
                        "[ETYC0372005]: Unknown function `${it.text}`",
                        ProblemHighlightType.ERROR
                    )
                }
            }
            o.locator?.let {
                it.reference?.resolve() ?: run {
                    it.identifier?.let { identifier ->
                        holder.registerProblem(
                            identifier,
                            "[ETYC0372005]: Unknown function `${identifier.text}`",
                            ProblemHighlightType.ERROR
                        )
                    }
                }
            }
        }
        // Type checker #47
        run {
            if (PsiTreeUtil.getParentOfType(o, LeoFunctionDeclaration::class.java) != null) {
                holder.registerProblem(
                    o,
                    "[ETYC0372047]: Cannot call another function from a standard function.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
        // Type checker #48
        run {
            if (PsiTreeUtil.getParentOfType(
                    o,
                    LeoTransitionDeclaration::class.java
                ) != null && o.locator == null && o.functionIdentifier?.reference?.resolve() as? LeoTransitionDeclaration != null
            ) {
                holder.registerProblem(
                    o,
                    "[ETYC0372048]: Cannot call a local transition function from a transition function.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitAdditiveAddExpression(o: LeoAdditiveAddExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitAdditiveSubExpression(o: LeoAdditiveSubExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitMultiplicativeMulExpression(o: LeoMultiplicativeMulExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitMultiplicativeDivExpression(o: LeoMultiplicativeDivExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitMultiplicativeModExpression(o: LeoMultiplicativeModExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitEqualityExpression(o: LeoEqualityExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitInequalityExpression(o: LeoInequalityExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitConjunctiveExpression(o: LeoConjunctiveExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitDisjunctiveExpression(o: LeoDisjunctiveExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitExclusiveDisjunctiveExpression(o: LeoExclusiveDisjunctiveExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitVariable(o: LeoVariable) {
        // Type checker #5
        run {
            o.reference?.resolve() ?: run {
                holder.registerProblem(
                    o,
                    "[ETYC0372005]: Unknown variable `${o.identifier.text}`",
                    ProblemHighlightType.ERROR
                )
            }
        }
    }

    override fun visitStructExpression(o: LeoStructExpression) {
        // Type checker #5, #12, #13
        run {
            val identifier = o.structExpressionIdentifier
            val declaration = identifier.reference?.resolve() as? LeoStructLikeDeclaration
            if (declaration == null) {
                holder.registerProblem(
                    identifier,
                    "[ETYC0372005]: Unknown struct `${identifier.text}`",
                    ProblemHighlightType.ERROR
                )
                return@run
            }
            val memberCount =
                declaration.structComponentDeclarations?.structComponentDeclarationList?.size ?: return@run
            val actualMemberCount = o.structComponentInitializerList.size
            if (memberCount != actualMemberCount) {
                holder.registerProblem(
                    o,
                    "[ETYC0372012]: Struct expected `${memberCount}` members, but got `${actualMemberCount}`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
            val members = declaration.structComponentDeclarations!!.structComponentDeclarationList.map { it.name }
            val actualMembers = o.structComponentInitializerList.map { it.structComponentIdentifier.identifier.text }
            members.forEach { member ->
                if (!actualMembers.contains(member)) {
                    holder.registerProblem(
                        o,
                        "[ETYC0372013]: Struct initialization expression for `${identifier.text}` is missing member `${member}`.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
    }

    override fun visitStructComponentInitializer(o: LeoStructComponentInitializer) {
        // Type checker #3
        run {
            val declaration =
                o.structComponentIdentifier.reference?.resolve() as? LeoStructComponentDeclaration ?: return@run
            val type = LeoUtils.typeToString(declaration) ?: return@run
            val actualType = getExpressionType(o.expression ?: return@run)
            checkTYC3(o.expression!!, type, actualType)
        }
    }

    override fun visitPrimaryExpression(o: LeoPrimaryExpression) {
        // Type checker #8
        run {
            if (o.firstChild is LeafPsiElement && o.firstChild.elementType != LPAREN) {
                val type = getExpressionType(o)
                if (type == "pass" || type == "unknown") {
                    return@run
                }
                if (type !in listOf("u8", "u16", "u32", "u64", "u128", "i8", "i16", "i32", "i64", "i128")) {
                    return@run
                }
                val number = o.firstChild.text.replace(type, "")
                if (!checkNumberRange(number, type)) {
                    holder.registerProblem(
                        o,
                        "[ETYC0372008]: The value $number is not a valid `${type}`",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
    }

    override fun visitUnaryMinusExpression(o: LeoUnaryMinusExpression) {
        // Type checker #8
        run {
            val type = getExpressionType(o.expression ?: return@run)
            if (type == "pass" || type == "unknown") {
                return@run
            }
            if (type !in listOf("u8", "u16", "u32", "u64", "u128", "i8", "i16", "i32", "i64", "i128")) {
                return@run
            }
            val number = o.expression!!.text.replace(type, "")
            if (!checkNumberRange("-$number", type)) {
                holder.registerProblem(
                    o,
                    "[ETYC0372008]: The value -$number is not a valid `${type}`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitAssociatedFunctionCall(o: LeoAssociatedFunctionCall) {
        // Type checker #6, #9
        run {
            fun registerTYC9Problem() {
                holder.registerProblem(
                    o.namedType,
                    "[ETYC0372009]: The instruction ${o.namedType.text}::${o.identifier.text} is not a valid core function.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }

            val coreStruct = o.namedType.coreStruct?.text
            if (coreStruct == null) {
                registerTYC9Problem()
                return@run
            }
            val function = o.identifier.text
            if (function !in listOf("commit", "hash")) {
                registerTYC9Problem()
                return@run
            }
            if (function == "commit" && coreStruct.contains("Poseidon")) {
                registerTYC9Problem()
                return@run
            }
            val args = if (function == "commit") 2 else 1
            val actualArgs = o.functionArguments.expressionList.size
            if (actualArgs != args) {
                holder.registerProblem(
                    o,
                    "[ETYC0372006]: Call expected `${args}` args, but got `${actualArgs}`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
        // Type checker #46
        run {
            fun registerTYC46Problem(element: PsiElement, type: String) {
                holder.registerProblem(
                    element,
                    "[ETYC0372046]: Invalid type `$type`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }

            val coreStruct = o.namedType.coreStruct?.text ?: return@run
            val function = o.identifier.text
            val args = if (function == "commit") 2 else 1
            val actualArgs = o.functionArguments.expressionList.size
            val arg1 = o.functionArguments.expressionList.first()
            val type1 = getExpressionType(arg1)
            val arg2 = if (args == 2 && actualArgs > 1) o.functionArguments.expressionList[1] else null
            val type2 = if (arg2 != null) getExpressionType(arg2) else null
            if (coreStruct.contains("Poseidon") || coreStruct.contains("BHP")) {
                if (type1.contains('(') || type1 == "unknown") {
                    registerTYC46Problem(arg1, type1)
                }
            } else {
                val types = mutableListOf("bool", "i8", "i16", "i32", "i64", "u8", "u16", "u32", "u64", "string")
                if (coreStruct == "Pedersen64" && type1 !in types) {
                    registerTYC46Problem(arg1, type1)
                }
                types += listOf("i128", "u128")
                if (coreStruct == "Pedersen128" && type1 !in types) {
                    registerTYC46Problem(arg1, type1)
                }
            }
            if (args == 2 && actualArgs > 1 && type2 != "scalar") {
                registerTYC46Problem(arg2!!, type2!!)
            }

        }
    }

    override fun visitStructLikeDeclaration(o: LeoStructLikeDeclaration) {
        // Type checker #15, #16
        run {
            val name = o.identifier?.text ?: return@run
            val members = o.structComponentDeclarations?.structComponentDeclarationList ?: return@run
            if (members.size != members.distinct().size) {
                if (o is LeoStructDeclaration) {
                    holder.registerProblem(
                        o,
                        "[ETYC0372015]: Struct $name defined with more than one member with the same name.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                } else {
                    holder.registerProblem(
                        o,
                        "[ETYC0372016]: Record $name defined with more than one member with the same name.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
    }

    override fun visitNamedType(o: LeoNamedType) {
        // Type checker #17
        run {
            if (o.namedPrimitiveType != null || o.coreStruct != null) {
                return@run
            }
            o.reference?.resolve() ?: o.locator?.reference?.resolve() ?: run {
                holder.registerProblem(
                    o,
                    "[ETYC0372017]: The type `${o.text}` is not found in the current scope.",
                    ProblemHighlightType.ERROR
                )
            }
        }
    }

    override fun visitStructComponentExpression(o: LeoStructComponentExpression) {
        // Type checker #18
        run check@{
            val identifier = o.structComponentIdentifier
            identifier.reference?.resolve() ?: run {
                val struct = (identifier.typeElement as? LeoNamedElement)?.name ?: return@check
                holder.registerProblem(
                    identifier,
                    "[ETYC0372018]: Variable ${identifier.text} is not a member of struct $struct.",
                    ProblemHighlightType.ERROR
                )
            }
        }
    }

    override fun visitRecordDeclaration(o: LeoRecordDeclaration) {
        // Type checker #19, #20
        run {
            val members = o.structComponentDeclarations?.structComponentDeclarationList ?: return@run
            val memberNames = members.map { it.name }
            for ((variable, type) in listOf(Pair("owner", "address"), Pair("gates", "u64"))) {
                if (variable !in memberNames) {
                    holder.registerProblem(
                        o,
                        "[ETYC0372019]: The `record` type requires the variable `$variable: $type`.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                } else {
                    val member = members.find { it.name == variable } ?: continue
                    val memberType = LeoUtils.typeToString(member) ?: continue
                    if (memberType != type) {
                        holder.registerProblem(
                            member,
                            "[ETYC0372020]: The field `$variable` in a `record` must have type `$type`.",
                            ProblemHighlightType.GENERIC_ERROR
                        )
                    }
                }
            }
        }
    }

    override fun visitExpression(o: LeoExpression) {
        // Type checker #3, #21
        run {
            if (o is LeoOrderingExpression) {
                val type = getExpressionType(o.expressionList.first())
                val actualType = getExpressionType(o.expressionList.last())
                checkTYC3(o.expressionList.last(), type, actualType)
                if (type == "address" || actualType == "address") {
                    var symbol = o.expressionList.first().nextSibling
                    while (symbol.elementType != SYMBOL) {
                        symbol = symbol.nextSibling
                        if (symbol == null) {
                            return@run
                        }
                    }
                    holder.registerProblem(
                        o,
                        "[ETYC0372021]: Comparison `${symbol.text}` is not supported for the address type.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
    }

    override fun visitTupleComponentExpression(o: LeoTupleComponentExpression) {
        // Type checker #24
        run {
            val tuple = o.expression
            val tupleType = getExpressionType(tuple)
            val index = o.numeral.text.toIntOrNull() ?: return@run
            val tupleSize = tupleType.split(", ").size
            if (index >= tupleSize) {
                holder.registerProblem(
                    o.numeral,
                    "[ETYC0372024]: Tuple index `$index` out of range for a tuple with length `$tupleSize`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitFunctionParameter(o: LeoFunctionParameter) {
        // Type checker #25
        run {
            if (o.tupleType != null) {
                holder.registerProblem(
                    o.tupleType!!,
                    "[ETYC0372025]: Tuples are only allowed as function return types.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
        // Type checker #41
        run {
            if (o.firstChild.elementType == KEYWORD && o.firstChild.text.contains("const")) {
                holder.registerProblem(
                    o.firstChild,
                    "[ETYC0372041]: Transition functions cannot have constant inputs.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitFunctionDeclaration(o: LeoFunctionDeclaration) {
        // Type checker #29
        run {
            o.functionParameterList?.functionParameterList?.forEach { parameter ->
                if (parameter.firstChild?.elementType == KEYWORD) {
                    holder.registerProblem(
                        parameter.firstChild!!,
                        "[ETYC0372029]: Standard functions cannot have modes associated with their inputs.\n\nConsider removing the mode or using the keyword `transition` instead of `function`.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
    }

    override fun visitBlock(o: LeoBlock) {
        // Type checker #26
        run {
            if (PsiTreeUtil.getParentOfType(o, LeoLoopStatement::class.java) != null) {
                return@run
            }
            val statements = o.children
            var returnIndex = statements.size
            statements.forEachIndexed { index, statement ->
                if (statement is LeoReturnStatement) {
                    returnIndex = index
                }
                if (index > returnIndex) {
                    holder.registerProblem(
                        statement,
                        "[ETYC0372026]: Cannot reach the following statement.\n\nRemove the unreachable code.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
        // Type checker #27, #36
        run {
            if (PsiTreeUtil.getParentOfType(o, LeoLoopStatement::class.java) == null) {
                return@run
            }
            PsiTreeUtil.findChildOfType(o, LeoReturnStatement::class.java)?.let {
                holder.registerProblem(
                    o,
                    "[ETYC0372027]: Loop body contains a return statement or always returns.\n\nRemove the code in the loop body that always returns.",
                    ProblemHighlightType.GENERIC_ERROR
                )
                if (it.finalizeLiteral != null) {
                    holder.registerProblem(
                        o,
                        "[ETYC0372036]: Loop body contains a finalize statement.\n\nRemove the finalize statement.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
        // Type checker #33
        run {
            if (PsiTreeUtil.getParentOfType(o, LeoFinalizer::class.java) == null) {
                return@run
            }
            PsiTreeUtil.findChildOfType(o, LeoReturnStatement::class.java)?.let {
                if (it.finalizeLiteral != null) {
                    holder.registerProblem(
                        o,
                        "[ETYC0372033]: A finalize block cannot contain a finalize statement.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
        // Type checker #34
        run {
            if (PsiTreeUtil.getParentOfType(o, LeoFinalizer::class.java) != null) {
                return@run
            }
            if (o.children.any { it is LeoIncrementLikeStatement }) {
                holder.registerProblem(
                    o,
                    "[ETYC0372034]: `increment` or `decrement` statements must be inside a finalize block.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
        // Type checker #38, #44
        run {
            val function = o.parent as? LeoFunctionLikeDeclaration ?: return@run

            val (hasReturn, hasFinalizer) = traverseBlock(o)
            if (LeoUtils.typeToString(function) != null && !hasReturn) {
                holder.registerProblem(
                    o,
                    "[ETYC0372038]: Function must return a value.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
            if (function is LeoTransitionDeclaration && function.finalizer != null && !hasFinalizer) {
                holder.registerProblem(
                    o,
                    "[ETYC0372044]: Function must contain a `finalize` statement on all execution paths.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitStructComponentDeclaration(o: LeoStructComponentDeclaration) {
        // Type checker #25
        run {
            if (o.tupleType != null) {
                holder.registerProblem(
                    o.tupleType!!,
                    "[ETYC0372025]: Tuples are only allowed as function return types.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
        // Type checker #30
        run {
            if ((o.namedType?.reference?.resolve() as? LeoRecordDeclaration) != null) {
                holder.registerProblem(
                    o.namedType!!,
                    "[ETYC0372030]: A struct or record cannot contain another record.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitMappingDeclaration(o: LeoMappingDeclaration) {
        // Type checker #31
        run {
            fun registerTYC31Problem(element: PsiElement, component: String, type: String) {
                holder.registerProblem(
                    element,
                    "[ETYC0372031]: A mapping's $component cannot be a $type",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
            o.mappingTypeList.first().let { key ->
                if (key.namedType?.reference?.resolve() as? LeoMappingDeclaration != null) {
                    registerTYC31Problem(key, "key", "mapping")
                }
                if (key.tupleType != null) {
                    registerTYC31Problem(key, "key", "tuple")
                }
            }
            o.mappingTypeList.last().let { value ->
                if (value.namedType?.reference?.resolve() as? LeoMappingDeclaration != null) {
                    registerTYC31Problem(value, "value", "mapping")
                }
                if (value.tupleType != null) {
                    registerTYC31Problem(value, "value", "tuple")
                }
            }
        }
    }

    override fun visitFinalizer(o: LeoFinalizer) {
        // Type checker #39
        run {
            if ((o.block?.children?.size ?: return@run) == 0) {
                holder.registerProblem(
                    o,
                    "[ETYC0372039]: A finalize block cannot be empty.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
        // Type checker #45
        run {
            val transition = PsiTreeUtil.getParentOfType(o, LeoTransitionDeclaration::class.java) ?: return@run
            val transitionName = transition.name ?: return@run
            val finalizerName = o.name ?: return@run
            if (finalizerName != transitionName) {
                holder.registerProblem(
                    o.identifier!!,
                    "[ETYC0372045]: `finalize` name `$finalizerName` does not match function name `$transitionName`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitSelfExpression(o: LeoSelfExpression) {
        // Type checker #43
        run {
            if (o.identifier.text != "caller") {
                holder.registerProblem(
                    o.identifier,
                    "[ETYC0372043]: The allowed accesses to `self` are `self.caller`.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    private fun traverseBlock(block: LeoBlock): Pair<Boolean, Boolean> {
        var hasReturn = false
        var hasFinalize = false
        block.children.forEach { statement ->
            when (statement) {
                is LeoReturnStatement -> {
                    hasReturn = true
                    if (statement.finalizeLiteral != null) {
                        hasFinalize = true
                    }
                }

                is LeoBlock -> {
                    val (blockHasReturn, blockHasFinalize) = traverseBlock(statement)
                    hasReturn = hasReturn || blockHasReturn
                    hasFinalize = hasFinalize || blockHasFinalize
                }

                is LeoConditionalStatement -> {
                    val (blockHasReturn, blockHasFinalize) = traverseConditionalStatement(statement)
                    hasReturn = hasReturn || blockHasReturn
                    hasFinalize = hasFinalize || blockHasFinalize
                }
            }
        }
        return Pair(hasReturn, hasFinalize)
    }

    private fun traverseConditionalStatement(statement: LeoConditionalStatement): Pair<Boolean, Boolean> {
        val (hasReturn, hasFinalize) = traverseBlock(statement.branch.block ?: return Pair(false, false))
        var elseHasReturn = false
        var elseHasFinalize = false
        if (statement.conditionalStatement != null) {
            val (blockHasReturn, blockHasFinalize) = traverseConditionalStatement(statement.conditionalStatement!!)
            elseHasReturn = blockHasReturn
            elseHasFinalize = blockHasFinalize
        } else if (statement.block != null) {
            val (blockHasReturn, blockHasFinalize) = traverseBlock(statement.block!!)
            elseHasReturn = blockHasReturn
            elseHasFinalize = blockHasFinalize
        }
        return Pair(hasReturn && elseHasReturn, hasFinalize && elseHasFinalize)
    }

    private fun checkNumberRange(number: String, type: String): Boolean {
        val value = number.toBigInteger()
        val lower: BigInteger
        val upper: BigInteger
        val bits = type.substring(1).toInt()
        if (type.contains('u')) {
            lower = BigInteger.ZERO
            upper = BigInteger.TWO.pow(bits) - BigInteger.ONE
        } else {
            lower = BigInteger.TWO.pow(bits - 1).unaryMinus()
            upper = BigInteger.TWO.pow(bits - 1) - BigInteger.ONE
        }
        return value in lower..upper
    }

    private fun checkTYC3(o: PsiElement, type: String, actualType: String) {
        if (actualType != "pass") {
            if (actualType.contains('|')) {
                val actualTypes = actualType.split('|')
                actualTypes.filter { it != type }.forEach { registerTYC3Problem(o, type, it) }
                actualTypes.filter { it == "unknown" }.forEach { _ -> registerTYC4Problem(o) }
            } else if (actualType == "unknown") {
                registerTYC4Problem(o)
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

    private fun registerTYC4Problem(value: PsiElement) {
        holder.registerProblem(
            value,
            "[ETYC0372004]: Could not determine the type of `${value.text}`\n\nThis might be a bug in the language plugin. Please report this issue.",
            ProblemHighlightType.GENERIC_ERROR
        )
    }

    private fun registerTYC7Problem(value: PsiElement, expected: String, actual: String) {
        holder.registerProblem(
            value,
            "[ETYC0372007]: Expected one type from `${expected}`, but got `${actual}`",
            ProblemHighlightType.GENERIC_ERROR
        )
    }

    private fun checkTYC7(o: PsiElement, type: String, actualType: String) {
        if (actualType != "pass") {
            if (actualType.contains('|')) {
                val actualTypes = actualType.split('|')
                actualTypes.filter { it != type }.forEach { registerTYC7Problem(o, type, it) }
            } else if (actualType != type) {
                registerTYC7Problem(o, type, actualType)
            }
        }
    }

    private fun getExpressionType(element: PsiElement): String {
        return when (element) {
            is LeoPrimaryExpression -> getPrimaryExpressionType(element)

            is LeoTupleComponentExpression -> getTupleComponentType(element)
            is LeoStructComponentExpression -> getStructComponentType(element)
            is LeoAssociatedFunctionCall -> "pass" // TODO check types

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
            is LeoAssociatedFunctionCall -> {
                val coreStruct = exp.namedType.coreStruct?.text ?: return "pass"
                val function = exp.identifier.text
                if (coreStruct.contains("Pedersen") && function == "commit") {
                    "group"
                } else {
                    "field"
                }
            }

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

            is LeoVariable -> exp.reference?.resolve()?.let {
                LeoUtils.typeToString(it as? LeoTypedElement ?: return@let "unknown") ?: "unknown"
            } ?: "pass"

            else ->
                if (exp.elementType == LPAREN) {
                    var realExp = exp.nextSibling
                    while (realExp.elementType == WHITE_SPACE) {
                        realExp = realExp.nextSibling
                    }
                    getExpressionType(realExp)
                } else if (exp is LeafPsiElement) {
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
            STRING_LITERAL -> "string"
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
        return "pass"
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
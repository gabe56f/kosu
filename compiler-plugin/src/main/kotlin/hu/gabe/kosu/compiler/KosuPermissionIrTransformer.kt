package hu.gabe.kosu.compiler

import org.jetbrains.kotlin.DeprecatedForRemovalCompilerApi
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irVararg
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrClassReferenceImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.fromSymbolOwner
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.findAnnotation
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

@OptIn(UnsafeDuringIrConstructionAPI::class)
class KosuPermissionIrTransformer(
    private val pluginContext: IrPluginContext,
    private val messageCollector: MessageCollector = MessageCollector.NONE,
) : IrElementTransformerVoidWithContext() {
    private val requiresOauthAnnotation = FqName("hu.gabe.kosu.annotations.RequiresOauth")
    private val throwsConstructor =
        pluginContext.referenceConstructors(ClassId.fromString("kotlin/jvm/Throws")).singleOrNull()

    // TODO: look for a better solution, because this is stupid
    private val illegalStateExceptionClass =
        pluginContext.referenceClass(ClassId.fromString("java/lang/IllegalStateException"))

    @OptIn(DeprecatedForRemovalCompilerApi::class)
    private fun addThrowsAnnotation(declaration: IrFunction) {
        val illegalStateExceptionType = illegalStateExceptionClass!!.defaultType

        val illegalStateException = IrClassReferenceImpl(
            startOffset = declaration.startOffset,
            endOffset = declaration.endOffset,
            type = pluginContext.irBuiltIns.kClassClass.defaultType,
            symbol = illegalStateExceptionClass,
            classType = illegalStateExceptionType
        )

        val throwsAnnotationCall = IrConstructorCallImpl.fromSymbolOwner(
            type = throwsConstructor!!.owner.returnType,
            constructorSymbol = throwsConstructor
        ).apply {
            val varargArgument = DeclarationIrBuilder(pluginContext, declaration.symbol).irVararg(
                throwsConstructor.owner.valueParameters[0].varargElementType!!,
                listOf(illegalStateException)
            )
            putValueArgument(0, varargArgument)
        }

        declaration.annotations += throwsAnnotationCall
    }

    private fun findCanExecuteFunction(declaration: IrFunction): IrSimpleFunctionSymbol? {
        // Find the `canExecute` function on the parent class
        val parentClass = declaration.parent as? IrClass ?: return null
        return parentClass.functions.find { it.name.asString() == "canExecute" }?.symbol
    }

    @OptIn(DeprecatedForRemovalCompilerApi::class)
    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        val annotation = declaration.annotations.findAnnotation(requiresOauthAnnotation)

        annotation ?: return super.visitFunctionNew(declaration)

        val canExecuteFunction = findCanExecuteFunction(declaration)
        if (canExecuteFunction == null) {
            messageCollector.report(
                CompilerMessageSeverity.ERROR,
                "Function ${declaration.name.asString()} not inside a Class that extends BaseRequestV2. Cannot check if Kosu is authorized."
            )
            return super.visitFunctionNew(declaration)
        }
        addThrowsAnnotation(declaration)

        val newBody = DeclarationIrBuilder(pluginContext, declaration.symbol).irBlockBody {
            val requiredScopes = annotation.getValueArgument(0)

            val call = irCall(canExecuteFunction).apply {
                dispatchReceiver = irGet(declaration.dispatchReceiverParameter!!)
                if (requiredScopes != null) {
                    putValueArgument(0, requiredScopes)
                }
            }
            +call

            declaration.body?.statements?.forEach { +it }
        }
        declaration.body = newBody

        return super.visitFunctionNew(declaration)
    }
}
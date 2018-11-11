package scalaparse.unit

import scalaparse.TestUtil
import utest._
import TestUtil._
object FailureTests extends TestSuite{
  val tests = Tests {

    * - checkNeg(
      "package package",
      aggregate = """(Id | PkgBlock | PkgObj)""",
      terminals = """("`" | var-id | chars-while(OpCharNotSlash, 1) | "/" | operator | plain-id | id | "case" | "object")""",
      found = "package"
    )

    * - checkNeg(
      """package torimatomeru
        |import a
        |import import
      """.stripMargin,
      aggregate = """(ThisPath | IdPath)""",
      terminals = """("this" | "super" | "`" | var-id | chars-while(OpCharNotSlash, 1) | "/" | operator | plain-id | id)""",
      found = "import"
    )

    * - checkNeg(
      """object O{
        |  for{
        |    x <- Nil
        |    if 1 ==
        |
        |    2
        |  } yield x
        |}
      """.stripMargin,
      aggregate = """(Id | Generator | Assign)""",
      terminals = """("`" | char-pred(UpperChar) | char-pred(LowerChar) | var-id | chars-while(OpCharNotSlash, 1) | "/" | operator | plain-id | id | "<-" | "←" | "=")""",
      found = "} yield x"
    )
    * - checkNeg(
      """object O{
        |  type T = (A B)
        |}
      """.stripMargin,
      aggregate = """(NamedType | Refinement)""",
      terminals = """(chars-while(IdCharacter, 1) | [_] | [ \t] | "/*" | "//" | "(" | "-" | "." | [0-9] | "0x" | "true" | "false" | "`" | char-pred(UpperChar) | char-pred(LowerChar) | var-id | chars-while(OpCharNotSlash, 1) | "/" | operator | plain-id | id | "\"\"\"" | "\"" | "'" | "null" | "this" | "super" | "_" | "{")""",
      found = ")"
    )
    * - checkNeg(
      """object O{
        | def from(n: Long, c: Int = 0): Int =
        |  if (n == 1) c + 1 else
        |}
      """.stripMargin,
      aggregate = """(If | While | Try | DoWhile | For | Throw | Return | ImplicitLambda | SmallerExprOrLambda)""",
      terminals = null,
      found = ""
    )
    * - checkNeg(
      "import scala.util.{Failure, Success + 1}",
      aggregate = """("=>" | "," | "}")""",
      terminals = null,
      found = "+ 1}"
    )
    * - checkNeg(
      """
        |object SyntaxTest extends TestSuite{
        |  def check[T]](input: String) = {
        |
        |  }
        |}
      """.stripMargin,
      aggregate = """ (FunTypeArgs | FunArgs | `:` | Body | Semis | "}")""",
      terminals = null,
      found = "](input: S"
    )
    * - checkNeg(
      """
        |object SyntaxTest{
        |  a(
        |  throw 1
        |}
      """.stripMargin,
      aggregate = """(WL ~ "." | ParenArgList | OneNLMax ~ BlockExpr | SuperPostfixSuffix | `:` | "," | ")")""",
      terminals = null,
      found ="}"
    )
    * - checkNeg(
      """
        |object SyntaxTest {
        |  {
        |    thro   1
        |  }
        |}
      """.stripMargin,
      aggregate = """("=>" | `:` | "." | WL ~ "." | ParenArgList | OneNLMax ~ BlockExpr | SuperPostfixSuffix | Semi | Semis | "}")""",
      terminals = null,
      found ="1\n"
    )
    * - checkNeg(
      """
        |object Moo{
        |  a
        |
        |  .
        |
        |  .
        |}
      """.stripMargin,
      aggregate = """(`this` | Id)""",
      terminals = null,
      found = "."
    )
    * - checkNeg(
      """
        |object Moo{
        | filename.asInstanceOf 10
        |}
      """.stripMargin,
      aggregate = """("." | WL ~ "." | ParenArgList | OneNLMax ~ BlockExpr | SuperPostfixSuffix | Semi | Semis | "}")""",
      terminals = null,
      found = "10"
    )
    * - checkNeg(
      """
        |object C o w{
        |  ().mkString
        |
        |  1
        |}
      """.stripMargin,
      aggregate = """(DefTmpl | Semi | end-of-input)""",
      terminals = null,
      found = "o w{"
    )
    * - checkNeg(
      """
        |object O{
        | private[this] applyMacroFull = 1
        |}
      """.stripMargin,
      aggregate = """(LocalMod | AccessMod | `override` | Dcl | TraitDef | ClsDef | ObjDef)""",
      terminals = null,
      found = "applyM"
    )
    * - checkNeg(
      """
        |object O{
        | private[this] def applyMacroFull(c: Context)
        |                      (expr: c.Expr[String],
        |                       runtimeErrors: Boolean,
        |                       debug: Boolean)
        |                      :  = {
        |                      }
        |}
      """.stripMargin,
      aggregate = """("=>" | NamedType | Refinement)""",
      terminals = null,
      found = "= {"
    )
    * - checkNeg(
      """
        |object O{
        |  class 1 extends Exception
        |
        |  1
        |}
      """.stripMargin,
      aggregate = """id""",
      terminals = null,
      found = "1 extends"
    )
    * - checkNeg(
      """
        |package torimatomeru
        |
        |package syntax
        |
        |import org.parboiled2 _
        |
      """.stripMargin,
      aggregate = """("." | Semi | end-of-input)""",
      terminals = null,
      found = "_"
    )
    * - checkNeg(
      """
        |object Foo{
        |  0 match {
        |    case A B => 0
        |  }
        |}
      """.stripMargin,
      aggregate = """(XmlPattern | Thingy | PatLiteral | TupleEx | Extractor | VarId)""",
      terminals = null,
      found = "=> 0"
    )
    * - checkNeg(
      """
        |object Compiler{
        |
        |  def apply = {
        |    def rec = t match
        |      case 0 => 0
        |    }
        |
        |    rec(tree)
        |  }
        |}
        |
      """.stripMargin,
      aggregate = """ "{" """,
      terminals = null,
      found = "case 0 =>"
    )
    * - checkNeg(
      """
        |object O {
        |    A A(A(A(A(A(A(A())))))))
        |}
        |
      """.stripMargin,
      aggregate = """(WL ~ "." | WL ~ TypeArgs | NotNewline ~ ArgList | NotNewline ~ `_` | InfixSuffix | PostFix | SuperPostfixSuffix | Semis | "}")""",
      terminals = null,
      found = ")"
    )
    * - checkNeg(
      """
        |object O{
        |   A(A(A(A(A(A(A(A(A(A(A(A(A(A(A(A()))))))))))))))
        |}
      """.stripMargin,
      aggregate = """(WL ~ "." | ParenArgList | OneNLMax ~ BlockExpr | SuperPostfixSuffix | `:` | "," | ")")""",
      terminals = null,
      found = "}"
    )
    * - checkNeg(
      """
        |object L{
        |  a.b =
        |}
      """.stripMargin,
      aggregate = """(If | While | Try | DoWhile | For | Throw | Return | ImplicitLambda | SmallerExprOrLambda)""",
      terminals = null,
      found = "}"
    )
    * - checkNeg(
      """
        |object L{
        |  a b c
        |  d = 1
        |
      """.stripMargin,
      aggregate = """(SuperPostfixSuffix | Semis | "}")""",
      terminals = null,
      found = ""
    )
    * - checkNeg(
      """/*
        |
        |package scala.scalajs.cli
        |
      """.stripMargin,
      aggregate = """ "*/" """,
      terminals = null,
      found = ""
    )
    * - checkNeg(
      """/*/*
        |*/
        |package scala.scalajs.cli
        |
      """.stripMargin,
      aggregate = """ "*/" """,
      terminals = null,
      found = ""
    )
    * - checkNeg(
      """
        |object O{
        |  for {
        |      a  <- b
        |      c <- d
        |  }
        |}
      """.stripMargin,
      aggregate = """(`yield` | If | While | Try | DoWhile | For | Throw | Return | ImplicitLambda | SmallerExprOrLambda)""",
      terminals = null,
      found = "}"
    )
    * - checkNeg(
      """
        |object O{
        |  val jarFile = catch { case _: F => G }
        |}
      """.stripMargin,
      aggregate = """(If | While | Try | DoWhile | For | Throw | Return | ImplicitLambda | SmallerExprOrLambda)""",
      terminals = null,
      found = "catch {"
    )
    * - checkNeg(
      """
        |object F{
        |  func{ case _: F = fail }
        |}
      """.stripMargin,
      aggregate = """("." | TypeArgs | `#` | Guard | "=>")""",
      terminals = null,
      found = "= fail"
    )
    * - checkNeg(
      """
        |object Foo{
        |    val a d // g
        |    val b = e // h
        |    val c = f
        |}
      """.stripMargin,
      aggregate = """(XmlPattern | Thingy | PatLiteral | TupleEx | Extractor | VarId)""",
      terminals = null,
      found = "val b = e"
    )
    * - checkNeg(
      """
        |object L{
        |  x match{
        |    case y.Y(z => z
        |  }
        |}
      """.stripMargin,
      aggregate = """(TypePattern | Binding | "." | TypeArgs | TupleEx | Id | "," | ")")""",
      terminals = null,
      found = "=> z"
    )
    * - checkNeg(
      """object K{
        |  val a:
        |    val c: D
        |  }
        |
        |  1
        |}
      """.stripMargin,
      aggregate = """("=>" | NamedType | Refinement)""",
      terminals = null,
      found = "val c"
    )
    * - checkNeg(
      """
        |object LOLS{
        |  def run(def apply() {}) {}
        |}
      """.stripMargin,
      aggregate = """(`implicit` | Args | ")")""",
      terminals = null,
      found = "def apply"
    )
    * - checkNeg(
      """
        |object O{
        |  a =:= .c
        |}
      """.stripMargin,
      aggregate = """(TypeArgs | ExprPrefix | XmlExpr | New | BlockExpr | ExprLiteral | StableId | `_` | Parened | Newline | SuperPostfixSuffix | Semi | Semis | "}")""",
      terminals = null,
      found = ".c"
    )
    * - checkNeg(
      """
        |object K{
        |  a(
        |    1:
        |  )
        |}
      """.stripMargin,
      aggregate = """(_* | AscriptionType | Annot.rep(1))""",
      terminals = null,
      found = ")\n}"
    )
    * - checkNeg(
      """
        |object K{
        |  a[)
        |}
      """.stripMargin,
      aggregate = """(Type | "," | "]")""",
      terminals = null,
      found = ")"
    )
    * - checkNeg(
      """
        |object K{
        |  a[b)
        |}
      """.stripMargin,
      aggregate = """(id ~ TQ | "." | TypeArgs | `#` | NLAnnot | `with` | Refinement | NotNewline ~ (`*` | Id) | "=>" | ExistentialClause | `>:` | `<:` | `*` | "," | "]")""",
      terminals = null,
      found = ")"
    )
    * - checkNeg(
      """
        |object P{
        |  tree match {
        |    stats :+ expr  => 1
        |  }
        |}
      """.stripMargin,
      aggregate = """ "case" """,
      terminals = null,
      found = "stats :+ e"
    )

    * - checkNeg(
      """
        |object K
        |  val trueA = 1
        |}
      """.stripMargin,
      aggregate = """(DefTmpl | ";" | Newline.rep(1) | Pkg | Import | Tmpl | Semis | end-of-input)""",
      terminals = null,
      found = "val trueA"
    )
    * - checkNeg(
      """
        |object K{
        |  val null null cow = 1
        |}
      """.stripMargin,
      aggregate = """(Id | `:` | `=` | Semi | Semis | "}")""",
      terminals = null,
      found = "null cow"
    )
    * - checkNeg(
      """
        |object K{
        |  val omg_+_+ = 1
        |}
      """.stripMargin,
      aggregate = """(Binding | id ~ TQ | "." | TypeArgs | TupleEx | Id | "," | `:` | `=` | Semis | "}")""",
      terminals = null,
      found = "_+ = 1"
    )
    * - checkNeg(
      """
        |object K{
        |  val + = 1
        |  var = 2
        |}
      """.stripMargin,
      aggregate = """(Binding | InfixPattern | VarId)""",
      terminals = null,
      found = "= 2"
    )
    * - checkNeg(
      """
        |object O{
        |   {
        |    case b |  => 1
        |  }
        |}
      """.stripMargin,
      aggregate = """(XmlPattern | Thingy | PatLiteral | TupleEx | Extractor | VarId)""",
      terminals = null,
      found = "=> 1"
    )
    * - checkNeg(
      """
        |trait Basic {
        |  b match {
        |    case C case _ => false
        |  }
        |}
      """.stripMargin,
      aggregate = """(Binding | "." | TypeArgs | TupleEx | Id | Guard | "=>")""",
      terminals = null,
      found = "case"
    )
    * - checkNeg(
      """trait Basic{
        |  a!.b
        |}
      """.stripMargin,
      aggregate = """(TypeArgs | ExprPrefix | XmlExpr | New | BlockExpr | ExprLiteral | StableId | `_` | Parened | PostFix | SuperPostfixSuffix | Semis | "}")""",
      terminals = null,
      found = ".b"
    )
    * - checkNeg(
      """
        |class Parser {
        |  {( => }
        |}
        |
      """.stripMargin,
      aggregate = """("(" ~ BlockLambdaHead | Expr | "," | ")")""",
      terminals = null,
      found = "=> }"
    )
    * - checkNeg(
      """
        |class Parser([
        |
      """.stripMargin,
      aggregate = """(`implicit` | ClsArg | "," | ")")""",
      terminals = null,
      found = "["
    )
    * - checkNeg(
      """
        |class Parser{
        | @mog
        |}
      """.stripMargin,
      aggregate = """("." | TypeArgs | `#` | "(" | Mod | Dcl | TraitDef | ClsDef | ObjDef)""",
      terminals = null,
      found = "}"
    )
    * - checkNeg(
      """class C
        |package omg
        |;
      """.stripMargin,
      aggregate = """ "{" """,
      terminals = null,
      found = ";"
    )

    * - checkNeg(
      """object B {
        |  { a: L = }
        |}
      """.stripMargin,
      aggregate = """("." | TypeArgs | `#` | `*` | Id | "=>" | BlockLambda | BlockStat | Semis | "}")""",
      terminals = null,
      found = "= }"
    )
      * - checkNeg(
        """object GenJSCode{
          |  a.b.()
          |}
          |
        """.stripMargin,
        aggregate = """(`this` | Id)""",
      terminals = null,
        found = "()"
      )
    * - checkNeg(
      """object GenJSCode{
        |  this.this.()
        |}
        |
      """.stripMargin,
      aggregate = """id""",
      terminals = null,
      found = "this"
    )
    * - checkNeg(
      """object GenJSCode{
        |  null: b.()
        |}
        |
      """.stripMargin,
      aggregate = """(`this` | Id)""",
      terminals = null,
      found = "()"
    )
      * - checkNeg(
        """object K{
          |  private O
          |}
        """.stripMargin,
        aggregate = """(AccessQualifier | LocalMod | AccessMod | `override` | Dcl | TraitDef | ClsDef | ObjDef)""",
      terminals = null,
        found = "O\n"
      )
      * - checkNeg(
        """object O{
          |  if eqeq &&
          |
          |    false  1
          |}
        """.stripMargin,
        aggregate = """ "(" """,
      terminals = null,
        found = "eqeq"
      )
      * - checkNeg(
        """
          |object O{
          |  for{
          |    x <- Nil map
          |
          |    (x => x)
          |  } yield x
          |}
        """.stripMargin,
        aggregate = """(TypePattern | Binding | "." | TypeArgs | TupleEx | Id | "," | ")")""",
      terminals = null,
        found = "=> x)"
      )
    * - checkNeg(
      """
        |object O{
        |  for{
        |    x <- Nil
        |    println(1)
        |  } yield x
        |}
      """.stripMargin,
      aggregate = """(Id | Generator | Assign)""",
      terminals = null,
      found = "} yield"
    )
    * - checkNeg(
      """
        |object O{
        |  for{
        |    x <- Nil
        |    {
        |  } yield x
        |}
      """.stripMargin,
      aggregate = """("." | WL ~ "." | "=>" | SuperPostfixSuffix | Guard | CuttingSemis ~ (GenAssign | Guard) | "}")""",
      terminals = null,
      found = "{\n"
    )
    * - checkNeg(
      """
        |object O{
        |  for{
        |    x <- Nil
        |    if
        |
        |    1 == 2
        |  } yield
        |}
      """.stripMargin,
      aggregate = """(If | While | Try | DoWhile | For | Throw | Return | ImplicitLambda | SmallerExprOrLambda)""",
      terminals = null,
      found = "}"
    )
    * - checkNeg(
      """
        |object ScopedVar {
        |  def withScopedVars(ass: Seq[_, ]) = 1
        |}
        |
      """.stripMargin,
      aggregate = """(TypeArgs | `#` | NLAnnot | `with` | Refinement | NotNewline ~ (`*` | Id) | "=>" | ExistentialClause | `>:` | `<:` | `*` | "," ~ Type | "," ~ WS ~ Newline | "]")""",
      terminals = null,
      found = ", ]"
    )
    * - checkNeg(
      """
        |abstract class JSASTTest extends DirectTest {
        |  def show: this.type = )
        |}
        |
      """.stripMargin,
      aggregate = """(`macro` | If | While | Try | DoWhile | For | Throw | Return | ImplicitLambda | SmallerExprOrLambda)""",
      terminals = null,
      found = ")\n"

    )
    * - checkNeg(
      """object Traversers {
        |  {
        |        1
        |        case foreach nil
        |  }
        |}
      """.stripMargin,
      aggregate = """(WL ~ "." | SuperPostfixSuffix | Semi | ";" | Newline.rep(1) | BlockLambda | BlockStat | Semis | "}")""",
      terminals = null,
      found = "case for"
    )
    * - checkNeg(
      """object Utils {
        |  "\q"
        |}
        |
      """.stripMargin,
      aggregate = """([btnfr'\\\\\"]] | OctalEscape | UnicodeEscape)""",
      terminals = null,
      found = "q"
    )
    * - checkNeg(
      """object Utils {
        |  "
        |  1
        |  "
        |}
        |
      """.stripMargin,
      aggregate = """(StringChars | Interp | LiteralSlash | Escape | NonStringEnd | "\"")""",
      terminals = null,
      found = "\n"
    )
    val tq = "\"\"\""
    * - checkNeg(
      s"""
        |object Utils {
        |  $tq
        |  1
        |  "
        |}
        |
      """.stripMargin,
      aggregate = """(StringChars | Interp | NonTripleQuoteChar | "\"\"\"")""",
      terminals = null,
      found = ""
    )
    * - checkNeg(
      """
        |object X{
        |  ''
        |}
        |
      """.stripMargin,
      aggregate = """(Escape | Symbol)""",
      terminals = null,
      found = "'\n"
    )

    // These two guys pass in Scalac, but I'm not gonna support it
    * - checkNeg(
      """
        |object X{
        |  (1.)
        |}
        |
      """.stripMargin,
      aggregate = """("." ~ Thing | id)""",
      terminals = null,
      found = ")"
    )
    * - checkNeg(
      s"""
         |object X{
         |  1..toString
         |}
      """.stripMargin,
      aggregate = """("." ~ Thing | id)""",
      terminals = null,
      found = ".toString"
    )
    * - checkNeg(
      s"""
         |object X{
         |  val x, = 1
         |}
      """.stripMargin,
      aggregate = """(Binding | InfixPattern | VarId)""",
      terminals = null,
      found = "= 1"
    )
  * - checkNeg(
    s"""
       |object X{
       |  val (x,) = 1
       |}
      """.stripMargin,
    aggregate = """(TypePattern | Binding | id ~ TQ | "." | TypeArgs | TupleEx | Id | "|" | "," ~ Pattern | "," ~ WS ~ Newline | ")")""",
      terminals = null,
    found = ",)"
  )
  * - checkNeg(
    s"""
       |object X{ val (_:) = 1 }
      """.stripMargin,
    aggregate = """(TypePattern | NamedType | Refinement)""",
      terminals = null,
    found = ") = 1"
  )
  * - checkNeg(
    s"""
       |import x.{y=>}
      """.stripMargin,
    aggregate = """(Id | `_`)""",
      terminals = null,
    found = "}"
  )
  * - checkNeg(
    s"""
       |import x.y,
      """.stripMargin,
    aggregate = """(ThisPath | IdPath)""",
      terminals = null,
    found = ""
  )
  * - checkNeg(
    s"""
       |object X{type T = A with}
      """.stripMargin,
    aggregate = """(TupleType | Literal | TypeId | `_`)""",
      terminals = null,
    found = "}"
  )
  * - checkNeg(
    s"""
       |object X{def f(x: Int, ) = 1}
      """.stripMargin,
    aggregate = """(Args | ")")""",
      terminals = null,
    found = ", )"
  )
  * - checkNeg(
    s"""
       |object X{val x = (1, 2,)}
      """.stripMargin,
    aggregate = """("." | Exp | "L" | "l" | WL ~ "." | WL ~ TypeArgs | Pass ~ ArgList | Pass ~ `_` | InfixSuffix | PostFix | "=>" | SuperPostfixSuffix | "," ~ Expr | "," ~ WS ~ Newline | ")")""",
      terminals = null,
    found = ",)"
  )
  * - checkNeg(
    s"""
       |object X{f[A,]}
      """.stripMargin,
    aggregate = """(id ~ TQ | "." | TypeArgs | `#` | NLAnnot | `with` | Refinement | NotNewline ~ (`*` | Id) | "=>" | ExistentialClause | `>:` | `<:` | `*` | "," ~ Type | "," ~ WS ~ Newline | "]")""",
      terminals = null,
    found = ",]"
  )
  * - checkNeg(
    s"""
       |object X{def f[T <% A <%] = 1}
      """.stripMargin,
    aggregate = """("=>" | NamedType | Refinement)""",
      terminals = null,
    found = "]"
  )
  * - checkNeg(
    s"""
       |object X{def f[T, B,] = 1}
      """.stripMargin,
    aggregate = """(TypeArgList | `>:` | `<:` | `<%` | `:` | "," ~ Annot.rep ~ TypeArg | "," ~ WS ~ Newline | "]")""",
      terminals = null,
    found = ",]"
  )
  * - checkNeg(
    s"""
       |object X{type T = F forSome }}
      """.stripMargin,
    aggregate = """ "{" """,
      terminals = null,
    found = "}}"
  )

  * - checkNeg(
    s"""
       |object X{def f(x: Int =) = 1}
      """.stripMargin,
    aggregate = """(If | While | Try | DoWhile | For | Throw | Return | ImplicitLambda | SmallerExprOrLambda)""",
      terminals = null,
    found = ") = 1"
  )
  * - checkNeg(
    s"""
       |object X{type T = Int#}
      """.stripMargin,
    aggregate = """id""",
      terminals = null,
    found = "}"
  )
  * - checkNeg(
    s"""
       |object X{type T = }
      """.stripMargin,
    aggregate = """("=>" | NamedType | Refinement)""",
      terminals = null,
    found = "}\n"
  )
  * - checkNeg(
    s"""
       |object X{type T[,] = A }
      """.stripMargin,
    aggregate = """(Annot | [+\\-] | Id | `_`)""",
      terminals = null,
    found = ",]"
  )
  * - checkNeg(
    s"""
       |object X{type T <: }
      """.stripMargin,
    aggregate = """("=>" | NamedType | Refinement)""",
      terminals = null,
    found = "}\n"
  )
    * - checkNeg(
      """
        |object System {
        |  def a[@b T @f [@b V]] = 1
        |}
        |
      """.stripMargin,
      aggregate = """(TypeArgList | `>:` | `<:` | `<%` | `:` | "," | "]")""",
      terminals = null,
      found = "@f"
    )
    * - checkNeg(
      """
        |@func(} object System {
        |
        |}
        |
      """.stripMargin,
      aggregate = """(Exprs | "," | ")")""",
      terminals = null,
      found = "}"
    )
    * - checkNeg(
      """
        |object System {
        |  def f[[a] = 1
        |}
        |
      """.stripMargin,
      aggregate = """(Annot | Id | `_`)""",
      terminals = null,
      found = "["
    )

    * - checkNeg(
      s"""
        |object System {
        |  $tq """".stripMargin,
      aggregate = """("\"" | StringChars | Interp | NonTripleQuoteChar | "\"\"\"")""",
      terminals = null,
      found = ""
    )
    * - checkNeg(
      """
        |object System {
        |  e match { case <xml:unparsed><</xml:unparsed> => }
        |}
        |
      """.stripMargin,
      aggregate = """(CharDataP | ScalaPatterns | ElemPattern | "</")""",
      terminals = null,
      found = "<</xml:unp"
    )

    * - checkNeg(
      s"""object Foo{
         |  for(i <- Nil if x: Int => bar) 1
         |}
       """.stripMargin,
      aggregate = """(Guard | Enumerator | ")")""",
      terminals = null,
      found = ": Int"
    )
    * - checkNeg(
      s"""object Foo{; x: Int => x}""",
      aggregate = """("." | TypeArgs | `#` | `*` | Id | Semi | Semis | "}")""",
      terminals = null,
      found = "=> x"
    )
    * - checkNeg(
      s"""object Foo{ (i: Int => +i) }""",
      aggregate = """(NamedType | Refinement)""",
      terminals = null,
      found = ")"
    )
    * - checkNeg(
      """
        |object X {
        |  for{
        |    a <- List(1)
        |    ;
        |  } yield a
        |}""".stripMargin,
      aggregate = """(GenAssign | Guard)""",
      terminals = null,
      found = "} yield a"
    )
    * - checkNeg(
      """
        |object X {
        |  {
        |    val x = 1
        |    ;
        |    """.stripMargin,
      aggregate = """(BlockLambda | BlockStat | Semis | "}")""",
      terminals = null,
      found = ""
    )

  }
}

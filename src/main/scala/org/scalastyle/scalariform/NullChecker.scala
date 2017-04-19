// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.scalastyle.scalariform

import org.scalastyle.PositionError
import org.scalastyle.ScalariformChecker
import org.scalastyle.ScalastyleError

import _root_.scalariform.lexer.Token
import _root_.scalariform.lexer.Tokens
import _root_.scalariform.lexer.TokenType
import _root_.scalariform.parser.CompilationUnit
import _root_.scalariform.parser.ForExpr

class NullChecker extends ScalariformChecker {
  val errorKey = "null"
  val dummyToken = new Token(Tokens.NULL, "null", 0, "null")

  val defaultAllowNullChecks = true
  lazy val allowNullChecks = getBoolean("allowNullChecks", defaultAllowNullChecks)

  final def verify(ast: CompilationUnit): List[ScalastyleError] = {
    val tokenTypes: List[TokenType] = ast.tokens.map(_.tokenType)

    val it = for {
      (t, prev) <- ast.tokens.zip(dummyToken :: ast.tokens)
      if t.tokenType == Tokens.NULL
      if !(allowNullChecks && prev.tokenType == Tokens.VARID && (prev.text == "==" || prev.text == "!="))
    } yield {
      PositionError(t.offset)
    }

    it
  }

}

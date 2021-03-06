/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package idea.plugin.protoeditor.ide.spelling;

import com.intellij.psi.PsiElement;
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.Tokenizer;
import idea.plugin.protoeditor.lang.psi.PbOptionExpression;
import idea.plugin.protoeditor.lang.psi.PbTextStringPart;
import idea.plugin.protoeditor.lang.psi.ProtoStringPart;
import org.jetbrains.annotations.NotNull;

/** A {@link SpellcheckingStrategy} for protobuf files */
public class PbSpellcheckingStrategy extends SpellcheckingStrategy {
  @NotNull
  @Override
  public Tokenizer<?> getTokenizer(PsiElement element) {
    if (element instanceof ProtoStringPart) {
      if (element instanceof PbTextStringPart) {
        return StringPartTokenizer.INSTANCE;
      } else if (isOptionValue(element)) {
        return StringPartTokenizer.INSTANCE;
      } else {
        // Don't enable spell checking for non-option strings (such as import paths)
        return EMPTY_TOKENIZER;
      }
    }
    return super.getTokenizer(element);
  }

  private static boolean isOptionValue(PsiElement part) {
    // PbStringPart -> PbStringValue -> PbOptionExpression
    PsiElement parent = part.getParent();
    return parent != null && parent.getParent() instanceof PbOptionExpression;
  }
}

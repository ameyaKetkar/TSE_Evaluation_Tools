/* LanguageTool, a natural language style checker 
 * Copyright (C) 2012 Marcin Miłkowski (http://www.languagetool.org)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */

package org.languagetool.rules.uk;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.languagetool.AnalyzedToken;
import org.languagetool.AnalyzedTokenReadings;
import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.rules.spelling.morfologik.MorfologikSpellerRule;
import org.languagetool.tagging.uk.IPOSTag;

public final class MorfologikUkrainianSpellerRule extends MorfologikSpellerRule {

  private static final String ABBREVIATION_CHAR = ".";
  private static final String RESOURCE_FILENAME = "/uk/hunspell/uk_UA.dict";
  private static final Pattern UKRAINIAN_LETTERS = Pattern.compile(".*[а-�?іїєґ�?-ЯІЇЄ�?].*");

  public MorfologikUkrainianSpellerRule(ResourceBundle messages,
                                        Language language) throws IOException {
    super(messages, language);
//    setCheckCompound(true);
  }

  @Override
  public String getFileName() {
    return RESOURCE_FILENAME;
  }

  @Override
  public String getId() {
    return "MORFOLOGIK_RULE_UK_UA";
  }

  @Override
  protected boolean ignoreToken(AnalyzedTokenReadings[] tokens, int idx) throws IOException {
    String word = tokens[idx].getToken();

    // don't check words that don't have Ukrainian letters
    if( ! UKRAINIAN_LETTERS.matcher(word).matches() )
      return true;

    if( super.ignoreToken(tokens, idx) )
      return true;

    if( idx < tokens.length - 1 && tokens[idx+1].getToken().equals(ABBREVIATION_CHAR) ) {
      if( super.ignoreWord(word + ABBREVIATION_CHAR) ) {
        return true;
      }
      if( word.matches("[�?-ЯІЇЄ�?]") ) {  //TODO: only do this for initials when last name is followed
        return true;
      }
    }
    
    if( word.contains("-") || word.endsWith(".") ) {
      return hasGoodTag(tokens[idx]);
    }

    return false;
  }

  private boolean hasGoodTag(AnalyzedTokenReadings tokens) {
    for (AnalyzedToken analyzedToken : tokens) {
      String posTag = analyzedToken.getPOSTag();
      if( posTag != null 
            && ! posTag.equals(JLanguageTool.SENTENCE_START_TAGNAME) 
            && ! posTag.equals(JLanguageTool.SENTENCE_END_TAGNAME) 
            && ! posTag.contains(IPOSTag.bad.getText()) )
        return true;
    }
    return false;
  }


}

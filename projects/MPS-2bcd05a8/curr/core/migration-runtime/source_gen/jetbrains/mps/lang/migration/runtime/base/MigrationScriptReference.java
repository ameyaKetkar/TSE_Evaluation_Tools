package jetbrains.mps.lang.migration.runtime.base;

/*Generated by MPS */

import org.jetbrains.mps.openapi.language.SLanguage;
import jetbrains.mps.persistence.IdHelper;
import jetbrains.mps.smodel.adapter.structure.MetaAdapterFactory;
import jetbrains.mps.smodel.adapter.ids.SLanguageId;

public class MigrationScriptReference {
  private final SLanguage language;
  private final int fromVersion;
  public MigrationScriptReference(SLanguage language, int fromVersion) {
    this.language = language;
    this.fromVersion = fromVersion;
  }
  public SLanguage getLanguage() {
    return language;
  }
  public int getFromVersion() {
    return fromVersion;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    MigrationScriptReference that = (MigrationScriptReference) o;
    if (!(language.equals(that.language))) {
      return false;
    }
    if (fromVersion != that.fromVersion) {
      return false;
    }
    return true;
  }
  @Override
  public int hashCode() {
    return language.hashCode() + 31 * fromVersion;
  }
  public String serialize() {
    return IdHelper.getLanguageId(language).serialize() + "(" + language.getQualifiedName() + ")" + "/" + fromVersion;
  }
  public static MigrationScriptReference deserialize(String s) {
    int version = Integer.parseInt(s.substring(s.indexOf('/') + 1));
    int ix = s.indexOf('(');
    SLanguage language = MetaAdapterFactory.getLanguage(SLanguageId.deserialize(s.substring(0, ix)), s.substring(ix + 1, s.indexOf(')', ix)));
    return new MigrationScriptReference(language, version);
  }
}

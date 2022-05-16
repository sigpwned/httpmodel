package com.sigpwned.httpmodel.entity;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

public class ModelHttpFormDataTest {
  @Test
  public void shouldConvertFromStringProperly() {
    ModelHttpFormData entity = ModelHttpFormData.fromString("alpha=bravo%21&charlie=delta%20echo");
    assertThat(entity, is(ModelHttpFormData.of(asList(ModelHttpFormData.Entry.of("alpha", "bravo!"),
        ModelHttpFormData.Entry.of("charlie", "delta echo")))));
  }

  @Test
  public void shouldConvertToStringProperly() {
    String string = ModelHttpFormData.of(asList(ModelHttpFormData.Entry.of("alpha", "bravo!"),
        ModelHttpFormData.Entry.of("charlie", "delta echo"))).toString();
    assertThat(string, is("alpha=bravo%21&charlie=delta%20echo"));
  }
}

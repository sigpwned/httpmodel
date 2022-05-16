/*-
 * =================================LICENSE_START==================================
 * httpmodel-core
 * ====================================SECTION=====================================
 * Copyright (C) 2022 Andy Boothe
 * ====================================SECTION=====================================
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
 * ==================================LICENSE_END===================================
 */
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

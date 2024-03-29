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
package com.sigpwned.httpmodel;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import com.sigpwned.httpmodel.core.model.ModelHttpQueryString;

public class ModelHttpQueryStringTest {
  @Test
  public void shouldEncodeProperly() {
    final ModelHttpQueryString queryString =
        ModelHttpQueryString.of(asList(ModelHttpQueryString.Parameter.of("alpha", "bravo!"),
            ModelHttpQueryString.Parameter.of("charlie", "delta echo")));

    assertThat(queryString.toString(), is("alpha=bravo%21&charlie=delta%20echo"));
  }
}

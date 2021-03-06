// Copyright 2013 The Apache Software Foundation
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

package org.apache.tapestry5.corelib.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;

/**
 * Renders a {@code <span>} tag with the CSS class to select a <a href="http://getbootstrap.com/components/#glyphicons">Bootstrap Glyphicon</a>.
 *
 * @since 5.4
 * @tapestrydoc
 */
public class Glyphicon
{
    /**
     * The name of the icon, e.g., "arrow-up", "flag", "fire" etc.
     */
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    String name;

    boolean beginRender(MarkupWriter writer)
    {
        writer.element("span",
                "class", "glyphicon glyphicon-" + name);
        writer.end();

        return false;
    }
}

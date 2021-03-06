//  Copyright 2008 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5;

/**
 * A wrapper around {@link org.apache.tapestry5.Translator} that combines the translator for a specific {@link
 * org.apache.tapestry5.Field} and (sometimes) an override of the default validation message (used when an input value
 * can't be parsed).
 */
public interface FieldTranslator<T>
{
    /**
     * Returns the type of the server-side value.
     *
     * @return a type
     */
    Class<T> getType();

    /**
     * Invoked after the client-submitted value has been {@link Translator translated} to check that the value conforms
     * to expectations (often, in terms of minimum or maximum value). If and only if the value is approved by all
     * Validators is the value applied by the field.
     *
     * @throws ValidationException if the value violates the constraint
     */
    T parse(String input) throws ValidationException;

    /**
     * Converts a server-side value to a client-side string. This allows for formatting of the value in a way
     * appropriate to the end user.
     *
     * @param value the server side value (which will not be null)
     * @return client-side value to present to the user
     * @see Translator#toClient(Object)
     */
    String toClient(T value);

    /**
     * Invokes {@link Translator#render(Field, String, MarkupWriter,org.apache.tapestry5.services.FormSupport)}. This is
     * called at a point "inside" the tag, so that additional attributes may be added.  In many cases, the underlying
     * {@link org.apache.tapestry5.Validator} may write client-side JavaScript to enforce the constraint as well.
     *
     * @param writer markup writer to direct output to.
     * @see org.apache.tapestry5.MarkupWriter#attributes(Object[])
     */
    void render(MarkupWriter writer);
}
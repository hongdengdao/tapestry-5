// Copyright 2012 The Apache Software Foundation
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

/**
 * Ensures that the encoding type of the containing form is multipart/form-data when an Upload
 * component is dynamically added to the form via Ajax.
 */
define(["./dom"],
        function (dom) {

            return function (elementId) {
                var form = dom(elementId).findContainer("form");

                if (form) {
                    form.element.enctype = "multipart/form-data";
                    form.element.encoding = "multipart/form-data";
                }
            }
        });

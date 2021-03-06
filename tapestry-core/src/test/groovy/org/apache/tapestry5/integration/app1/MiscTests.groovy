package org.apache.tapestry5.integration.app1

import org.apache.tapestry5.integration.TapestryCoreTestCase
import org.apache.tapestry5.test.TapestryTestConfiguration
import org.testng.annotations.Test

@TapestryTestConfiguration(webAppFolder = "src/test/app1")
class MiscTests extends TapestryCoreTestCase {

  @Test
  void operation_tracking_via_annotation() {
    openLinks "Operation Worker Demo", "throw exception"

    assertTitle "Application Exception"

    assertTextPresent "[Operation Description]"
  }

    @Test
    void meta_tag_identifying_page_name_is_present()
    {
        openLinks "Zone Demo"

        assertAttribute "//meta[@name='tapestry-page-name']/@content", "nested/ZoneDemo"
    }

    @Test
    void FieldGroup_mixin() {
        openLinks "Autocomplete Mixin Demo"

        assertText "css=div.field-group > label", "Title"

        // Using Geb, we could do a lot more. Sigh.
    }

    // TAP5-2045
    @Test
    void label_class_override()
    {
        openLinks "Override Label Class Demo"

        assertSourcePresent "<label for=\"firstName\" class=\"control-label\">First Name</label>",
                            "<label for=\"lastName\" class=\"dummyClassName\">Last Name</label>"

    }


}

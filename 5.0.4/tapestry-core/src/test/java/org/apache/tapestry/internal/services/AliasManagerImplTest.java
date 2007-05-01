// Copyright 2006, 2007 The Apache Software Foundation
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

package org.apache.tapestry.internal.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.tapestry.internal.test.InternalBaseTestCase;
import org.apache.tapestry.services.AliasContribution;
import org.apache.tapestry.services.AliasManager;
import org.testng.annotations.Test;

public class AliasManagerImplTest extends InternalBaseTestCase
{
    @Test
    public void no_conflict()
    {
        Log log = mockLog();
        Runnable r = mockRunnable();

        replay();

        AliasContribution[] contributions =
        { AliasContribution.create(String.class, "FRED"),
                AliasContribution.create(Runnable.class, r) };
        Collection<AliasContribution> configuration = Arrays.asList(contributions);

        AliasManager manager = new AliasManagerImpl(log, configuration);

        Map<Class, Object> map = manager.getAliasesForMode("foo");

        assertEquals(map.size(), 2);
        assertEquals(map.get(String.class), "FRED");
        assertSame(map.get(Runnable.class), r);

        verify();
    }

    @Test
    public void first_entry_wins_on_conflict()
    {
        Log log = mockLog();
        Runnable r = mockRunnable();

        log
                .error("Contribution FRED-CONFLICT (for type java.lang.String) conflicts with existing contribution FRED and has been ignored.");

        replay();

        AliasContribution[] contributions =
        { AliasContribution.create(String.class, "FRED"),
                AliasContribution.create(String.class, "FRED-CONFLICT"),
                AliasContribution.create(Runnable.class, r) };
        Collection<AliasContribution> configuration = Arrays.asList(contributions);

        AliasManager manager = new AliasManagerImpl(log, configuration);

        Map<Class, Object> map = manager.getAliasesForMode("foo");

        assertEquals(map.size(), 2);
        assertEquals(map.get(String.class), "FRED");
        assertSame(map.get(Runnable.class), r);

        verify();
    }

    @Test
    public void contributions_to_other_modes_are_ignored()
    {
        Log log = mockLog();
        Runnable r = mockRunnable();

        replay();

        AliasContribution[] contributions =
        { AliasContribution.create(String.class, "FRED"),
                AliasContribution.create(String.class, "bar", "FRED-NON-CONFLICT"),
                AliasContribution.create(Runnable.class, r) };
        Collection<AliasContribution> configuration = Arrays.asList(contributions);

        AliasManager manager = new AliasManagerImpl(log, configuration);

        Map<Class, Object> map = manager.getAliasesForMode("foo");

        assertEquals(map.size(), 2);
        assertEquals(map.get(String.class), "FRED");
        assertSame(map.get(Runnable.class), r);

        verify();
    }

    @Test
    public void mode_specific_contribution_overrides_general_contribution()
    {
        Log log = mockLog();
        Runnable r = mockRunnable();

        replay();

        AliasContribution[] contributions =
        { AliasContribution.create(String.class, "FRED"),
                AliasContribution.create(String.class, "bar", "FRED-NON-CONFLICT"),
                AliasContribution.create(Runnable.class, r) };
        Collection<AliasContribution> configuration = Arrays.asList(contributions);

        AliasManager manager = new AliasManagerImpl(log, configuration);

        Map<Class, Object> map = manager.getAliasesForMode("BAR");

        assertEquals(map.size(), 2);
        assertEquals(map.get(String.class), "FRED-NON-CONFLICT");
        assertSame(map.get(Runnable.class), r);

        verify();
    }
}

// Copyright 2010 The Apache Software Foundation
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

package org.apache.tapestry5.func;

public class LazyZipValue<A, B> implements LazyValue<Tuple<A, B>>
{
    private final Flow<A> aFlow;

    private final Flow<B> bFlow;

    public LazyZipValue(Flow<A> aFlow, Flow<B> bFlow)
    {
        this.aFlow = aFlow;
        this.bFlow = bFlow;
    }

    public Tuple<A, B> get()
    {
        return new Tuple<A, B>(aFlow.first(), bFlow.first());
    }
}

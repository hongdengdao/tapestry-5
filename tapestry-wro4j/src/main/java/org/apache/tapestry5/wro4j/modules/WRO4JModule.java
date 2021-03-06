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

package org.apache.tapestry5.wro4j.modules;

import com.github.sommeri.less4j.LessCompiler;
import com.github.sommeri.less4j.core.parser.AntlrException;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.internal.wro4j.*;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.services.ObjectRenderer;
import org.apache.tapestry5.services.assets.ResourceMinimizer;
import org.apache.tapestry5.services.assets.ResourceTransformer;
import org.apache.tapestry5.services.assets.StreamableResourceSource;
import org.apache.tapestry5.wro4j.WRO4JSymbols;
import org.apache.tapestry5.wro4j.services.ResourceProcessorSource;
import ro.isdc.wro.extensions.processor.js.GoogleClosureCompressorProcessor;
import ro.isdc.wro.extensions.processor.js.RhinoCoffeeScriptProcessor;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;
import ro.isdc.wro.model.resource.processor.impl.css.CssCompressorProcessor;

import java.util.List;

/**
 * Configures use of various WRO4J processors.
 *
 * @since 5.4
 */
public class WRO4JModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(ResourceProcessorSource.class, ResourceProcessorSourceImpl.class);
        binder.bind(ResourceTransformerFactory.class, ResourceTransformerFactoryImpl.class);
    }

    @Contribute(SymbolProvider.class)
    @FactoryDefaults
    public static void setupDefaultCacheDirectory(MappedConfiguration<String, Object> configuration)
    {
        configuration.add(WRO4JSymbols.CACHE_DIR, "${java.io.tmpdir}");
    }

    /**
     * Configures the default set of processors.
     * <dl>
     * <dt>CoffeeScriptCompiler</dt> <dd>{@link RhinoCoffeeScriptProcessor}, configured as with --bare</dd>
     * <dt>CSSMinimizer</dt> <dd>{@link CssCompressorProcessor} (see <a href="https://github.com/andyroberts/csscompressor">csscompressor on GitHub</a></dd>
     * <dt>JavaScriptMinimizer</dt>
     * <dd>{@link GoogleClosureCompressorProcessor} configured for simple optimizations. Advanced optimizations assume that all code is loaded
     * in a single bundle, not a given for Tapestry.</dd>
     * </dl>
     */
    @Contribute(ResourceProcessorSource.class)
    public static void provideDefaultProcessors(MappedConfiguration<String, ResourcePreProcessor> configuration)
    {
        configuration.addInstance("CoffeeScriptCompiler", NodeOrRhinoCoffeeScriptCompiler.class);
        configuration.addInstance("CSSMinimizer", CssCompressorProcessor.class);
        configuration.add("JavaScriptMinimizer", new GoogleClosureCompressorProcessor());
    }

    @Contribute(StreamableResourceSource.class)
    public static void provideCompilers(MappedConfiguration<String, ResourceTransformer> configuration, ResourceTransformerFactory factory)
    {
        // contribution ids are file extensions:

        configuration.add("coffee",
                factory.createCompiler("text/javascript", "CoffeeScriptCompiler", "CoffeeScript", "JavaScript", CacheMode.SINGLE_FILE));

        configuration.add("less",
                factory.createCompiler("text/css", "Less", "CSS", new LessResourceTransformer(),
                        CacheMode.MULTIPLE_FILE));
    }

    @Contribute(ResourceMinimizer.class)
    @Primary
    public static void setupDefaultResourceMinimizers(MappedConfiguration<String, ResourceMinimizer> configuration)
    {
        configuration.addInstance("text/css", CSSMinimizer.class);
        configuration.addInstance("text/javascript", JavaScriptMinimizer.class);
    }

    /**
     * Alas {@link AntlrException}s do not have a useful toString() which makes them useless in the exception report;
     * here we provide an {@link ObjectRenderer} that breaks them apart into useful strings. Eventually we may be
     * able to synthesize a {@link org.apache.tapestry5.ioc.Location} from them as well and show some of the source .less file.
     */
    @Contribute(ObjectRenderer.class)
    @Primary
    public static void provideLessCompilerProblemRenderer(MappedConfiguration<Class, ObjectRenderer> configuration)
    {
        configuration.add(LessCompiler.Problem.class, new ObjectRenderer<LessCompiler.Problem>()
        {
            public void render(LessCompiler.Problem problem, MarkupWriter writer)
            {
                List<String> strings = CollectionFactory.newList();

                if (InternalUtils.isNonBlank(problem.getMessage()))
                {
                    strings.add(problem.getMessage());
                }

                // Inside WRO4J we see that the LessSource is a StringSource with no useful toString(), so
                // it is omitted. We may need to create our own processors, stripping away a couple of layers of
                // WRO4J to get proper exception reporting!

                if (problem.getLine() > 0)
                {
                    strings.add("line " + problem.getLine());
                }

                if (problem.getCharacter() > 0)
                {
                    strings.add("position " + problem.getCharacter());
                }

                writer.write(InternalUtils.join(strings, " - "));
            }
        });
    }
}

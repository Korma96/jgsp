package com.mjvs.jgsp;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.ArrayList;
import java.util.List;

// note: class name need not match the @Plugin name.
@Plugin(name="TestLog4j2Appender", category="Core", elementType="appender", printObject=true)
public class TestLog4j2Appender extends AbstractAppender {

    private static final long serialVersionUID = -4319748955513985321L;

    private final List<LogEvent> logs = new ArrayList();

    protected TestLog4j2Appender(String name, Filter filter,
                                 Layout layout, final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    // The append method is where the appender does the work.
    // Given a log event, you are free to do with it what you want.
    // This example demonstrates:
    // 1. Concurrency: this method may be called by multiple threads concurrently
    // 2. How to use layouts
    // 3. Error handling
    @Override
    public void append(LogEvent event) {
        logs.add(event);
    }

    public void clearMessages ()
    {
        logs.clear();
    }

    private String getMessage (int index){
        if (logs.isEmpty())
        {
            return null;
        }
        return logs.get(index).getMessage().getFormattedMessage();
    }

    public List<String> getMessages()
    {
        ArrayList<String> messages = new ArrayList<>();
        for(int i=0; i<logs.size(); i++) {
            messages.add(getMessage(i));
        }
        return messages;
    }

    // Your custom appender needs to declare a factory method
    // annotated with `@PluginFactory`. Log4j will parse the configuration
    // and call this factory method to construct an appender instance with
    // the configured attributes.
    @PluginFactory
    public static TestLog4j2Appender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout layout,
            @PluginElement("Filter") final Filter filter,
            @PluginAttribute("otherAttribute") String otherAttribute) {
        if (name == null) {
            LOGGER.error("No name provided for TestLog4j2Appender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new TestLog4j2Appender(name, filter, layout, true);
    }
}

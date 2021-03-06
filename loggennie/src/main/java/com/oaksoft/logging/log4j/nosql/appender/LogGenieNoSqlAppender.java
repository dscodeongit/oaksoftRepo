package com.oaksoft.logging.log4j.nosql.appender;

import java.io.Serializable;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.nosql.appender.NoSqlProvider;

@Plugin(name = "LogGenieNoSql", category = "Core", elementType = "appender", printObject = true)
public final class LogGenieNoSqlAppender extends AbstractDatabaseAppender<LogGenieNoSqlDatabaseManager<?>>
{
	private static final long serialVersionUID = -2172481912340740713L;
	private final String description;

	private LogGenieNoSqlAppender(final String name, final Filter filter, final boolean ignoreExceptions, final LogGenieNoSqlDatabaseManager<?> manager, Layout<? extends Serializable> layout)
	{
		super(name, filter, ignoreExceptions, manager);

		this.description = this.getName() + "{ manager=" + this.getManager() + " }";
	}

    @Override
    public String toString()
    {
        return this.description;
    }


    @PluginFactory
    public static LogGenieNoSqlAppender createAppender(
            @PluginAttribute("name") final String name,
            @PluginAttribute("ignoreExceptions") final String ignore,
            @PluginElement("Filter") final Filter filter,
            @PluginAttribute("bufferSize") final String bufferSize,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("NoSqlProvider") final NoSqlProvider<?> provider)
    {

    	if (provider == null)
    	{
            LOGGER.error("NoSQL provider not specified for appender [{}].", name);
            return null;
        }

        final int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);

        final String managerName = "noSqlManager{ description=" + name + ", bufferSize=" + bufferSizeInt
                + ", provider=" + provider + " }";

        final LogGenieNoSqlDatabaseManager<?> manager = LogGenieNoSqlDatabaseManager.getNoSqlDatabaseManager(managerName, bufferSizeInt, provider);


        if (manager == null)
        {
            return null;
        }
        else
        {
        	manager.setLayout(layout);
        }

        return new LogGenieNoSqlAppender(name, filter, ignoreExceptions, manager, layout);
    }
}

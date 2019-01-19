package com.gitub.lulewiczg.jetty;
import java.util.List;

import org.eclipse.jetty.server.handler.ContextHandler;

/**
 * Holds information about found contexts.
 *
 * @author Grzegurz
 */
public class ContextHolder {
    private ContextHandler context;

    public ContextHolder(ContextHandler context) {
        this.context = context;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<li><a href=\"");
        builder.append(context.getContextPath());
        builder.append("\">");
        builder.append(context.getContextPath());
        builder.append("&nbsp;--->&nbsp;");
        builder.append(context.getResourceBase());
        if (!context.isRunning()) {
            builder.append(" [failed]");
        }
        builder.append("</a></li>\n");
        return builder.toString();
    }

    /**
     * Lists contexts as HTML.
     *
     * @param contexts
     *            context handlers
     *
     * @return HTML
     */
    public static String toHMTL(List<ContextHolder> contexts) {
        StringBuilder builder = new StringBuilder();
        builder.append("<ul>");
        for (ContextHolder holder : contexts) {
            builder.append(holder.toString());
        }
        builder.append("</ul>");
        return builder.toString();
    }
}

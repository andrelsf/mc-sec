package br.dev.multicode.configs;

import io.jaegertracing.internal.JaegerSpanContext;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class TraceInterceptor implements ContainerResponseFilter {

  private static final String REQUEST_ID_NAME = "requestId";

  @Inject
  Tracer tracer;

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
  {
    final SpanContext spanContext = tracer.scopeManager().activeSpan().context();

    if (spanContext instanceof JaegerSpanContext)
    {
      responseContext.getHeaders().add(REQUEST_ID_NAME, ((JaegerSpanContext) spanContext).getTraceId());
    }
  }
}
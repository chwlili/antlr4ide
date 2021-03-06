package com.github.jknack;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.xtext.linking.ILinkingDiagnosticMessageProvider;
import org.junit.Test;

import com.github.jknack.antlr4.Antlr4Factory;
import com.github.jknack.generator.Antlr4OutputConfigurationProvider;
import com.github.jknack.scoping.Antlr4NameProvider;
import com.github.jknack.validation.Antlr4MissingReferenceMessageProvider;
import com.google.inject.Binder;
import com.google.inject.binder.AnnotatedBindingBuilder;

public class Antlr4RuntimeModuleTest {

  @SuppressWarnings("unchecked")
  @Test
  public void configure() {
    Binder binder = createNiceMock(Binder.class);
    AnnotatedBindingBuilder<Antlr4Factory> bindAntlr4Factory = createMock(AnnotatedBindingBuilder.class);
    AnnotatedBindingBuilder<ILinkingDiagnosticMessageProvider.Extended> bindLinkingDMP = createMock(AnnotatedBindingBuilder.class);
    AnnotatedBindingBuilder<ILaunchManager> bindLaunchManager = createMock(AnnotatedBindingBuilder.class);
    final ILaunchManager launchManager = createMock(ILaunchManager.class);

    expect(binder.bind(Antlr4Factory.class)).andReturn(bindAntlr4Factory);
    expect(binder.bind(ILinkingDiagnosticMessageProvider.Extended.class)).andReturn(bindLinkingDMP);
    expect(binder.bind(ILaunchManager.class)).andReturn(bindLaunchManager);

    bindAntlr4Factory.toInstance(Antlr4Factory.eINSTANCE);

    expect(bindLinkingDMP.to(Antlr4MissingReferenceMessageProvider.class)).andReturn(null);

    bindLaunchManager.toInstance(launchManager);

    Object[] mocks = {binder, bindAntlr4Factory, bindLinkingDMP, launchManager };

    replay(mocks);

    new Antlr4RuntimeModule() {
      @Override
      protected ILaunchManager getLaunchManager() {
        return launchManager;
      }
    }.configure(binder);

    verify(mocks);
  }

  @Test
  public void bindIQualifiedNameProvider() {
    assertEquals(Antlr4NameProvider.class, new Antlr4RuntimeModule().bindIQualifiedNameProvider());
  }

  @Test
  public void bindIOutputConfigurationProvider() {
    assertEquals(Antlr4OutputConfigurationProvider.class,
        new Antlr4RuntimeModule().bindIOutputConfigurationProvider());
  }
}

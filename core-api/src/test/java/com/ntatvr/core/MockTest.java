package com.ntatvr.core;

import javax.validation.Validator;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public abstract class MockTest {

  @Mock
  protected Validator validator;
}

package io.knotx.server.common.placeholders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.vertx.core.json.JsonObject;

public class JsonPlaceholderSubstitutorTest {

  private static final JsonObject payload = new JsonObject().put("values",
      new JsonObject().put("str", "str")
          .put("number", 1)
          .put("boolean", true));

  @Test
  public void shouldReturnValueForString() {
    //given
    JsonPlaceholderSubstitutor underTest = new JsonPlaceholderSubstitutor();

    //when
    String strValue = underTest.getValue(payload, "payload.values.str");

    //then
    assertEquals("str", strValue);
  }

  @Test
  public void shouldReturnValueForNumber() {
    //given
    JsonPlaceholderSubstitutor underTest = new JsonPlaceholderSubstitutor();

    //when
    String numberValue = underTest.getValue(payload, "payload.values.number");

    //then
    assertEquals("1", numberValue);
  }

  @Test
  public void shouldReturnValueForBoolean() {
    //given
    JsonPlaceholderSubstitutor underTest = new JsonPlaceholderSubstitutor();

    //when
    String booleanValue = underTest.getValue(payload, "payload.values.boolean");

    //then
    assertEquals("true", booleanValue);
  }

  @Test
  public void shouldReturnEmptyStringValueForInvalidKey() {
    //given
    JsonPlaceholderSubstitutor underTest = new JsonPlaceholderSubstitutor();

    //when
    String invalidValue = underTest.getValue(payload, "payload.values.invalid");

    //then
    assertEquals("", invalidValue);
  }
}

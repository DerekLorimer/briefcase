package org.opendatakit.briefcase.ui.export.components;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.opendatakit.briefcase.matchers.SwingMatchers.selected;
import static org.opendatakit.briefcase.ui.export.components.Value.INHERIT;
import static org.opendatakit.briefcase.ui.export.components.Value.NO;
import static org.opendatakit.briefcase.ui.export.components.Value.YES;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class CustomConfBooleanFormTest extends AssertJSwingJUnitTestCase {
  private CustomConfBooleanPageObject component;

  @Override
  protected void onSetUp() {
    // component creation is made on each test to allow different scenarios
  }

  @Test
  public void inherit_is_selected_if_no_inital_value_is_given() {
    component = CustomConfBooleanPageObject.setUp(robot(), Optional.empty());
    component.show();

    assertThat(component.inherit(), is(selected()));
    assertThat(component.yes(), is(not(selected())));
    assertThat(component.no(), is(not(selected())));
  }

  @Test
  public void it_can_have_an_initial_value_different_than_inherit() {
    component = CustomConfBooleanPageObject.setUp(robot(), Optional.of(YES));
    component.show();

    assertThat(component.inherit(), is(not(selected())));
    assertThat(component.yes(), is(selected()));
    assertThat(component.no(), is(not(selected())));
  }

  @Test
  public void selecting_a_different_option_unselects_others() {
    // This test verifies that all radio buttons belong to the same ButtonGroup
    component = CustomConfBooleanPageObject.setUp(robot(), Optional.of(YES));
    component.show();

    component.set(NO);

    assertThat(component.inherit(), is(not(selected())));
    assertThat(component.yes(), is(not(selected())));
    assertThat(component.no(), is(selected()));
  }

  @Test
  public void lets_third_parties_subscribe_to_change_events() {
    Wrapper<Value> lastValue = new Wrapper<>(INHERIT);
    component = CustomConfBooleanPageObject.setUp(robot(), Optional.of(lastValue.get()));
    component.onChange(lastValue::set);
    component.show();

    component.set(YES);

    assertThat(lastValue.get(), is(YES));
  }

  @Test
  public void avoids_sending_duplicate_events() {
    final AtomicInteger changeCounts = new AtomicInteger(0);
    component = CustomConfBooleanPageObject.setUp(robot(), Optional.of(NO));
    component.onChange(__ -> changeCounts.incrementAndGet());
    component.show();

    component.set(YES);
    component.set(YES);

    assertThat(changeCounts.get(), is(1));
  }

  class Wrapper<T> {
    public T t;

    Wrapper(T t) {
      this.t = t;
    }

    public T get() {
      return t;
    }

    public void set(T newT) {
      t = newT;
    }
  }
}
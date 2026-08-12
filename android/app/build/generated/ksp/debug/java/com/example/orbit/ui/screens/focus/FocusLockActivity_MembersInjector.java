package com.example.orbit.ui.screens.focus;

import com.example.orbit.data.local.FocusPreferencesManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;

@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class FocusLockActivity_MembersInjector implements MembersInjector<FocusLockActivity> {
  private final Provider<FocusPreferencesManager> focusPrefsProvider;

  private FocusLockActivity_MembersInjector(Provider<FocusPreferencesManager> focusPrefsProvider) {
    this.focusPrefsProvider = focusPrefsProvider;
  }

  @Override
  public void injectMembers(FocusLockActivity instance) {
    injectFocusPrefs(instance, focusPrefsProvider.get());
  }

  public static MembersInjector<FocusLockActivity> create(
      Provider<FocusPreferencesManager> focusPrefsProvider) {
    return new FocusLockActivity_MembersInjector(focusPrefsProvider);
  }

  @InjectedFieldSignature("com.example.orbit.ui.screens.focus.FocusLockActivity.focusPrefs")
  public static void injectFocusPrefs(FocusLockActivity instance,
      FocusPreferencesManager focusPrefs) {
    instance.focusPrefs = focusPrefs;
  }
}

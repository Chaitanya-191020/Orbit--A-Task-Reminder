package com.example.orbit.services;

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
public final class FocusLockAccessibilityService_MembersInjector implements MembersInjector<FocusLockAccessibilityService> {
  private final Provider<FocusPreferencesManager> focusPrefsProvider;

  private FocusLockAccessibilityService_MembersInjector(
      Provider<FocusPreferencesManager> focusPrefsProvider) {
    this.focusPrefsProvider = focusPrefsProvider;
  }

  @Override
  public void injectMembers(FocusLockAccessibilityService instance) {
    injectFocusPrefs(instance, focusPrefsProvider.get());
  }

  public static MembersInjector<FocusLockAccessibilityService> create(
      Provider<FocusPreferencesManager> focusPrefsProvider) {
    return new FocusLockAccessibilityService_MembersInjector(focusPrefsProvider);
  }

  @InjectedFieldSignature("com.example.orbit.services.FocusLockAccessibilityService.focusPrefs")
  public static void injectFocusPrefs(FocusLockAccessibilityService instance,
      FocusPreferencesManager focusPrefs) {
    instance.focusPrefs = focusPrefs;
  }
}

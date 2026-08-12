package com.example.orbit.ui.viewmodels;

import android.content.Context;
import com.example.orbit.data.local.FocusPreferencesManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AllowedAppsViewModel_Factory implements Factory<AllowedAppsViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<FocusPreferencesManager> focusPrefsProvider;

  private AllowedAppsViewModel_Factory(Provider<Context> contextProvider,
      Provider<FocusPreferencesManager> focusPrefsProvider) {
    this.contextProvider = contextProvider;
    this.focusPrefsProvider = focusPrefsProvider;
  }

  @Override
  public AllowedAppsViewModel get() {
    return newInstance(contextProvider.get(), focusPrefsProvider.get());
  }

  public static AllowedAppsViewModel_Factory create(Provider<Context> contextProvider,
      Provider<FocusPreferencesManager> focusPrefsProvider) {
    return new AllowedAppsViewModel_Factory(contextProvider, focusPrefsProvider);
  }

  public static AllowedAppsViewModel newInstance(Context context,
      FocusPreferencesManager focusPrefs) {
    return new AllowedAppsViewModel(context, focusPrefs);
  }
}

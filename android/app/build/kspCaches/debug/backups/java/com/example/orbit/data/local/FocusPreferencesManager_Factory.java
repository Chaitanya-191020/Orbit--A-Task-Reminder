package com.example.orbit.data.local;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class FocusPreferencesManager_Factory implements Factory<FocusPreferencesManager> {
  private final Provider<Context> contextProvider;

  private FocusPreferencesManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public FocusPreferencesManager get() {
    return newInstance(contextProvider.get());
  }

  public static FocusPreferencesManager_Factory create(Provider<Context> contextProvider) {
    return new FocusPreferencesManager_Factory(contextProvider);
  }

  public static FocusPreferencesManager newInstance(Context context) {
    return new FocusPreferencesManager(context);
  }
}

package com.example.orbit.ui.viewmodels;

import com.example.orbit.data.remote.OrbitApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class HabitsViewModel_Factory implements Factory<HabitsViewModel> {
  private final Provider<OrbitApiService> apiServiceProvider;

  private HabitsViewModel_Factory(Provider<OrbitApiService> apiServiceProvider) {
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public HabitsViewModel get() {
    return newInstance(apiServiceProvider.get());
  }

  public static HabitsViewModel_Factory create(Provider<OrbitApiService> apiServiceProvider) {
    return new HabitsViewModel_Factory(apiServiceProvider);
  }

  public static HabitsViewModel newInstance(OrbitApiService apiService) {
    return new HabitsViewModel(apiService);
  }
}

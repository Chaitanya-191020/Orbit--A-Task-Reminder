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
public final class AnalyticsViewModel_Factory implements Factory<AnalyticsViewModel> {
  private final Provider<OrbitApiService> apiServiceProvider;

  private AnalyticsViewModel_Factory(Provider<OrbitApiService> apiServiceProvider) {
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public AnalyticsViewModel get() {
    return newInstance(apiServiceProvider.get());
  }

  public static AnalyticsViewModel_Factory create(Provider<OrbitApiService> apiServiceProvider) {
    return new AnalyticsViewModel_Factory(apiServiceProvider);
  }

  public static AnalyticsViewModel newInstance(OrbitApiService apiService) {
    return new AnalyticsViewModel(apiService);
  }
}

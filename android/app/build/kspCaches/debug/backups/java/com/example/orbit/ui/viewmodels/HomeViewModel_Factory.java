package com.example.orbit.ui.viewmodels;

import com.example.orbit.alarms.AlarmScheduler;
import com.example.orbit.data.repository.AlarmRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<AlarmRepository> alarmRepositoryProvider;

  private final Provider<AlarmScheduler> alarmSchedulerProvider;

  private HomeViewModel_Factory(Provider<AlarmRepository> alarmRepositoryProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider) {
    this.alarmRepositoryProvider = alarmRepositoryProvider;
    this.alarmSchedulerProvider = alarmSchedulerProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(alarmRepositoryProvider.get(), alarmSchedulerProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<AlarmRepository> alarmRepositoryProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider) {
    return new HomeViewModel_Factory(alarmRepositoryProvider, alarmSchedulerProvider);
  }

  public static HomeViewModel newInstance(AlarmRepository alarmRepository,
      AlarmScheduler alarmScheduler) {
    return new HomeViewModel(alarmRepository, alarmScheduler);
  }
}

package com.example.orbit.alarms;

import com.example.orbit.data.repository.AlarmRepository;
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
public final class AlarmService_MembersInjector implements MembersInjector<AlarmService> {
  private final Provider<AlarmScheduler> alarmSchedulerProvider;

  private final Provider<AlarmRepository> alarmRepositoryProvider;

  private AlarmService_MembersInjector(Provider<AlarmScheduler> alarmSchedulerProvider,
      Provider<AlarmRepository> alarmRepositoryProvider) {
    this.alarmSchedulerProvider = alarmSchedulerProvider;
    this.alarmRepositoryProvider = alarmRepositoryProvider;
  }

  @Override
  public void injectMembers(AlarmService instance) {
    injectAlarmScheduler(instance, alarmSchedulerProvider.get());
    injectAlarmRepository(instance, alarmRepositoryProvider.get());
  }

  public static MembersInjector<AlarmService> create(
      Provider<AlarmScheduler> alarmSchedulerProvider,
      Provider<AlarmRepository> alarmRepositoryProvider) {
    return new AlarmService_MembersInjector(alarmSchedulerProvider, alarmRepositoryProvider);
  }

  @InjectedFieldSignature("com.example.orbit.alarms.AlarmService.alarmScheduler")
  public static void injectAlarmScheduler(AlarmService instance, AlarmScheduler alarmScheduler) {
    instance.alarmScheduler = alarmScheduler;
  }

  @InjectedFieldSignature("com.example.orbit.alarms.AlarmService.alarmRepository")
  public static void injectAlarmRepository(AlarmService instance, AlarmRepository alarmRepository) {
    instance.alarmRepository = alarmRepository;
  }
}

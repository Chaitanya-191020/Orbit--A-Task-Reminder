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
public final class AlarmReceiver_MembersInjector implements MembersInjector<AlarmReceiver> {
  private final Provider<AlarmRepository> alarmRepositoryProvider;

  private AlarmReceiver_MembersInjector(Provider<AlarmRepository> alarmRepositoryProvider) {
    this.alarmRepositoryProvider = alarmRepositoryProvider;
  }

  @Override
  public void injectMembers(AlarmReceiver instance) {
    injectAlarmRepository(instance, alarmRepositoryProvider.get());
  }

  public static MembersInjector<AlarmReceiver> create(
      Provider<AlarmRepository> alarmRepositoryProvider) {
    return new AlarmReceiver_MembersInjector(alarmRepositoryProvider);
  }

  @InjectedFieldSignature("com.example.orbit.alarms.AlarmReceiver.alarmRepository")
  public static void injectAlarmRepository(AlarmReceiver instance,
      AlarmRepository alarmRepository) {
    instance.alarmRepository = alarmRepository;
  }
}

package com.example.orbit.di;

import com.example.orbit.data.local.AlarmDao;
import com.example.orbit.data.local.OrbitDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideAlarmDaoFactory implements Factory<AlarmDao> {
  private final Provider<OrbitDatabase> databaseProvider;

  private AppModule_ProvideAlarmDaoFactory(Provider<OrbitDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AlarmDao get() {
    return provideAlarmDao(databaseProvider.get());
  }

  public static AppModule_ProvideAlarmDaoFactory create(Provider<OrbitDatabase> databaseProvider) {
    return new AppModule_ProvideAlarmDaoFactory(databaseProvider);
  }

  public static AlarmDao provideAlarmDao(OrbitDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAlarmDao(database));
  }
}

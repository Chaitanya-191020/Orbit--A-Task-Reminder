package com.example.orbit.di;

import com.example.orbit.data.local.OrbitDatabase;
import com.example.orbit.data.local.TaskDao;
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
public final class AppModule_ProvideTaskDaoFactory implements Factory<TaskDao> {
  private final Provider<OrbitDatabase> databaseProvider;

  private AppModule_ProvideTaskDaoFactory(Provider<OrbitDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TaskDao get() {
    return provideTaskDao(databaseProvider.get());
  }

  public static AppModule_ProvideTaskDaoFactory create(Provider<OrbitDatabase> databaseProvider) {
    return new AppModule_ProvideTaskDaoFactory(databaseProvider);
  }

  public static TaskDao provideTaskDao(OrbitDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideTaskDao(database));
  }
}

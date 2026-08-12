package com.example.orbit.data.repository;

import com.example.orbit.data.local.TaskDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class TaskRepository_Factory implements Factory<TaskRepository> {
  private final Provider<TaskDao> taskDaoProvider;

  private TaskRepository_Factory(Provider<TaskDao> taskDaoProvider) {
    this.taskDaoProvider = taskDaoProvider;
  }

  @Override
  public TaskRepository get() {
    return newInstance(taskDaoProvider.get());
  }

  public static TaskRepository_Factory create(Provider<TaskDao> taskDaoProvider) {
    return new TaskRepository_Factory(taskDaoProvider);
  }

  public static TaskRepository newInstance(TaskDao taskDao) {
    return new TaskRepository(taskDao);
  }
}

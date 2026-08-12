package com.example.orbit.ui.viewmodels;

import com.example.orbit.alarms.AlarmScheduler;
import com.example.orbit.data.repository.TaskRepository;
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
public final class TasksViewModel_Factory implements Factory<TasksViewModel> {
  private final Provider<TaskRepository> taskRepositoryProvider;

  private final Provider<AlarmScheduler> alarmSchedulerProvider;

  private TasksViewModel_Factory(Provider<TaskRepository> taskRepositoryProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider) {
    this.taskRepositoryProvider = taskRepositoryProvider;
    this.alarmSchedulerProvider = alarmSchedulerProvider;
  }

  @Override
  public TasksViewModel get() {
    return newInstance(taskRepositoryProvider.get(), alarmSchedulerProvider.get());
  }

  public static TasksViewModel_Factory create(Provider<TaskRepository> taskRepositoryProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider) {
    return new TasksViewModel_Factory(taskRepositoryProvider, alarmSchedulerProvider);
  }

  public static TasksViewModel newInstance(TaskRepository taskRepository,
      AlarmScheduler alarmScheduler) {
    return new TasksViewModel(taskRepository, alarmScheduler);
  }
}

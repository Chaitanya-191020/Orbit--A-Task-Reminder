package com.example.orbit.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class OrbitDatabase_Impl : OrbitDatabase() {
  private val _alarmDao: Lazy<AlarmDao> = lazy {
    AlarmDao_Impl(this)
  }

  private val _taskDao: Lazy<TaskDao> = lazy {
    TaskDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(5, "1efaf4e27348bf622c69ac232503eadd", "07d8034d43adfa71aa2c72ce304db8f7") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `alarms` (`id` TEXT NOT NULL, `label` TEXT NOT NULL, `alarmTime` TEXT NOT NULL, `repeatDays` TEXT NOT NULL, `soundUri` TEXT, `ringtoneName` TEXT, `vibrate` INTEGER NOT NULL, `snoozeDurationMinutes` INTEGER NOT NULL, `snoozeTimes` INTEGER NOT NULL, `snoozedCount` INTEGER NOT NULL, `isEnabled` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `tasks` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `dueDate` INTEGER, `startTime` TEXT, `endTime` TEXT, `priority` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `attachedAlarmId` TEXT, `loopId` TEXT, `loopIndex` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '1efaf4e27348bf622c69ac232503eadd')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `alarms`")
        connection.execSQL("DROP TABLE IF EXISTS `tasks`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsAlarms: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsAlarms.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAlarms.put("label", TableInfo.Column("label", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAlarms.put("alarmTime", TableInfo.Column("alarmTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAlarms.put("repeatDays", TableInfo.Column("repeatDays", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAlarms.put("soundUri", TableInfo.Column("soundUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAlarms.put("ringtoneName", TableInfo.Column("ringtoneName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAlarms.put("vibrate", TableInfo.Column("vibrate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAlarms.put("snoozeDurationMinutes", TableInfo.Column("snoozeDurationMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAlarms.put("snoozeTimes", TableInfo.Column("snoozeTimes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAlarms.put("snoozedCount", TableInfo.Column("snoozedCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAlarms.put("isEnabled", TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAlarms.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysAlarms: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesAlarms: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoAlarms: TableInfo = TableInfo("alarms", _columnsAlarms, _foreignKeysAlarms, _indicesAlarms)
        val _existingAlarms: TableInfo = read(connection, "alarms")
        if (!_infoAlarms.equals(_existingAlarms)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |alarms(com.example.orbit.data.local.AlarmEntity).
              | Expected:
              |""".trimMargin() + _infoAlarms + """
              |
              | Found:
              |""".trimMargin() + _existingAlarms)
        }
        val _columnsTasks: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTasks.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("title", TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("description", TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("dueDate", TableInfo.Column("dueDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("startTime", TableInfo.Column("startTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("endTime", TableInfo.Column("endTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("priority", TableInfo.Column("priority", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("isCompleted", TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("attachedAlarmId", TableInfo.Column("attachedAlarmId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("loopId", TableInfo.Column("loopId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("loopIndex", TableInfo.Column("loopIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTasks: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTasks: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTasks: TableInfo = TableInfo("tasks", _columnsTasks, _foreignKeysTasks, _indicesTasks)
        val _existingTasks: TableInfo = read(connection, "tasks")
        if (!_infoTasks.equals(_existingTasks)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |tasks(com.example.orbit.data.local.TaskEntity).
              | Expected:
              |""".trimMargin() + _infoTasks + """
              |
              | Found:
              |""".trimMargin() + _existingTasks)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "alarms", "tasks")
  }

  public override fun clearAllTables() {
    super.performClear(false, "alarms", "tasks")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(AlarmDao::class, AlarmDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(TaskDao::class, TaskDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun alarmDao(): AlarmDao = _alarmDao.value

  public override fun taskDao(): TaskDao = _taskDao.value
}

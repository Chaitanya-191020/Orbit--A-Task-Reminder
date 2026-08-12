package com.example.orbit.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class TaskDao_Impl(
  __db: RoomDatabase,
) : TaskDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTaskEntity: EntityInsertAdapter<TaskEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTaskEntity = object : EntityInsertAdapter<TaskEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `tasks` (`id`,`title`,`description`,`dueDate`,`startTime`,`endTime`,`priority`,`isCompleted`,`attachedAlarmId`,`loopId`,`loopIndex`) VALUES (?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TaskEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpDescription)
        }
        val _tmpDueDate: Long? = entity.dueDate
        if (_tmpDueDate == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmpDueDate)
        }
        val _tmpStartTime: String? = entity.startTime
        if (_tmpStartTime == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpStartTime)
        }
        val _tmpEndTime: String? = entity.endTime
        if (_tmpEndTime == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpEndTime)
        }
        statement.bindLong(7, entity.priority.toLong())
        val _tmp: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        val _tmpAttachedAlarmId: String? = entity.attachedAlarmId
        if (_tmpAttachedAlarmId == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpAttachedAlarmId)
        }
        val _tmpLoopId: String? = entity.loopId
        if (_tmpLoopId == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpLoopId)
        }
        statement.bindLong(11, entity.loopIndex.toLong())
      }
    }
  }

  public override suspend fun insertTask(task: TaskEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTaskEntity.insert(_connection, task)
  }

  public override fun getTasksForAlarm(alarmId: String): Flow<List<TaskEntity>> {
    val _sql: String = "SELECT * FROM tasks WHERE attachedAlarmId = ?"
    return createFlow(__db, false, arrayOf("tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, alarmId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDueDate: Int = getColumnIndexOrThrow(_stmt, "dueDate")
        val _columnIndexOfStartTime: Int = getColumnIndexOrThrow(_stmt, "startTime")
        val _columnIndexOfEndTime: Int = getColumnIndexOrThrow(_stmt, "endTime")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfAttachedAlarmId: Int = getColumnIndexOrThrow(_stmt, "attachedAlarmId")
        val _columnIndexOfLoopId: Int = getColumnIndexOrThrow(_stmt, "loopId")
        val _columnIndexOfLoopIndex: Int = getColumnIndexOrThrow(_stmt, "loopIndex")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDueDate: Long?
          if (_stmt.isNull(_columnIndexOfDueDate)) {
            _tmpDueDate = null
          } else {
            _tmpDueDate = _stmt.getLong(_columnIndexOfDueDate)
          }
          val _tmpStartTime: String?
          if (_stmt.isNull(_columnIndexOfStartTime)) {
            _tmpStartTime = null
          } else {
            _tmpStartTime = _stmt.getText(_columnIndexOfStartTime)
          }
          val _tmpEndTime: String?
          if (_stmt.isNull(_columnIndexOfEndTime)) {
            _tmpEndTime = null
          } else {
            _tmpEndTime = _stmt.getText(_columnIndexOfEndTime)
          }
          val _tmpPriority: Int
          _tmpPriority = _stmt.getLong(_columnIndexOfPriority).toInt()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpAttachedAlarmId: String?
          if (_stmt.isNull(_columnIndexOfAttachedAlarmId)) {
            _tmpAttachedAlarmId = null
          } else {
            _tmpAttachedAlarmId = _stmt.getText(_columnIndexOfAttachedAlarmId)
          }
          val _tmpLoopId: String?
          if (_stmt.isNull(_columnIndexOfLoopId)) {
            _tmpLoopId = null
          } else {
            _tmpLoopId = _stmt.getText(_columnIndexOfLoopId)
          }
          val _tmpLoopIndex: Int
          _tmpLoopIndex = _stmt.getLong(_columnIndexOfLoopIndex).toInt()
          _item = TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpDueDate,_tmpStartTime,_tmpEndTime,_tmpPriority,_tmpIsCompleted,_tmpAttachedAlarmId,_tmpLoopId,_tmpLoopIndex)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllTasks(): Flow<List<TaskEntity>> {
    val _sql: String = "SELECT * FROM tasks"
    return createFlow(__db, false, arrayOf("tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDueDate: Int = getColumnIndexOrThrow(_stmt, "dueDate")
        val _columnIndexOfStartTime: Int = getColumnIndexOrThrow(_stmt, "startTime")
        val _columnIndexOfEndTime: Int = getColumnIndexOrThrow(_stmt, "endTime")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfAttachedAlarmId: Int = getColumnIndexOrThrow(_stmt, "attachedAlarmId")
        val _columnIndexOfLoopId: Int = getColumnIndexOrThrow(_stmt, "loopId")
        val _columnIndexOfLoopIndex: Int = getColumnIndexOrThrow(_stmt, "loopIndex")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDueDate: Long?
          if (_stmt.isNull(_columnIndexOfDueDate)) {
            _tmpDueDate = null
          } else {
            _tmpDueDate = _stmt.getLong(_columnIndexOfDueDate)
          }
          val _tmpStartTime: String?
          if (_stmt.isNull(_columnIndexOfStartTime)) {
            _tmpStartTime = null
          } else {
            _tmpStartTime = _stmt.getText(_columnIndexOfStartTime)
          }
          val _tmpEndTime: String?
          if (_stmt.isNull(_columnIndexOfEndTime)) {
            _tmpEndTime = null
          } else {
            _tmpEndTime = _stmt.getText(_columnIndexOfEndTime)
          }
          val _tmpPriority: Int
          _tmpPriority = _stmt.getLong(_columnIndexOfPriority).toInt()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpAttachedAlarmId: String?
          if (_stmt.isNull(_columnIndexOfAttachedAlarmId)) {
            _tmpAttachedAlarmId = null
          } else {
            _tmpAttachedAlarmId = _stmt.getText(_columnIndexOfAttachedAlarmId)
          }
          val _tmpLoopId: String?
          if (_stmt.isNull(_columnIndexOfLoopId)) {
            _tmpLoopId = null
          } else {
            _tmpLoopId = _stmt.getText(_columnIndexOfLoopId)
          }
          val _tmpLoopIndex: Int
          _tmpLoopIndex = _stmt.getLong(_columnIndexOfLoopIndex).toInt()
          _item = TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpDueDate,_tmpStartTime,_tmpEndTime,_tmpPriority,_tmpIsCompleted,_tmpAttachedAlarmId,_tmpLoopId,_tmpLoopIndex)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTaskById(taskId: String): TaskEntity? {
    val _sql: String = "SELECT * FROM tasks WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, taskId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDueDate: Int = getColumnIndexOrThrow(_stmt, "dueDate")
        val _columnIndexOfStartTime: Int = getColumnIndexOrThrow(_stmt, "startTime")
        val _columnIndexOfEndTime: Int = getColumnIndexOrThrow(_stmt, "endTime")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfAttachedAlarmId: Int = getColumnIndexOrThrow(_stmt, "attachedAlarmId")
        val _columnIndexOfLoopId: Int = getColumnIndexOrThrow(_stmt, "loopId")
        val _columnIndexOfLoopIndex: Int = getColumnIndexOrThrow(_stmt, "loopIndex")
        val _result: TaskEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDueDate: Long?
          if (_stmt.isNull(_columnIndexOfDueDate)) {
            _tmpDueDate = null
          } else {
            _tmpDueDate = _stmt.getLong(_columnIndexOfDueDate)
          }
          val _tmpStartTime: String?
          if (_stmt.isNull(_columnIndexOfStartTime)) {
            _tmpStartTime = null
          } else {
            _tmpStartTime = _stmt.getText(_columnIndexOfStartTime)
          }
          val _tmpEndTime: String?
          if (_stmt.isNull(_columnIndexOfEndTime)) {
            _tmpEndTime = null
          } else {
            _tmpEndTime = _stmt.getText(_columnIndexOfEndTime)
          }
          val _tmpPriority: Int
          _tmpPriority = _stmt.getLong(_columnIndexOfPriority).toInt()
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpAttachedAlarmId: String?
          if (_stmt.isNull(_columnIndexOfAttachedAlarmId)) {
            _tmpAttachedAlarmId = null
          } else {
            _tmpAttachedAlarmId = _stmt.getText(_columnIndexOfAttachedAlarmId)
          }
          val _tmpLoopId: String?
          if (_stmt.isNull(_columnIndexOfLoopId)) {
            _tmpLoopId = null
          } else {
            _tmpLoopId = _stmt.getText(_columnIndexOfLoopId)
          }
          val _tmpLoopIndex: Int
          _tmpLoopIndex = _stmt.getLong(_columnIndexOfLoopIndex).toInt()
          _result = TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpDueDate,_tmpStartTime,_tmpEndTime,_tmpPriority,_tmpIsCompleted,_tmpAttachedAlarmId,_tmpLoopId,_tmpLoopIndex)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteTask(taskId: String) {
    val _sql: String = "DELETE FROM tasks WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, taskId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}

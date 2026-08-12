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
public class AlarmDao_Impl(
  __db: RoomDatabase,
) : AlarmDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfAlarmEntity: EntityInsertAdapter<AlarmEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfAlarmEntity = object : EntityInsertAdapter<AlarmEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `alarms` (`id`,`label`,`alarmTime`,`repeatDays`,`soundUri`,`ringtoneName`,`vibrate`,`snoozeDurationMinutes`,`snoozeTimes`,`snoozedCount`,`isEnabled`,`createdAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AlarmEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.label)
        statement.bindText(3, entity.alarmTime)
        statement.bindText(4, entity.repeatDays)
        val _tmpSoundUri: String? = entity.soundUri
        if (_tmpSoundUri == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpSoundUri)
        }
        val _tmpRingtoneName: String? = entity.ringtoneName
        if (_tmpRingtoneName == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpRingtoneName)
        }
        val _tmp: Int = if (entity.vibrate) 1 else 0
        statement.bindLong(7, _tmp.toLong())
        statement.bindLong(8, entity.snoozeDurationMinutes.toLong())
        statement.bindLong(9, entity.snoozeTimes.toLong())
        statement.bindLong(10, entity.snoozedCount.toLong())
        val _tmp_1: Int = if (entity.isEnabled) 1 else 0
        statement.bindLong(11, _tmp_1.toLong())
        statement.bindLong(12, entity.createdAt)
      }
    }
  }

  public override suspend fun insertAlarm(alarm: AlarmEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfAlarmEntity.insert(_connection, alarm)
  }

  public override fun getAllAlarms(): Flow<List<AlarmEntity>> {
    val _sql: String = "SELECT * FROM alarms ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("alarms")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfAlarmTime: Int = getColumnIndexOrThrow(_stmt, "alarmTime")
        val _columnIndexOfRepeatDays: Int = getColumnIndexOrThrow(_stmt, "repeatDays")
        val _columnIndexOfSoundUri: Int = getColumnIndexOrThrow(_stmt, "soundUri")
        val _columnIndexOfRingtoneName: Int = getColumnIndexOrThrow(_stmt, "ringtoneName")
        val _columnIndexOfVibrate: Int = getColumnIndexOrThrow(_stmt, "vibrate")
        val _columnIndexOfSnoozeDurationMinutes: Int = getColumnIndexOrThrow(_stmt, "snoozeDurationMinutes")
        val _columnIndexOfSnoozeTimes: Int = getColumnIndexOrThrow(_stmt, "snoozeTimes")
        val _columnIndexOfSnoozedCount: Int = getColumnIndexOrThrow(_stmt, "snoozedCount")
        val _columnIndexOfIsEnabled: Int = getColumnIndexOrThrow(_stmt, "isEnabled")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<AlarmEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AlarmEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpLabel: String
          _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          val _tmpAlarmTime: String
          _tmpAlarmTime = _stmt.getText(_columnIndexOfAlarmTime)
          val _tmpRepeatDays: String
          _tmpRepeatDays = _stmt.getText(_columnIndexOfRepeatDays)
          val _tmpSoundUri: String?
          if (_stmt.isNull(_columnIndexOfSoundUri)) {
            _tmpSoundUri = null
          } else {
            _tmpSoundUri = _stmt.getText(_columnIndexOfSoundUri)
          }
          val _tmpRingtoneName: String?
          if (_stmt.isNull(_columnIndexOfRingtoneName)) {
            _tmpRingtoneName = null
          } else {
            _tmpRingtoneName = _stmt.getText(_columnIndexOfRingtoneName)
          }
          val _tmpVibrate: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfVibrate).toInt()
          _tmpVibrate = _tmp != 0
          val _tmpSnoozeDurationMinutes: Int
          _tmpSnoozeDurationMinutes = _stmt.getLong(_columnIndexOfSnoozeDurationMinutes).toInt()
          val _tmpSnoozeTimes: Int
          _tmpSnoozeTimes = _stmt.getLong(_columnIndexOfSnoozeTimes).toInt()
          val _tmpSnoozedCount: Int
          _tmpSnoozedCount = _stmt.getLong(_columnIndexOfSnoozedCount).toInt()
          val _tmpIsEnabled: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsEnabled).toInt()
          _tmpIsEnabled = _tmp_1 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _item = AlarmEntity(_tmpId,_tmpLabel,_tmpAlarmTime,_tmpRepeatDays,_tmpSoundUri,_tmpRingtoneName,_tmpVibrate,_tmpSnoozeDurationMinutes,_tmpSnoozeTimes,_tmpSnoozedCount,_tmpIsEnabled,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAlarmById(id: String): AlarmEntity? {
    val _sql: String = "SELECT * FROM alarms WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfAlarmTime: Int = getColumnIndexOrThrow(_stmt, "alarmTime")
        val _columnIndexOfRepeatDays: Int = getColumnIndexOrThrow(_stmt, "repeatDays")
        val _columnIndexOfSoundUri: Int = getColumnIndexOrThrow(_stmt, "soundUri")
        val _columnIndexOfRingtoneName: Int = getColumnIndexOrThrow(_stmt, "ringtoneName")
        val _columnIndexOfVibrate: Int = getColumnIndexOrThrow(_stmt, "vibrate")
        val _columnIndexOfSnoozeDurationMinutes: Int = getColumnIndexOrThrow(_stmt, "snoozeDurationMinutes")
        val _columnIndexOfSnoozeTimes: Int = getColumnIndexOrThrow(_stmt, "snoozeTimes")
        val _columnIndexOfSnoozedCount: Int = getColumnIndexOrThrow(_stmt, "snoozedCount")
        val _columnIndexOfIsEnabled: Int = getColumnIndexOrThrow(_stmt, "isEnabled")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: AlarmEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpLabel: String
          _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          val _tmpAlarmTime: String
          _tmpAlarmTime = _stmt.getText(_columnIndexOfAlarmTime)
          val _tmpRepeatDays: String
          _tmpRepeatDays = _stmt.getText(_columnIndexOfRepeatDays)
          val _tmpSoundUri: String?
          if (_stmt.isNull(_columnIndexOfSoundUri)) {
            _tmpSoundUri = null
          } else {
            _tmpSoundUri = _stmt.getText(_columnIndexOfSoundUri)
          }
          val _tmpRingtoneName: String?
          if (_stmt.isNull(_columnIndexOfRingtoneName)) {
            _tmpRingtoneName = null
          } else {
            _tmpRingtoneName = _stmt.getText(_columnIndexOfRingtoneName)
          }
          val _tmpVibrate: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfVibrate).toInt()
          _tmpVibrate = _tmp != 0
          val _tmpSnoozeDurationMinutes: Int
          _tmpSnoozeDurationMinutes = _stmt.getLong(_columnIndexOfSnoozeDurationMinutes).toInt()
          val _tmpSnoozeTimes: Int
          _tmpSnoozeTimes = _stmt.getLong(_columnIndexOfSnoozeTimes).toInt()
          val _tmpSnoozedCount: Int
          _tmpSnoozedCount = _stmt.getLong(_columnIndexOfSnoozedCount).toInt()
          val _tmpIsEnabled: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsEnabled).toInt()
          _tmpIsEnabled = _tmp_1 != 0
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          _result = AlarmEntity(_tmpId,_tmpLabel,_tmpAlarmTime,_tmpRepeatDays,_tmpSoundUri,_tmpRingtoneName,_tmpVibrate,_tmpSnoozeDurationMinutes,_tmpSnoozeTimes,_tmpSnoozedCount,_tmpIsEnabled,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAlarm(id: String) {
    val _sql: String = "DELETE FROM alarms WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
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

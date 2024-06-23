package com.example.mychat.DAO;

import android.database.Cursor;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.mychat.models.MessageAi;
import com.example.mychat.utils.Converters;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MessageAIDAO_Impl implements MessageAIDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MessageAi> __insertionAdapterOfMessageAi;

  private final EntityDeletionOrUpdateAdapter<MessageAi> __deletionAdapterOfMessageAi;

  public MessageAIDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMessageAi = new EntityInsertionAdapter<MessageAi>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `messageAi` (`id`,`text`,`isSentByUser`,`image`,`timestamp`,`roomId`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final MessageAi entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getText() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getText());
        }
        final int _tmp = entity.isSentByUser() ? 1 : 0;
        statement.bindLong(3, _tmp);
        final byte[] _tmp_1 = Converters.fromBitmap(entity.getImage());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindBlob(4, _tmp_1);
        }
        statement.bindLong(5, entity.getTimestamp());
        statement.bindLong(6, entity.getRoomId());
      }
    };
    this.__deletionAdapterOfMessageAi = new EntityDeletionOrUpdateAdapter<MessageAi>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `messageAi` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final MessageAi entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public void insertMessageAi(final MessageAi messageAi) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMessageAi.insert(messageAi);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertMessageAis(final List<MessageAi> messageAis) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMessageAi.insert(messageAis);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final MessageAi messageAi) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfMessageAi.handle(messageAi);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<MessageAi> getListMessageAi(final long roomId) {
    final String _sql = "SELECT * FROM messageAi WHERE roomId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, roomId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
      final int _cursorIndexOfIsSentByUser = CursorUtil.getColumnIndexOrThrow(_cursor, "isSentByUser");
      final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfRoomId = CursorUtil.getColumnIndexOrThrow(_cursor, "roomId");
      final List<MessageAi> _result = new ArrayList<MessageAi>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final MessageAi _item;
        final String _tmpText;
        if (_cursor.isNull(_cursorIndexOfText)) {
          _tmpText = null;
        } else {
          _tmpText = _cursor.getString(_cursorIndexOfText);
        }
        final boolean _tmpIsSentByUser;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsSentByUser);
        _tmpIsSentByUser = _tmp != 0;
        final Bitmap _tmpImage;
        final byte[] _tmp_1;
        if (_cursor.isNull(_cursorIndexOfImage)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getBlob(_cursorIndexOfImage);
        }
        _tmpImage = Converters.toBitmap(_tmp_1);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        final long _tmpRoomId;
        _tmpRoomId = _cursor.getLong(_cursorIndexOfRoomId);
        _item = new MessageAi(_tmpText,_tmpIsSentByUser,_tmpImage,_tmpTimestamp,_tmpRoomId);
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

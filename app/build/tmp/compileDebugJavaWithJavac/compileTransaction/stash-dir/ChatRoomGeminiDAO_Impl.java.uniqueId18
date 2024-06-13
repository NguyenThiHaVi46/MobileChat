package com.example.mychat.DAO;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.mychat.models.ChatRoomGemini;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ChatRoomGeminiDAO_Impl implements ChatRoomGeminiDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ChatRoomGemini> __insertionAdapterOfChatRoomGemini;

  private final EntityDeletionOrUpdateAdapter<ChatRoomGemini> __deletionAdapterOfChatRoomGemini;

  private final EntityDeletionOrUpdateAdapter<ChatRoomGemini> __updateAdapterOfChatRoomGemini;

  public ChatRoomGeminiDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChatRoomGemini = new EntityInsertionAdapter<ChatRoomGemini>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `chatRoomGemini` (`id`,`name`) VALUES (nullif(?, 0),?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ChatRoomGemini entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
      }
    };
    this.__deletionAdapterOfChatRoomGemini = new EntityDeletionOrUpdateAdapter<ChatRoomGemini>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `chatRoomGemini` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ChatRoomGemini entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfChatRoomGemini = new EntityDeletionOrUpdateAdapter<ChatRoomGemini>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `chatRoomGemini` SET `id` = ?,`name` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ChatRoomGemini entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindLong(3, entity.getId());
      }
    };
  }

  @Override
  public long insertChatRoomGemini(final ChatRoomGemini chatRoomGemini) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfChatRoomGemini.insertAndReturnId(chatRoomGemini);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertChatRoomGeminis(final List<ChatRoomGemini> chatRoomGeminis) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfChatRoomGemini.insert(chatRoomGeminis);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final ChatRoomGemini chatRoomGemini) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfChatRoomGemini.handle(chatRoomGemini);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateChatRoomGemini(final ChatRoomGemini chatRoomGemini) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfChatRoomGemini.handle(chatRoomGemini);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<ChatRoomGemini> getListChatRoomGemini() {
    final String _sql = "SELECT * FROM chatRoomGemini";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final List<ChatRoomGemini> _result = new ArrayList<ChatRoomGemini>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final ChatRoomGemini _item;
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item = new ChatRoomGemini(_tmpName);
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

  @Override
  public ChatRoomGemini getChatRoomById(final long id) {
    final String _sql = "SELECT * FROM chatRoomGemini WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final ChatRoomGemini _result;
      if (_cursor.moveToFirst()) {
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _result = new ChatRoomGemini(_tmpName);
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _result.setId(_tmpId);
      } else {
        _result = null;
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

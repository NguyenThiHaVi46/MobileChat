package com.example.mychat.data.RoomData;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.mychat.DAO.ChatRoomGeminiDAO;
import com.example.mychat.DAO.ChatRoomGeminiDAO_Impl;
import com.example.mychat.DAO.MessageAIDAO;
import com.example.mychat.DAO.MessageAIDAO_Impl;
import com.example.mychat.DAO.UserDAO;
import com.example.mychat.DAO.UserDAO_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class Data_Impl extends Data {
  private volatile MessageAIDAO _messageAIDAO;

  private volatile ChatRoomGeminiDAO _chatRoomGeminiDAO;

  private volatile UserDAO _userDAO;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `messageAi` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `text` TEXT, `isSentByUser` INTEGER NOT NULL, `image` BLOB, `timestamp` INTEGER NOT NULL, `roomId` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `chatRoomGemini` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user` (`phoneNumber` TEXT, `username` TEXT, `timestamp` INTEGER, `userId` TEXT NOT NULL, `fcmToken` TEXT, `password` TEXT, `email` TEXT, PRIMARY KEY(`userId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '073ef2aad6bc060c014b0ca8b0286468')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `messageAi`");
        db.execSQL("DROP TABLE IF EXISTS `chatRoomGemini`");
        db.execSQL("DROP TABLE IF EXISTS `user`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsMessageAi = new HashMap<String, TableInfo.Column>(6);
        _columnsMessageAi.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageAi.put("text", new TableInfo.Column("text", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageAi.put("isSentByUser", new TableInfo.Column("isSentByUser", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageAi.put("image", new TableInfo.Column("image", "BLOB", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageAi.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageAi.put("roomId", new TableInfo.Column("roomId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMessageAi = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMessageAi = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMessageAi = new TableInfo("messageAi", _columnsMessageAi, _foreignKeysMessageAi, _indicesMessageAi);
        final TableInfo _existingMessageAi = TableInfo.read(db, "messageAi");
        if (!_infoMessageAi.equals(_existingMessageAi)) {
          return new RoomOpenHelper.ValidationResult(false, "messageAi(com.example.mychat.models.MessageAi).\n"
                  + " Expected:\n" + _infoMessageAi + "\n"
                  + " Found:\n" + _existingMessageAi);
        }
        final HashMap<String, TableInfo.Column> _columnsChatRoomGemini = new HashMap<String, TableInfo.Column>(2);
        _columnsChatRoomGemini.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatRoomGemini.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChatRoomGemini = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesChatRoomGemini = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoChatRoomGemini = new TableInfo("chatRoomGemini", _columnsChatRoomGemini, _foreignKeysChatRoomGemini, _indicesChatRoomGemini);
        final TableInfo _existingChatRoomGemini = TableInfo.read(db, "chatRoomGemini");
        if (!_infoChatRoomGemini.equals(_existingChatRoomGemini)) {
          return new RoomOpenHelper.ValidationResult(false, "chatRoomGemini(com.example.mychat.models.ChatRoomGemini).\n"
                  + " Expected:\n" + _infoChatRoomGemini + "\n"
                  + " Found:\n" + _existingChatRoomGemini);
        }
        final HashMap<String, TableInfo.Column> _columnsUser = new HashMap<String, TableInfo.Column>(7);
        _columnsUser.put("phoneNumber", new TableInfo.Column("phoneNumber", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("username", new TableInfo.Column("username", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("userId", new TableInfo.Column("userId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("fcmToken", new TableInfo.Column("fcmToken", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("password", new TableInfo.Column("password", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUser = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUser = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUser = new TableInfo("user", _columnsUser, _foreignKeysUser, _indicesUser);
        final TableInfo _existingUser = TableInfo.read(db, "user");
        if (!_infoUser.equals(_existingUser)) {
          return new RoomOpenHelper.ValidationResult(false, "user(com.example.mychat.models.User).\n"
                  + " Expected:\n" + _infoUser + "\n"
                  + " Found:\n" + _existingUser);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "073ef2aad6bc060c014b0ca8b0286468", "cb2def73a11b21c2121823100b609039");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "messageAi","chatRoomGemini","user");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `messageAi`");
      _db.execSQL("DELETE FROM `chatRoomGemini`");
      _db.execSQL("DELETE FROM `user`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(MessageAIDAO.class, MessageAIDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChatRoomGeminiDAO.class, ChatRoomGeminiDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserDAO.class, UserDAO_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public MessageAIDAO messageAIDAO() {
    if (_messageAIDAO != null) {
      return _messageAIDAO;
    } else {
      synchronized(this) {
        if(_messageAIDAO == null) {
          _messageAIDAO = new MessageAIDAO_Impl(this);
        }
        return _messageAIDAO;
      }
    }
  }

  @Override
  public ChatRoomGeminiDAO chatRoomGeminiDAO() {
    if (_chatRoomGeminiDAO != null) {
      return _chatRoomGeminiDAO;
    } else {
      synchronized(this) {
        if(_chatRoomGeminiDAO == null) {
          _chatRoomGeminiDAO = new ChatRoomGeminiDAO_Impl(this);
        }
        return _chatRoomGeminiDAO;
      }
    }
  }

  @Override
  public UserDAO userDAO() {
    if (_userDAO != null) {
      return _userDAO;
    } else {
      synchronized(this) {
        if(_userDAO == null) {
          _userDAO = new UserDAO_Impl(this);
        }
        return _userDAO;
      }
    }
  }
}

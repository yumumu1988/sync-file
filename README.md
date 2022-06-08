# sync-file

## 数据库

### 文件记录表

    create table FILE_RECORD
    (
    ID          INTEGER auto_increment,
    FILENAME    varchar(255) not null,
    FILE_SIZE   BIGINT    not null,
    MD5         varchar(255) not null,
    VERSION     INTEGER   not null,
    CREATE_TIME TIMESTAMP not null,
    ENABLE      TINYINT   not null,
    TEMP_NAME   varchar(255) not NULL,
    CLIENT_ID   varchar(255) not NULL,
    constraint FILE_RECORD_PK
    primary key (ID)
    );
    
    comment on table FILE_RECORD is '文件记录表';
    
    create index FILE_RECORD_FILENAME_INDEX
    on FILE_RECORD (FILENAME);

### 客户端表

    create table CLIENTS
    (
    ID              INTEGER auto_increment,
    CLIENT_ID       varchar(255) not null,
    CREATE_TIME     TIMESTAMP not null,
    UPDATE_TIME     TIMESTAMP not null,
    CURRENT_FILE_ID INTEGER   not null,
    CLIENT_IP       varchar(255) not null,
    constraint CLIENTS_PK
    primary key (ID)
    );
    
    create index CLIENTS_CLIENT_ID_INDEX
    on CLIENTS (CLIENT_ID);
-- This SQL script initializes the Azure SQL Edge database with necessary tables and configurations.

IF OBJECT_ID('dbo.Users', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Users (
        UserId INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_Users PRIMARY KEY,
        UserName NVARCHAR(100) NOT NULL,
        Email NVARCHAR(100) NOT NULL CONSTRAINT UQ_Users_Email UNIQUE,
        CreatedAt DATETIME NOT NULL CONSTRAINT DF_Users_CreatedAt DEFAULT (GETDATE())
    );
END;

IF OBJECT_ID('dbo.Messages', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Messages (
        MessageId INT IDENTITY(1,1) NOT NULL CONSTRAINT PK_Messages PRIMARY KEY,
        UserId INT NOT NULL,
        Content NVARCHAR(MAX) NOT NULL,
        CreatedAt DATETIME NOT NULL CONSTRAINT DF_Messages_CreatedAt DEFAULT (GETDATE()),
        CONSTRAINT FK_Messages_Users FOREIGN KEY (UserId) REFERENCES dbo.Users(UserId)
    );
END;

IF NOT EXISTS (SELECT 1 FROM dbo.Users WHERE Email = 'john.doe@example.com')
    INSERT INTO dbo.Users (UserName, Email) VALUES ('John Doe', 'john.doe@example.com');

IF NOT EXISTS (SELECT 1 FROM dbo.Users WHERE Email = 'jane.smith@example.com')
    INSERT INTO dbo.Users (UserName, Email) VALUES ('Jane Smith', 'jane.smith@example.com');
use rtb

IF OBJECT_ID('dbo.BidResponses', 'u') IS NOT NULL 
	DROP TABLE BidResponses
IF OBJECT_ID('dbo.BidRequests', 'u') IS NOT NULL 
	DROP TABLE BidRequests
IF OBJECT_ID('dbo.Campaigns', 'u') IS NOT NULL 
	DROP TABLE Campaigns
IF OBJECT_ID('dbo.Targetings', 'u') IS NOT NULL 
	DROP TABLE Targetings
IF OBJECT_ID('dbo.TargetedSites', 'u') IS NOT NULL 
	DROP TABLE TargetedSites
IF OBJECT_ID('dbo.Countries', 'u') IS NOT NULL 
	DROP TABLE Countries
IF OBJECT_ID('dbo.Cities', 'u') IS NOT NULL 
	DROP TABLE Cities
IF OBJECT_ID('dbo.Devices', 'u') IS NOT NULL 
	DROP TABLE Devices
IF OBJECT_ID('dbo.Users', 'u') IS NOT NULL 
 DROP TABLE Users
IF OBJECT_ID('dbo.Banners', 'u') IS NOT NULL 
	DROP TABLE Banners
IF OBJECT_ID('dbo.Sites', 'u') IS NOT NULL 
	DROP TABLE Sites
IF OBJECT_ID('dbo.Geos', 'u') IS NOT NULL 
	DROP TABLE Geos
IF OBJECT_ID('dbo.Impressions', 'u') IS NOT NULL 
	DROP TABLE Impressions
IF OBJECT_ID('dbo.DeviceTypes', 'u') IS NOT NULL 
	DROP TABLE DeviceTypes
IF OBJECT_ID('dbo.Pmps', 'u') IS NOT NULL 
	DROP TABLE Pmps
IF OBJECT_ID('dbo.Deals', 'u') IS NOT NULL 
	DROP TABLE Deals
IF OBJECT_ID('dbo.Publishers', 'u') IS NOT NULL 
	DROP TABLE Publishers

CREATE TABLE Banners (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Source VARCHAR(128) NOT NULL,
	Description VARCHAR(512) NOT NULL,
    Width INT NOT NULL,
	Height INT NOT NULL,
	Position INT NOT NULL
);

CREATE TABLE Sites (
    Id INT IDENTITY(1,1) PRIMARY KEY,
	Name VARCHAR(32) NOT NULL,
    Domain VARCHAR(512) NOT NULL,
	Page VARCHAR(512) NULL,
	Category VARCHAR(512) NULL
);

CREATE TABLE Publishers (
    Id INT IDENTITY(1,1) PRIMARY KEY,
	Name VARCHAR(512) NOT NULL,
    Domain VARCHAR(512) NOT NULL,
	Category VARCHAR(512) NULL
);

CREATE TABLE Geos (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Country VARCHAR(64) NULL,
	City VARCHAR(64) NULL,
	Lat DOUBLE PRECISION NOT NULL,
	Lon DOUBLE PRECISION NOT NULL,
);

CREATE TABLE Impressions (
    Id INT IDENTITY(1,1) PRIMARY KEY,
	WidthMin INT NOT NULL,
	WidthMax INT NOT NULL,
	Width INT NOT NULL,
	HeightMin INT NOT NULL,
	HeightMax INT NOT NULL,
	Height INT NOT NULL,
	BidFloor DECIMAL(13, 2) NOT NULL
);

CREATE TABLE Deals (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    BidFloor DECIMAL(13, 2) NOT NULL,
	Currency VARCHAR(64) NOT NULL,
	AuctionType INT NULL,
	WSeat VARCHAR(MAX) NULL,
	WaDomain VARCHAR(512) NULL
);

CREATE TABLE DeviceTypes (
    Id INT IDENTITY(1,1) PRIMARY KEY,
	Name VARCHAR(512) NOT NULL,
    Description VARCHAR(512) NOT NULL,
	Notes VARCHAR(512) NULL
);

CREATE TABLE Pmps (
    Id INT IDENTITY(1,1) PRIMARY KEY,
	PrivateAuction INT,
	DealId INT FOREIGN KEY REFERENCES Deals(Id)
);

CREATE TABLE Users (
    Id INT IDENTITY(1,1) PRIMARY KEY,
	Name VARCHAR(64) NOT NULL,
	BuyerId VARCHAR(512) NULL,
	YearOfBirth INT,
	Gender VARCHAR(8),
    GeoId INT FOREIGN KEY REFERENCES Geos(Id)
);

CREATE TABLE Devices (
    Id INT IDENTITY(1,1) PRIMARY KEY,
	Make VARCHAR(64) NULL,
	Model VARCHAR(64) NULL,
	OS VARCHAR(64) NULL,
	Language VARCHAR(64) NULL,
	DeviceTypeId INT FOREIGN KEY REFERENCES DeviceTypes(Id),
    GeoId INT FOREIGN KEY REFERENCES Geos(Id)
);

CREATE TABLE Countries (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name VARCHAR(64) NOT NULL,
	Notes VARCHAR(512) NOT NULL
);

CREATE TABLE Cities (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name VARCHAR(64) NOT NULL,
	Notes VARCHAR(512) NOT NULL
);

CREATE TABLE TargetedSites (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name VARCHAR(64) NOT NULL,
	Domain VARCHAR(512) NOT NULL,
	Url VARCHAR(512) NOT NULL
);

CREATE TABLE Targetings (
    CityId INT NOT NULL,
	TargetSiteId INT NOT NULL,
	FOREIGN KEY (CityId) REFERENCES Cities(Id),
	FOREIGN KEY (TargetSiteId) REFERENCES TargetedSites(Id),
	UNIQUE (CityId, TargetSiteId)
);

CREATE TABLE Campaigns (
    Id INT IDENTITY(1,1) PRIMARY KEY,
	BidPrice DECIMAL(13, 2) NOT NULL,
	Currency VARCHAR(64) NOT NULL,
	CityId INT FOREIGN KEY REFERENCES Cities(Id),
	CountryId INT FOREIGN KEY REFERENCES Countries(Id),
	UserId INT FOREIGN KEY REFERENCES Users(Id),
	TargetSiteId INT FOREIGN KEY REFERENCES TargetedSites(Id),
	BannerId INT FOREIGN KEY REFERENCES Banners(Id)
);

CREATE TABLE BidRequests (
    Id INT IDENTITY(1,1) PRIMARY KEY,
	AuctionType INT NOT NULL,
	Currency VARCHAR(64) NOT NULL,
	SiteId INT FOREIGN KEY REFERENCES Sites(Id),
	DeviceId INT FOREIGN KEY REFERENCES Devices(Id),
	BannerId INT FOREIGN KEY REFERENCES Banners(Id),
	PublisherId INT FOREIGN KEY REFERENCES Publishers(Id),
	ImpressionId INT FOREIGN KEY REFERENCES Impressions(Id),
	UserId INT FOREIGN KEY REFERENCES Users(Id)
);


CREATE TABLE BidResponses (
    Id INT IDENTITY(1,1) PRIMARY KEY,
	Price DECIMAL(13, 2) NOT NULL,
	Currency VARCHAR(64) NOT NULL,
	BidRequestId VARCHAR(255) NOT NULL,
	AdId INT FOREIGN KEY REFERENCES Campaigns(Id),
	BannerId INT FOREIGN KEY REFERENCES Banners(Id)
);
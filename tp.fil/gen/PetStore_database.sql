	
		CREATE TABLE SellerContactInfo
		(
			contactInfoID String
,			lastName String
,			firstName String
,			email String
		)

		CREATE TABLE Tag
		(
			tagID int
,			items Collection<Item>
,			tag String
,			refCount int
		)

		CREATE TABLE Address
		(
			addressID String
,			street1 String
,			street2 String
,			city String
,			state String
,			zip String
,			latitude double
,			longitude double
,			COMMA String
		)

		CREATE TABLE FileUploadResponse
		(
			itemId String
,			productId String
,			message String
,			status String
,			duration String
,			durationString String
,			startDate String
,			endDate String
,			uploadSize String
,			thumbnail String
		)

		CREATE TABLE Category
		(
			categoryID String
,			name String
,			description String
,			imageURL String
		)

		CREATE TABLE PayPalBean
		(
			postData BuyNowPostData
		)

		CREATE TABLE ZipLocation
		(
			zipCode int
,			city String
,			state String
		)

		CREATE TABLE Product
		(
			productID String
,			categoryID String
,			name String
,			description String
,			imageURL String
		)


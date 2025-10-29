Product (ProductDetails)
├─ productId : String
├─ productType : String (INAPP | SUBS)
├─ title : String
├─ name : String (tên hiển thị)
├─ description : String
├─ defaultPrice : Money (giá mặc định)
│
├─ OneTimePurchaseOfferDetails (chỉ có nếu là INAPP)
│   ├─ priceAmountMicros : Long
│   ├─ priceCurrencyCode : String
│   └─ formattedPrice : String
│
└─ SubscriptionOfferDetails[] (chỉ có nếu là SUBS)
    ├─ basePlanId : String
    ├─ offerId : String (có thể null)
    ├─ offerToken : String (dùng khi launchBillingFlow)
    ├─ offerTags : List<String>
    │
    ├─ pricingPhases[]
    │   ├─ pricingPhase #1
    │   │   ├─ priceAmountMicros : Long
    │   │   ├─ priceCurrencyCode : String
    │   │   ├─ formattedPrice : String
    │   │   ├─ billingPeriod : String (P1W, P1M, P1Y)
    │   │   ├─ recurrenceMode : Int (FINITE_RECURRING / INFINITE_RECURRING)
    │   │   └─ recurrenceCount : Int
    │   └─ pricingPhase #2 (nếu có — ví dụ intro, free trial)
    │
    ├─ recurrenceType : String (AUTO_RENEW | PREPAID)
    ├─ billingPeriod : String (chu kỳ thanh toán, e.g. P1M)
    ├─ basePricingPhases[] (phần giá gốc)
    │
    ├─ eligibilityRules[] (điều kiện áp dụng offer)
    │   ├─ isUserEligible : Boolean
    │   └─ regionRestriction : List<String>
    │
    └─ otherMetadata...

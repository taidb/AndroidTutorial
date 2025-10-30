Product (ProductDetails) ( thông tin sản phẩm)
├─ productId : String
├─ productType : String (INAPP | SUBS) INAPP : sản phẩm mua 1 lần sở hữu vĩnh viễn |SUBS: đăng kí định kì để sự dụng dịch vụ
├─ title : String
├─ name : String (tên hiển thị)
├─ description : String
├─ defaultPrice : Money (giá mặc định) -> show nhanh giá nếu ko muốn phân tích các giai đoạn định giá
│
├─ OneTimePurchaseOfferDetails (chỉ có nếu là INAPP) -> giá cố định
│   ├─ priceAmountMicros : Long( đơn vị 1.000.000 micro) -> giúp tính toán chính xacs
│   ├─ priceCurrencyCode : String (mã tiền)
│   └─ formattedPrice : String
│
└─ SubscriptionOfferDetails[] (chỉ có nếu là SUBS) -> có thể có nhiều base plan( chu kì,loại gia hạn)/offer( giảm giá,free trial, giá intro...)
    ├─ basePlanId : String ( phân biệt chu kỳ và loại plan)
    ├─ offerId : String (có thể null -> giá gốc)
    ├─ offerToken : String (dùng khi launchBillingFlow để Play biết mình đang mua đúng biến thể)
    ├─ offerTags : List<String>
    │
    ├─ pricingPhases[] ( danh sách cacs giai đoạn)
    │   ├─ pricingPhase #1
    │   │   ├─ priceAmountMicros : Long
    │   │   ├─ priceCurrencyCode : String
    │   │   ├─ formattedPrice : String
    │   │   ├─ billingPeriod : String (P1W, P1M, P1Y) ( chu kỳ thanh toán)
    │   │   ├─ recurrenceMode : Int (FINITE_RECURRING / INFINITE_RECURRING /NON_RECURRING)
    │   │   └─ recurrenceCount : Int (số lần lặp chỉ áp dụng cho FINITE_RECURRING)
    │   └─ pricingPhase #2 (nếu có — ví dụ intro, free trial)
    │
    ├─ recurrenceType : String (AUTO_RENEW | PREPAID)
    ├─ billingPeriod : String (chu kỳ thanh toán, e.g. P1M)
    ├─ basePricingPhases[] (phần giá gốc)
    │
    ├─ eligibilityRules[] (điều kiện áp dụng offer)
       ├─ isUserEligible : Boolean ( cho biết user hiện tại có đủ điều kiện để dun offer)
       └─ regionRestriction : List<String>


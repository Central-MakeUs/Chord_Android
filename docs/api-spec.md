# Chord_Server /coachcoach API 스펙 (회원가입/로그인/토큰갱신, 메뉴, 재료)

범위: Chord_Server `/coachcoach` 기준의 인증(Auth), 메뉴(Menu), 재료(Ingredient) API 문서.

## 공통
- Base Path: `/api/v1`
- Auth Header: `Authorization: Bearer {accessToken}`
- Public 엔드포인트:
  - `POST /auth/sign-up`
  - `POST /auth/login`
  - `POST /auth/refresh`
  - `GET /catalog/menu-categories`
  - `GET /catalog/menus/search`
  - `GET /catalog/menus/templates/{templateId}`
  - `GET /catalog/menus/templates/{templateId}/ingredients`
- 공통 응답 포맷
  - 성공:
    - `success` (boolean)
    - `data` (object)
    - `timestamp` (string, 포맷 TBD)
    - `message` (string, 서버 래퍼에 optional로 존재)
  - 실패:
    - `success` (boolean)
    - `code` (string)
    - `message` (string)
    - `errors` (object, optional, `field -> message` 맵)
    - `timestamp` (string, 포맷 TBD)
- timestamp 포맷 미확정 예시(타임존 접미사 없음): `2026-01-05T22:13:24.0727464`

예시(성공):
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOi...",
    "refreshToken": "eyJhbGciOi...",
    "onboardingCompleted": false
  },
  "timestamp": "2026-01-05T22:13:24.0727464",
  "message": "OK"
}
```

예시(실패, errors 맵 포함):
```json
{
  "success": false,
  "code": "COMMON_001",
  "message": "입력값이 올바르지 않습니다",
  "errors": {
    "loginId": "3~20자 소문자+숫자만 가능"
  },
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

공통 응답 소스: `coachcoach/common/src/main/java/com/coachcoach/common/dto/ApiResponse.java`, `coachcoach/common/src/main/java/com/coachcoach/common/dto/ErrorResponse.java`

---

## Auth (회원가입/로그인/토큰갱신)
Source:
- Controller: `coachcoach/app/src/main/java/com/coachcoach/api/user/AuthController.java`
- DTO: `coachcoach/user/src/main/java/com/coachcoach/user/dto/request/*`, `coachcoach/user/src/main/java/com/coachcoach/user/dto/response/*`, `coachcoach/user/src/main/java/com/coachcoach/user/dto/validation/*`

### POST /auth/sign-up
- 인증: 없음 (Public)
- Request Body (SignUpRequest)
  - `loginId` (string, required)
    - 길이 3~20
    - 소문자+숫자만
    - 전체가 숫자만이면 안 됨
  - `password` (string, required)
    - 길이 8~100
    - 대/소문자/숫자/특수문자 중 2종류 이상 포함
    - `loginId`를 포함하면 안 됨
- Response Data: `data: {}`

Sample Request:
```json
{
  "loginId": "coach01",
  "password": "Passw0rd!"
}
```

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### POST /auth/login
- 인증: 없음 (Public)
- Request Body (LoginRequest)
  - `loginId` (string, required)
  - `password` (string, required)
- Response Data (LoginResponse)
  - `accessToken` (string)
  - `refreshToken` (string)
  - `onboardingCompleted` (boolean)

Sample Request:
```json
{
  "loginId": "coach01",
  "password": "Passw0rd!"
}
```

Sample Response:
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOi...",
    "refreshToken": "eyJhbGciOi...",
    "onboardingCompleted": false
  },
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### POST /auth/refresh
- 인증: 없음 (Public)
- Request Body (TokenRefreshRequest)
  - `refreshToken` (string, required)
- Response Data (TokenRefreshResponse)
  - `accessToken` (string)

Sample Request:
```json
{
  "refreshToken": "eyJhbGciOi..."
}
```

Sample Response:
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOi..."
  },
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

---

## Menu (메뉴)
Source:
- Controller: `coachcoach/app/src/main/java/com/coachcoach/api/catalog/CatalogController.java`
- DTO: `coachcoach/catalog/src/main/java/com/coachcoach/catalog/dto/request/*`, `coachcoach/catalog/src/main/java/com/coachcoach/catalog/dto/response/*`

### GET /catalog/menu-categories
- 인증: 없음 (Public)
- Response Data (MenuCategoryResponse[])
  - `categoryCode` (string)
  - `categoryName` (string)
  - `displayOrder` (number)

Sample Response:
```json
{
  "success": true,
  "data": [
    {
      "categoryCode": "COFFEE",
      "categoryName": "커피",
      "displayOrder": 1
    }
  ],
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### GET /catalog/menus/search?keyword
- 인증: 없음 (Public)
- Query
  - `keyword` (string, optional)
- Response Data (SearchMenusResponse[])
  - `templateId` (number)
  - `menuName` (string)

Sample Response:
```json
{
  "success": true,
  "data": [
    { "templateId": 12, "menuName": "아메리카노" }
  ],
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### GET /catalog/menus/templates/{templateId}
- 인증: 없음 (Public)
- Path
  - `templateId` (number, required)
- Response Data (TemplateBasicResponse)
  - `templateId` (number)
  - `menuName` (string)
  - `defaultSellingPrice` (number)
  - `categoryCode` (string)
  - `workTime` (number)

Sample Response:
```json
{
  "success": true,
  "data": {
    "templateId": 12,
    "menuName": "아메리카노",
    "defaultSellingPrice": 4500,
    "categoryCode": "COFFEE",
    "workTime": 120
  },
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### GET /catalog/menus/templates/{templateId}/ingredients
- 인증: 없음 (Public)
- Path
  - `templateId` (number, required)
- Response Data (RecipeTemplateResponse[])
  - `ingredientName` (string)
  - `defaultUsageAmount` (number)
  - `defaultPrice` (number)
  - `unitCode` (string)

Sample Response:
```json
{
  "success": true,
  "data": [
    {
      "ingredientName": "원두",
      "defaultUsageAmount": 18,
      "defaultPrice": 320,
      "unitCode": "G"
    }
  ],
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### GET /catalog/menus?category
- 인증: 필요
- Query
  - `category` (string, optional)
- Response Data (MenuResponse[])
  - `menuId` (number)
  - `menuName` (string)
  - `sellingPrice` (number)
  - `costRate` (number)
  - `marginGradeCode` (string)
  - `marginGradeName` (string)
  - `marginRate` (number)

Sample Response:
```json
{
  "success": true,
  "data": [
    {
      "menuId": 101,
      "menuName": "아메리카노",
      "sellingPrice": 4500,
      "costRate": 32.1,
      "marginGradeCode": "A",
      "marginGradeName": "우수",
      "marginRate": 67.9
    }
  ],
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### GET /catalog/menus/{menuId}
- 인증: 필요
- Path
  - `menuId` (number, required)
- Response Data (MenuDetailResponse)
  - `menuId` (number)
  - `menuName` (string)
  - `workTime` (number)
  - `sellingPrice` (number)
  - `marginRate` (number)
  - `totalCost` (number)
  - `costRate` (number)
  - `contributionMargin` (number)
  - `marginGradeCode` (string)
  - `marginGradeName` (string)
  - `marginGradeMessage` (string)
  - `recommendedPrice` (number)

Sample Response:
```json
{
  "success": true,
  "data": {
    "menuId": 101,
    "menuName": "아메리카노",
    "workTime": 120,
    "sellingPrice": 4500,
    "marginRate": 67.9,
    "totalCost": 1445,
    "costRate": 32.1,
    "contributionMargin": 3055,
    "marginGradeCode": "A",
    "marginGradeName": "우수",
    "marginGradeMessage": "가격 경쟁력이 높습니다.",
    "recommendedPrice": 4800
  },
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### GET /catalog/menus/{menuId}/recipes
- 인증: 필요
- Path
  - `menuId` (number, required)
- Response Data (RecipeListResponse)
  - `recipes` (array of RecipeResponse)
    - `recipeId` (number)
    - `menuId` (number)
    - `ingredientId` (number)
    - `ingredientName` (string)
    - `amount` (number)
    - `unitCode` (string)
    - `price` (number)
  - `totalCost` (number)

Sample Response:
```json
{
  "success": true,
  "data": {
    "recipes": [
      {
        "recipeId": 5001,
        "menuId": 101,
        "ingredientId": 2001,
        "ingredientName": "원두",
        "amount": 18,
        "unitCode": "G",
        "price": 320
      }
    ],
    "totalCost": 1445
  },
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### POST /catalog/menus/check-dup
- 인증: 필요
- Request Body (CheckDupRequest)
  - `menuName` (string, required, NotBlank)
  - `ingredientNames` (array of string, optional)
- Response Data (CheckDupResponse)
  - `menuNameDuplicate` (boolean)
  - `dupIngredientNames` (array of string)

Sample Request:
```json
{
  "menuName": "아메리카노",
  "ingredientNames": ["원두", "물"]
}
```

Sample Response:
```json
{
  "success": true,
  "data": {
    "menuNameDuplicate": true,
    "dupIngredientNames": ["원두"]
  },
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### POST /catalog/menus
- 인증: 필요
- Request Body (MenuCreateRequest)
  - `menuCategoryCode` (string, required, NotBlank)
  - `menuName` (string, required, NotBlank)
  - `sellingPrice` (number, required, NotNull, Positive)
  - `workTime` (number, required, NotNull, Positive)
  - `recipes` (array of RecipeCreateRequest, optional)
    - `ingredientId` (number, optional)
    - `amount` (number, required, NotNull, Positive)
  - `newRecipes` (array of NewRecipeCreateRequest, optional)
    - `amount` (number, required)
    - `price` (number, required)
    - `unitCode` (string, required)
    - `ingredientCategoryCode` (string, required)
    - `ingredientName` (string, required)
    - `supplier` (string, optional)
- Response Data: `data: {}`

Sample Request:
```json
{
  "menuCategoryCode": "COFFEE",
  "menuName": "아메리카노",
  "sellingPrice": 4500,
  "workTime": 120,
  "recipes": [
    { "ingredientId": 2001, "amount": 18 }
  ],
  "newRecipes": [
    {
      "amount": 250,
      "price": 100,
      "unitCode": "ML",
      "ingredientCategoryCode": "WATER",
      "ingredientName": "정수물",
      "supplier": "사내"
    }
  ]
}
```

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### POST /catalog/menus/{menuId}/recipes/existing
- 인증: 필요
- Path
  - `menuId` (number, required)
- Request Body (RecipeCreateRequest)
  - `ingredientId` (number, optional)
  - `amount` (number, required, NotNull, Positive)
- Response Data: `data: {}`

Sample Request:
```json
{
  "ingredientId": 2002,
  "amount": 20
}
```

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### POST /catalog/menus/{menuId}/recipes/new
- 인증: 필요
- Path
  - `menuId` (number, required)
- Request Body (NewRecipeCreateRequest)
  - `amount` (number, required)
  - `price` (number, required)
  - `unitCode` (string, required)
  - `ingredientCategoryCode` (string, required)
  - `ingredientName` (string, required)
  - `supplier` (string, optional)
- Response Data: `data: {}`

Sample Request:
```json
{
  "amount": 30,
  "price": 200,
  "unitCode": "G",
  "ingredientCategoryCode": "BEAN",
  "ingredientName": "블렌드 원두",
  "supplier": "로스터리A"
}
```

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### PATCH /catalog/menus/{menuId}
- 인증: 필요
- Path
  - `menuId` (number, required)
- Request Body (MenuNameUpdateRequest)
  - `menuName` (string, required, NotBlank)
- Response Data: `data: {}`

Sample Request:
```json
{
  "menuName": "아이스 아메리카노"
}
```

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### PATCH /catalog/menus/{menuId}/price
- 인증: 필요
- Path
  - `menuId` (number, required)
- Request Body (MenuPriceUpdateRequest)
  - `sellingPrice` (number, required, NotNull, Positive)
- Response Data: `data: {}`

Sample Request:
```json
{
  "sellingPrice": 4800
}
```

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### PATCH /catalog/menus/{menuId}/category
- 인증: 필요
- Path
  - `menuId` (number, required)
- Request Body (MenuCategoryUpdateRequest)
  - `category` (string, required, NotBlank)
- Response Data: `data: {}`

Sample Request:
```json
{
  "category": "NON_COFFEE"
}
```

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### PATCH /catalog/menus/{menuId}/worktime
- 인증: 필요
- Path
  - `menuId` (number, required)
- Request Body (MenuWorktimeUpdateRequest)
  - `workTime` (number, required, NotNull, Positive)
- Response Data: `data: {}`

Sample Request:
```json
{
  "workTime": 150
}
```

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### PATCH /catalog/menus/{menuId}/recipes/{recipeId}
- 인증: 필요
- Path
  - `menuId` (number, required)
  - `recipeId` (number, required)
- Request Body (AmountUpdateRequest)
  - `amount` (number, required, NotNull, Positive)
- Response Data: `data: {}`

Sample Request:
```json
{
  "amount": 22
}
```

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### DELETE /catalog/menus/{menuId}/recipes
- 인증: 필요
- Path
  - `menuId` (number, required)
- Request Body (DeleteRecipesRequest)
  - `recipeIds` (array of number, optional)
- Response Data: `data: {}`

Sample Request:
```json
{
  "recipeIds": [5001, 5002]
}
```

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### DELETE /catalog/menus/{menuId}
- 인증: 필요
- Path
  - `menuId` (number, required)
- Response Data: `data: {}`

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

---

## Ingredient (재료)
Source:
- Controller: `coachcoach/app/src/main/java/com/coachcoach/api/catalog/CatalogController.java`
- DTO: `coachcoach/catalog/src/main/java/com/coachcoach/catalog/dto/request/*`, `coachcoach/catalog/src/main/java/com/coachcoach/catalog/dto/response/*`

### GET /catalog/ingredient-categories
- 인증: 필요
- Response Data (IngredientCategoryResponse[])
  - `categoryCode` (string)
  - `categoryName` (string)
  - `displayOrder` (number)

Sample Response:
```json
{
  "success": true,
  "data": [
    {
      "categoryCode": "BEAN",
      "categoryName": "원두",
      "displayOrder": 1
    }
  ],
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### GET /catalog/ingredients?category
- 인증: 필요
- Query
  - `category` (string, optional; `list` 등)
- Response Data (IngredientResponse[])
  - `ingredientId` (number)
  - `ingredientCategoryCode` (string)
  - `ingredientName` (string)
  - `unitCode` (string)
  - `baseQuantity` (number)
  - `currentUnitPrice` (number)

Sample Response:
```json
{
  "success": true,
  "data": [
    {
      "ingredientId": 2001,
      "ingredientCategoryCode": "BEAN",
      "ingredientName": "원두",
      "unitCode": "G",
      "baseQuantity": 1000,
      "currentUnitPrice": 18
    }
  ],
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### GET /catalog/ingredients/{ingredientId}
- 인증: 필요
- Path
  - `ingredientId` (number, required)
- Response Data (IngredientDetailResponse)
  - `ingredientId` (number)
  - `ingredientName` (string)
  - `unitPrice` (number)
  - `baseQuantity` (number)
  - `unitCode` (string)
  - `supplier` (string, nullable)
  - `menus` (array)
  - `originalAmount` (number)
  - `originalPrice` (number)
  - `isFavorite` (boolean)

Sample Response:
```json
{
  "success": true,
  "data": {
    "ingredientId": 2001,
    "ingredientName": "원두",
    "unitPrice": 18000,
    "baseQuantity": 1000,
    "unitCode": "G",
    "supplier": "로스터리A",
    "menus": [],
    "originalAmount": 1000,
    "originalPrice": 18000,
    "isFavorite": true
  },
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### GET /catalog/ingredients/{ingredientId}/price-history
- 인증: 필요
- Path
  - `ingredientId` (number, required)
- Response Data (PriceHistoryResponse[])
  - `historyId` (number)
  - `changeDate` (string)
  - `unitPrice` (number)
  - `unitCode` (string)
  - `baseQuantity` (number)

Sample Response:
```json
{
  "success": true,
  "data": [
    {
      "historyId": 9001,
      "changeDate": "2026-01-01",
      "unitPrice": 17000,
      "unitCode": "G",
      "baseQuantity": 1000
    }
  ],
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### GET /catalog/ingredients/search?keyword
- 인증: 필요
- Query
  - `keyword` (string, optional)
- Response Data (SearchIngredientsResponse[])
  - `isTemplate` (boolean)
  - `templateId` (number, optional)
  - `ingredientId` (number, optional)
  - `ingredientName` (string)

Sample Response:
```json
{
  "success": true,
  "data": [
    {
      "isTemplate": true,
      "templateId": 501,
      "ingredientName": "우유"
    }
  ],
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### GET /catalog/ingredients/check-dup?name
- 인증: 필요
- Query
  - `name` (string, required, NotBlank, max 100)
- Response Data: `data: {}`

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### GET /catalog/ingredients/search/my?keyword
- 인증: 필요
- Query
  - `keyword` (string, optional)
- Response Data (SearchMyIngredientsResponse[])
  - `ingredientId` (number)
  - `ingredientName` (string)

Sample Response:
```json
{
  "success": true,
  "data": [
    { "ingredientId": 2001, "ingredientName": "원두" }
  ],
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### POST /catalog/ingredients
- 인증: 필요
- Request Body (IngredientCreateRequest)
  - `categoryCode` (string, required, NotBlank)
  - `ingredientName` (string, required, NotBlank)
  - `unitCode` (string, required, NotBlank)
  - `price` (number, required, NotNull, Positive)
  - `amount` (number, required, NotNull, Positive)
  - `supplier` (string, optional)
- Response Data: `data: {}`

Sample Request:
```json
{
  "categoryCode": "BEAN",
  "ingredientName": "원두",
  "unitCode": "G",
  "price": 18000,
  "amount": 1000,
  "supplier": "로스터리A"
}
```

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### PATCH /catalog/ingredients/{ingredientId}/favorite?favorite
- 인증: 필요
- Path
  - `ingredientId` (number, required)
- Query
  - `favorite` (boolean, required)
- Response Data: `data: {}`

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### PATCH /catalog/ingredients/{ingredientId}/supplier
- 인증: 필요
- Path
  - `ingredientId` (number, required)
- Request Body (SupplierUpdateRequest)
  - `supplier` (string, optional)
- Response Data: `data: {}`

Sample Request:
```json
{
  "supplier": "로스터리B"
}
```

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### PATCH /catalog/ingredients/{ingredientId}
- 인증: 필요
- Path
  - `ingredientId` (number, required)
- Request Body (IngredientUpdateRequest)
  - `category` (string, required, NotBlank)
  - `price` (number, required, NotNull, Positive)
  - `amount` (number, required, NotNull, Positive)
  - `unitCode` (string, required, NotBlank)
- Response Data: `data: {}`

Sample Request:
```json
{
  "category": "BEAN",
  "price": 19000,
  "amount": 1000,
  "unitCode": "G"
}
```

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### DELETE /catalog/ingredients/{ingredientId}
- 인증: 필요
- Path
  - `ingredientId` (number, required)
- Response Data: `data: {}`

Sample Response:
```json
{
  "success": true,
  "data": {},
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

---

## 오류 응답 샘플 (코드별)
예시는 공통 실패 포맷을 따름. `errors`는 object map이며 optional이다.

### COMMON_001
```json
{
  "success": false,
  "code": "COMMON_001",
  "message": "입력값이 올바르지 않습니다",
  "errors": {
    "loginId": "3~20자 소문자+숫자만 가능"
  },
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### USER_001
```json
{
  "success": false,
  "code": "USER_001",
  "message": "이미 사용 중인 로그인 ID입니다.",
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### USER_010
```json
{
  "success": false,
  "code": "USER_010",
  "message": "존재하지 않는 아이디입니다.",
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### USER_020
```json
{
  "success": false,
  "code": "USER_020",
  "message": "비밀번호에 아이디가 포함될 수 없습니다.",
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### USER_021
```json
{
  "success": false,
  "code": "USER_021",
  "message": "비밀번호가 일치하지 않습니다.",
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### AUTH_001
```json
{
  "success": false,
  "code": "AUTH_001",
  "message": "인증이 필요합니다",
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

### AUTH_003
```json
{
  "success": false,
  "code": "AUTH_003",
  "message": "유효하지 않은 토큰입니다.",
  "timestamp": "2026-01-05T22:13:24.0727464"
}
```

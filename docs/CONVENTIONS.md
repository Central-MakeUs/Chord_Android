# Code Conventions

## Naming

### Packages
```
com.team.chord.[layer].[feature]
```

### Classes
| Type | Pattern | Example |
|------|---------|---------|
| ViewModel | `{Feature}ViewModel` | `HomeViewModel` |
| UseCase | `{Action}{Entity}UseCase` | `GetUserUseCase` |
| Repository | `{Entity}Repository` | `UserRepository` |
| RepositoryImpl | `{Entity}RepositoryImpl` | `UserRepositoryImpl` |
| Datasource | `{Feature}Datasource` | `HomeDatasource`, `RemoteHomeDatasource`, `FakeHomeDatasource` |

### Functions
- UseCase: `invoke()` operator 사용
- Repository: `get*`, `save*`, `delete*`, `observe*`
- ViewModel: `on*` (이벤트 핸들러)

## Architecture Patterns

### Clean Architecture Layers

1. **Domain Layer** (`core-domain`)
   - Entity: 순수 비즈니스 모델
   - UseCase: 비즈니스 로직 단위
   - Repository Interface: 데이터 추상화

2. **Data Layer** (`core-data`)
   - RepositoryImpl: Repository 구현체
   - DataSource: 데이터 소스 (Local/Remote)
   - Mapper: DTO ↔ Entity 변환

3. **Presentation Layer** (`feature-*`)
   - Screen: Composable 화면
   - ViewModel: UI 상태 관리
   - UiState: 불변 UI 상태 클래스

### MVVM Pattern

```kotlin
// UiState 패턴
data class HomeUiState(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val error: String? = null
)

// ViewModel
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getItemsUseCase: GetItemsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}
```

## File Structure

### Feature Module Structure
```
feature-home/
├── src/main/kotlin/com/team/chord/feature/home/
│   ├── HomeScreen.kt       # Composable
│   ├── HomeViewModel.kt    # ViewModel
│   ├── HomeUiState.kt      # UI State
│   └── navigation/         # Navigation 설정
└── build.gradle.kts
```

## Best Practices

1. **State Hoisting**: Composable은 stateless하게 유지
2. **Single Source of Truth**: UiState는 ViewModel에서만 관리
3. **Unidirectional Data Flow**: Event → ViewModel → State → UI
4. **Dependency Injection**: 모든 의존성은 Hilt로 주입

---
*Last Updated: 2026-01-02*
